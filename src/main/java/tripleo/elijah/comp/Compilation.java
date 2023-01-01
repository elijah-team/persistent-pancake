/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.comp;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.Out;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.comp.diagnostic.ExceptionDiagnostic;
import tripleo.elijah.comp.diagnostic.FileNotFoundDiagnostic;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.comp.queries.QueryEzFileToModule;
import tripleo.elijah.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah.comp.queries.QuerySourceFileToModule;
import tripleo.elijah.comp.queries.QuerySourceFileToModuleParams;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.nextgen.query.Mode;
import tripleo.elijah.nextgen.query.Operation2;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.FunctionMapHook;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.util.Helpers;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;
import tripleo.elijjah.EzLexer;
import tripleo.elijjah.EzParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public abstract class Compilation {

	public final  List<OS_Module>                   modules      = new ArrayList<OS_Module>();
	public final  List<CompilerInstructions>        cis          = new ArrayList<CompilerInstructions>();
	public final List<ElLog> elLogs = new LinkedList<ElLog>();
	final         Map<String, CompilerInstructions> fn2ci        = new HashMap<String, CompilerInstructions>();
	final  Pipeline      pipelines = new Pipeline();
	private final int                               _compilationNumber;
	private final ErrSink                           eee;
	private final Map<String, OS_Module>            fn2m         = new HashMap<String, OS_Module>();
	private final Map<String, OS_Package>           _packages    = new HashMap<String, OS_Package>();
	private final CIS _cis = new CIS();

	//
	//
	//
	public PipelineLogic pipelineLogic;
	final private USE use = new USE(this);
	//
	//
	//
	public Stages stage = Stages.O; // Output
	public  boolean do_out;
	protected boolean silent = false;
	private       IO                                io;
	private       int                               _packageCode = 1;
	private CompilationRunner    __cr;
	private CompilerInstructions rootCI;
	private int     _classCode    = 101;
	private int     _functionCode = 1001;

	public Compilation(final ErrSink eee, final IO io) {
		this.eee                = eee;
		this.io                 = io;
		this._compilationNumber = new Random().nextInt(Integer.MAX_VALUE);
	}

	public String getProjectName() {
		return rootCI.getName();
	}

	public Operation<CompilerInstructions> parseEzFile(final @NotNull File aFile) {
		try {
			final QueryEzFileToModuleParams       params = new QueryEzFileToModuleParams(aFile.getAbsolutePath(), io.readFile(aFile));
			final Operation<CompilerInstructions> x      = new QueryEzFileToModule(params).calculate();
			return x;
		} catch (FileNotFoundException aE) {
			return Operation.failure(aE);
		}
	}

	void hasInstructions(final @NotNull List<CompilerInstructions> cis,
	                     final boolean do_out,
	                     final @NotNull OptionsProcessor op) throws Exception {
		//assert cis.size() == 1;

		assert cis.size() > 0;

		rootCI = cis.get(0);

		if (__cr != null) {
			System.err.println("200 __cr != null");
			//throw new AssertionError();
		}

/*
		assert _cis != null; // final; redundant

		if (__cr == null)
			__cr = new CompilationRunner(this, _cis);
*/

		__cr.start(rootCI, do_out, op);
	}

	public void pushItem(CompilerInstructions aci) {
		_cis.onNext(aci);
	}

	public void addPipeline(PipelineMember aPl) {
		pipelines.add(aPl);
	}

	public AccessBus feedCmdLine(final @NotNull List<String> args) throws Exception {
		final ErrSink   errSink = eee == null ? new StdErrSink() : eee;
		boolean         do_out  = false /*, silent = false*/;
		final AccessBus ab;

		if (args.size() == 0) {
			System.err.println("Usage: eljc [--showtree] [-sE|O] <directory or .ez file names>");
			return null; // ab
		}

		try {
			final OptionsProcessor op = new ApacheOptionsProcessor();

			final CompilerInstructionsObserver cio = new CompilerInstructionsObserver(this, op);
			_cis._cio = cio;

			subscribeCI(cio);

			String[] args2;
			args2 = op.process(this, args);

/*
			final Options options = new Options();
			options.addOption("s", true, "stage: E: parse; O: output");
			options.addOption("showtree", false, "show tree");
			options.addOption("out", false, "make debug files");
			options.addOption("silent", false, "suppress DeduceType output to console");
			final CommandLineParser clp = new DefaultParser();
			final CommandLine       cmd = clp.parse(options, args.toArray(new String[args.size()]));

			if (cmd.hasOption("s")) {
				stage = Stages.valueOf(cmd.getOptionValue('s'));
			}
			if (cmd.hasOption("showtree")) {
				showTree = true;
			}
			if (cmd.hasOption("out")) {
				do_out = true;
			}
			if (isGitlab_ci() || cmd.hasOption("silent")) {
				silent = true;
			}

			CompilerInstructions ez_file = null;
			args2 = cmd.getArgs();
*/

			__cr = new CompilationRunner(this, _cis);
			__cr.doFindCIs(args2);

//			CompilerInstructions ez_file = null;
//
//			for (int i = 0; i < args2.length; i++) {
//				final String  file_name = args2[i];
//				final File    f         = new File(file_name);
//				final boolean matches2  = Pattern.matches(".+\\.ez$", file_name);
//				if (matches2)
//					add_ci(parseEzFile(f, file_name, eee));
//				else {
////						eee.reportError("9996 Not an .ez file "+file_name);
//					if (f.isDirectory()) {
//						final List<CompilerInstructions> ezs = searchEzFiles(f);
//						if (ezs.size() > 1) {
////								eee.reportError("9998 Too many .ez files, using first.");
//							eee.reportError("9997 Too many .ez files, be specific.");
////								add_ci(ezs.get(0));
//						} else if (ezs.size() == 0) {
//							eee.reportError("9999 No .ez files found.");
//						} else {
//							ez_file = ezs.get(0);
//							add_ci(ez_file);
//						}
//					} else
//						eee.reportError("9995 Not a directory " + f.getAbsolutePath());
//				}
//			}
//
//			System.err.println("130 GEN_LANG: " + cis.get(0).genLang());
//			findStdLib("c"); // TODO find a better place for this
//
//			for (final CompilerInstructions ci : cis) {
//				use(ci, do_out);
//			}
//
//			ab = new AccessBus(this);
//
//			if (stage == Stages.E) {
//				// do nothing. job over
//			} else {
//				ab.addPipelineLogic(PipelineLogic::new);
//
////					ab.subscribePipelineLogic(xx -> pipelineLogic = xx);
//				pipelineLogic = ab.__getPL();
//
//				ab.add(DeducePipeline::new);
//				ab.add(GeneratePipeline::new);
//				ab.add(WritePipeline::new);
////					ab.add(WriteMesonPipeline::new);
//
//
//				modules.stream().forEach(m -> pipelineLogic.addModule(m));
//
//
//				pipelines.run();
//
//				writeLogs(silent, elLogs);
//			}

//			ab =
		} catch (final Exception e) {
			errSink.exception(e);
			throw new RuntimeException(e);
		}

		return null;
//		return ab;
	}

	private void subscribeCI(final Observer<CompilerInstructions> aCio) {
		_cis.subscribe(aCio);
	}

	//
	//
	//

	private CompilerInstructions parseEzFile(final File f, final String file_name, final ErrSink errSink) throws Exception {
		System.out.println((String.format("   %s", f.getAbsolutePath())));
		if (!f.exists()) {
			errSink.reportError(
			  "File doesn't exist " + f.getAbsolutePath());
			return null;
		}

		final CompilerInstructions m = realParseEzFile(file_name, io.readFile(f), f);
		{
			String prelude = m.genLang();
			System.err.println("230 " + prelude);
			if (prelude == null) prelude = "c"; // TODO should be java for eljc
		}
		return m;
	}

	private @NotNull List<CompilerInstructions> searchEzFiles(final @NotNull File directory) {
		final List<CompilerInstructions> R = new ArrayList<CompilerInstructions>();
		final FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(final File file, final String s) {
				final boolean matches2 = Pattern.matches(".+\\.ez$", s);
				return matches2;
			}
		};
		final String[] list = directory.list(filter);
		if (list != null) {
			for (final String file_name : list) {
				try {
					final @NotNull File        file   = new File(directory, file_name);
					final CompilerInstructions ezFile = parseEzFile(file, file.toString(), eee);
					if (ezFile != null)
						R.add(ezFile);
					else
						eee.reportError("9995 ezFile is null "+file.toString());
				} catch (final Exception e) {
					eee.exception(e);
				}
			}
		}
		return R;
	}


	public static ElLog.Verbosity gitlabCIVerbosity() {
		final boolean gitlab_ci = isGitlab_ci();
		return gitlab_ci ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	public static boolean isGitlab_ci() {
		return System.getenv("GITLAB_CI") != null;
	}

	public boolean findStdLib(final String prelude_name) {
		final File local_stdlib = new File("lib_elijjah/lib-" + prelude_name + "/stdlib.ez");
		if (local_stdlib.exists()) {
			try {
				final CompilerInstructions ci = realParseEzFile(local_stdlib.getName(), io.readFile(local_stdlib), local_stdlib);
				add_ci(ci);
				return true;
			} catch (final Exception e) {
				eee.exception(e);
			}
		}
		return false;
	}

	public void use(final @NotNull CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
		use.use(compilerInstructions, do_out);    // NOTE Rust
	}

	private void add_ci(final CompilerInstructions ci) {
		cis.add(ci);
	}

//	public void use(final CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
//		final File instruction_dir = new File(compilerInstructions.getFilename()).getParentFile();
//		for (final LibraryStatementPart lsp : compilerInstructions.lsps) {
//			final String dir_name = Helpers.remove_single_quotes_from_string(lsp.getDirName());
//			final File dir;// = new File(dir_name);
//			if (dir_name.equals(".."))
//				dir = instruction_dir/*.getAbsoluteFile()*/.getParentFile();
//			else
//				dir = new File(instruction_dir, dir_name);
//			use_internal(dir, do_out, lsp);
//		}
//		final LibraryStatementPart lsp = new LibraryStatementPart();
//		lsp.setName(Helpers.makeToken("default")); // TODO: make sure this doesn't conflict
//		lsp.setDirName(Helpers.makeToken(String.format("\"%s\"", instruction_dir)));
//		lsp.setInstructions(compilerInstructions);
//		use_internal(instruction_dir, do_out, lsp);
//	}

//	private void use_internal(final File dir, final boolean do_out, final LibraryStatementPart lsp) throws Exception {
//		if (!dir.isDirectory()) {
//			eee.reportError("9997 Not a directory " + dir.toString());
//			return;
//		}
//		//
//		final FilenameFilter accept_source_files = new FilenameFilter() {
//			@Override
//			public boolean accept(final File directory, final String file_name) {
//				final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
//						             || Pattern.matches(".+\\.elijjah$", file_name);
//				return matches;
//			}
//		};
//		final File[] files = dir.listFiles(accept_source_files);
//		if (files != null) {
//			for (final File file : files) {
//				parseElijjahFile(file, file.toString(), eee, do_out, lsp);
//			}
//		}
//	}

	void writeLogs(final boolean aSilent, final @NotNull List<ElLog> aLogs) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		if (true || aSilent) {
			for (final ElLog deduceLog : aLogs) {
				logMap.put(deduceLog.getFileName(), deduceLog);
			}
			for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
				final F202 f202 = new F202(getErrSink(), this);
				f202.processLogs(stringCollectionEntry.getValue());
			}
		}
	}

	public IO getIO() {
		return io;
	}

	public CompilerInstructions realParseEzFile(final String f, final InputStream s, final File file) throws Exception {
		final String absolutePath = file.getCanonicalFile().toString();
		if (fn2ci.containsKey(absolutePath)) { // don't parse twice
			return fn2ci.get(absolutePath);
		}
		try {
			final CompilerInstructions R = parseEzFile_(f, s);
			R.setFilename(file.toString());
			fn2ci.put(absolutePath, R);
			s.close();
			return R;
		} catch (final ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	private OS_Module parseFile_(final String f, final InputStream s, final boolean do_out) throws RecognitionException, TokenStreamException {
		final ElijjahLexer lexer = new ElijjahLexer(s);
		lexer.setFilename(f);
		final ElijjahParser parser = new ElijjahParser(lexer);
		parser.out = new Out(f, this, do_out);
		parser.setFilename(f);
		parser.program();
		final OS_Module module = parser.out.module();
		parser.out = null;
		return module;
	}

	public void setIO(final IO io) {
		this.io = io;
	}

	boolean showTree = false;

	public List<ClassStatement> findClass(final String string) {
		final List<ClassStatement> l = new ArrayList<ClassStatement>();
		for (final OS_Module module : modules) {
			if (module.hasClass(string)) {
				l.add((ClassStatement) module.findClass(string));
			}
		}
		return l;
	}

	public int errorCount() {
		return eee.errorCount();
	}

	public OS_Module realParseElijjahFile(final String f, final @NotNull File file, final boolean do_out) throws Exception {
		final String absolutePath = file.getCanonicalFile().toString();
		if (fn2m.containsKey(absolutePath)) { // don't parse twice
			return fn2m.get(absolutePath);
		}
		final InputStream s = io.readFile(file);
		try {
			final OS_Module R = parseFile_(f, s, do_out);
			fn2m.put(absolutePath, R);
			s.close();
			return R;
		} catch (final ANTLRException e) {
			System.err.println(("parser exception: " + e));
			e.printStackTrace(System.err);
			s.close();
			return null;
		}
	}

	private CompilerInstructions parseEzFile_(final String f, final InputStream s) throws RecognitionException, TokenStreamException {
		final EzLexer lexer = new EzLexer(s);
		lexer.setFilename(f);
		final EzParser parser = new EzParser(lexer);
		parser.setFilename(f);
		parser.program();
		final @NotNull CompilerInstructions instructions = parser.ci;
		return instructions;
	}

	//
	// region MODULE STUFF
	//

	public void addModule(final OS_Module module, final String fn) {
		modules.add(module);
		fn2m.put(fn, module);
	}

    public OS_Module fileNameToModule(final String fileName) {
        if (fn2m.containsKey(fileName)) {
            return fn2m.get(fileName);
        }
        return null;
    }

	// endregion

    //
	// region CLASS AND FUNCTION CODES
	//

	public Operation2<OS_Module> findPrelude(final String prelude_name) {
		return use.findPrelude(prelude_name);
	}

	public void addFunctionMapHook(final FunctionMapHook aFunctionMapHook) {
		getDeducePhase().addFunctionMapHook(aFunctionMapHook);
	}

	public @NotNull DeducePhase getDeducePhase() {
		// TODO subscribeDeducePhase??
		return pipelineLogic.dp;
	}

	public int nextClassCode() {
		return _classCode++;
	}

	public int nextFunctionCode() {
		return _functionCode++;
	}

	// endregion

	//
	// region PACKAGES
	//

	public boolean isPackage(final String pkg) {
		return _packages.containsKey(pkg);
	}

	public OS_Package getPackage(final Qualident pkg_name) {
		return _packages.get(pkg_name.toString());
	}

	public OS_Package makePackage(final Qualident pkg_name) {
		if (!isPackage(pkg_name.toString())) {
			final OS_Package newPackage = new OS_Package(pkg_name, nextPackageCode());
			_packages.put(pkg_name.toString(), newPackage);
			return newPackage;
		} else
			return _packages.get(pkg_name.toString());
	}

	private int nextPackageCode() {
		return _packageCode++;
	}

	// endregion

	public int compilationNumber() {
		return _compilationNumber;
	}

	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

	public ErrSink getErrSink() {
		return eee;
	}

	public boolean getSilence() {
		return silent;
	}

	@NotNull
	public abstract EOT_OutputTree getOutputTree();

	public abstract @NotNull FluffyComp getFluffy();

	public @NotNull List<GeneratedNode> getLGC() {
		return getDeducePhase().generatedClasses.copy();
	}

	@Deprecated
	public int modules_size() {
		return modules.size();
	}

	@Deprecated
	public @NotNull List<OS_Module> getModules() {
		return modules;
	}

	public Pipeline getPipelines() {
		return pipelines;
	}

	static class CIS implements Observer<CompilerInstructions> {

		private final Subject<CompilerInstructions> compilerInstructionsSubject = ReplaySubject.<CompilerInstructions>create();
		CompilerInstructionsObserver _cio;

		@Override
		public void onSubscribe(@NonNull final Disposable d) {
			compilerInstructionsSubject.onSubscribe(d);
		}

		@Override
		public void onNext(@NonNull final CompilerInstructions aCompilerInstructions) {
			compilerInstructionsSubject.onNext(aCompilerInstructions);
		}

		@Override
		public void onError(@NonNull final Throwable e) {
			compilerInstructionsSubject.onError(e);
		}

		@Override
		public void onComplete() {
			throw new IllegalStateException();
			//compilerInstructionsSubject.onComplete();
		}

		public void almostComplete() {
			_cio.almostComplete();
		}

		public void subscribe(final Observer<CompilerInstructions> aCio) {
			compilerInstructionsSubject.subscribe(aCio);
		}
	}

	public ModuleBuilder moduleBuilder() {
		return new ModuleBuilder(this);
	}

//	public static class ModuleBuilder {
//		// private final Compilation compilation;
//		private final OS_Module mod;
//		private boolean _addToCompilation = false;
//		private String _fn = null;
//
//		public ModuleBuilder(@NotNull final Compilation aCompilation) {
////          compilation = aCompilation;
//			mod = new OS_Module();
//			mod.setParent(aCompilation);
//		}
//
//		public ModuleBuilder setContext() {
//			final ModuleContext mctx = new ModuleContext(mod);
//			mod.setContext(mctx);
//			return this;
//		}
//
//		public OS_Module build() {
//			if (_addToCompilation) {
//				if (_fn == null)
//					throw new IllegalStateException("Filename not set in ModuleBuilder");
//
//				mod.getCompilation().addModule(mod, _fn);
//			}
//			return mod;
//		}
//
//		public ModuleBuilder withPrelude(final String aPrelude) {
//			mod.prelude = mod.getCompilation().findPrelude("c");
//			return this;
//		}
//
//		public ModuleBuilder withFileName(final String aFn) {
//			_fn = aFn;
//			mod.setFileName(aFn);
//			return this;
//		}
//
//		public ModuleBuilder addToCompilation() {
//			_addToCompilation = true;
//			return this;
//		}
//	}

	static class USE {
		private static final FilenameFilter accept_source_files = (directory, file_name) -> {
			final boolean matches = Pattern.matches(".+\\.elijah$", file_name)
			  || Pattern.matches(".+\\.elijjah$", file_name);
			return matches;
		};
		private final Compilation c;
		private final ErrSink     errSink;
		private final Map<String, OS_Module> fn2m = new HashMap<String, OS_Module>();

		@Contract(pure = true)
		public USE(final Compilation aCompilation) {
			c       = aCompilation;
			errSink = c.getErrSink();
		}

		public void use(final @NotNull CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
			final File instruction_dir = new File(compilerInstructions.getFilename()).getParentFile();
			for (final LibraryStatementPart lsp : compilerInstructions.lsps) {
				final String dir_name = Helpers.remove_single_quotes_from_string(lsp.getDirName());
				File         dir;// = new File(dir_name);
				if (dir_name.equals(".."))
					dir = instruction_dir/*.getAbsoluteFile()*/.getParentFile();
				else
					dir = new File(instruction_dir, dir_name);
				use_internal(dir, do_out, lsp);
			}
			final LibraryStatementPart lsp = new LibraryStatementPart();
			lsp.setName(Helpers.makeToken("default")); // TODO: make sure this doesn't conflict
			lsp.setDirName(Helpers.makeToken(String.format("\"%s\"", instruction_dir)));
			lsp.setInstructions(compilerInstructions);
			use_internal(instruction_dir, do_out, lsp);
		}

		private void use_internal(final @NotNull File dir, final boolean do_out, LibraryStatementPart lsp) throws Exception {
			if (!dir.isDirectory()) {
				errSink.reportError("9997 Not a directory " + dir);
				return;
			}
			//
			final File[] files = dir.listFiles(accept_source_files);
			if (files != null) {
				for (final File file : files) {
					parseElijjahFile(file, file.toString(), do_out, lsp);
				}
			}
		}

		public Operation2<OS_Module> findPrelude(final String prelude_name) {
			final File local_prelude = new File("lib_elijjah/lib-" + prelude_name + "/Prelude.elijjah");

			if (!(local_prelude.exists())) {
				return Operation2.failure(new FileNotFoundDiagnostic(local_prelude));
			}

			try {
				final Operation2<OS_Module> om = realParseElijjahFile2(local_prelude.getName(), local_prelude, false);

				assert om.mode() == Mode.SUCCESS;

				return Operation2.success(om.success());
			} catch (final Exception e) {
				errSink.exception(e);
				return Operation2.failure(new ExceptionDiagnostic(e));
			}
		}

		private Operation2<OS_Module> parseElijjahFile(final @NotNull File f,
		                                               final @NotNull String file_name,
		                                               final boolean do_out,
		                                               final @NotNull LibraryStatementPart lsp) {
			System.out.printf("   %s%n", f.getAbsolutePath());

			if (f.exists()) {
				final Operation2<OS_Module> om = realParseElijjahFile2(file_name, f, do_out);

				if (om.mode() == Mode.SUCCESS) {
					// TODO we dont know which prelude to find yet
					final Operation2<OS_Module> pl = findPrelude(CompilationAlways.defaultPrelude());

					// NOTE Go. infectious. tedious. also slightly lazy
					assert pl.mode() == Mode.SUCCESS;

					final OS_Module mm = om.success();

					assert mm.getLsp() == null;
					assert mm.prelude == null;

					mm.setLsp(lsp);
					mm.prelude = pl.success();

					return Operation2.success(mm);
				} else {
					final Diagnostic e = new UnknownExceptionDiagnostic(om);
					return Operation2.failure(e);
				}
			} else {
				final Diagnostic e = new FileNotFoundDiagnostic(f);

				return Operation2.failure(e);
			}
		}

		public Operation2<OS_Module> realParseElijjahFile2(final String f, final @NotNull File file, final boolean do_out) {
			final Operation<OS_Module> om;

			try {
				om = realParseElijjahFile(f, file, do_out);
			} catch (Exception aE) {
				return Operation2.failure(new ExceptionDiagnostic(aE));
			}

			switch (om.mode()) {
				case SUCCESS:
					return Operation2.success(om.success());
				case FAILURE:
					final Exception e = om.failure();
					errSink.exception(e);
					return Operation2.failure(new ExceptionDiagnostic(e));
				default:
					throw new IllegalStateException("Unexpected value: " + om.mode());
			}
		}

		private Operation<OS_Module> parseFile_(final String f, final InputStream s, final boolean do_out) throws RecognitionException, TokenStreamException {
			final QuerySourceFileToModuleParams qp = new QuerySourceFileToModuleParams(s, f, do_out);
			return new QuerySourceFileToModule(qp, c).calculate();
		}

		public Operation<OS_Module> realParseElijjahFile(final String f, final @NotNull File file, final boolean do_out) throws Exception {
			final String absolutePath = file.getCanonicalFile().toString();
			if (fn2m.containsKey(absolutePath)) { // don't parse twice
				final OS_Module m = fn2m.get(absolutePath);
				return Operation.success(m);
			}

			final IO io = c.getIO();

			// tree add something

			final InputStream s = io.readFile(file);
			try {
				final Operation<OS_Module> om = parseFile_(f, s, do_out);
				if (om.mode() != Mode.SUCCESS) {
					final Exception e = om.failure();
					assert e != null;

					System.err.println(("parser exception: " + e));
					e.printStackTrace(System.err);
					s.close();
					return Operation.failure(e);
				}
				final OS_Module R = om.success();
				fn2m.put(absolutePath, R);
				s.close();
				return Operation.success(R);
			} catch (final ANTLRException e) {
				System.err.println(("parser exception: " + e));
				e.printStackTrace(System.err);
				s.close();
				return Operation.failure(e);
			}
		}

		public void addModule(final OS_Module aModule, final String aFn) {
			fn2m.put(aFn, aModule);
		}
	}

	public static class CompilationAlways {
		@NotNull
		public static String defaultPrelude() {
			return "c";
		}
	}
}

//
//
//
