/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.*;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.functionality.f202.F202;
import tripleo.elijah.comp.i.LCM_CompilerAccess;
import tripleo.elijah.comp.impl.LCM_Event_RootCI;
import tripleo.elijah.comp.queries.QueryEzFileToModule;
import tripleo.elijah.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah_pancake.sep1011.modeltransition.ElSystemSink;
import tripleo.elijah.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.util.Operation2;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.deduce.fluffy.impl.FluffyCompImpl;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.stages.generate.OutputStrategyC;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah.ut.UT_Controller;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.Operation;
import tripleo.elijah.world.i.LivingRepo;
import tripleo.elijah.world.impl.DefaultLivingRepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CompilationImpl implements Compilation, ElSystemSink {

	public final  List<ElLog>          elLogs = new LinkedList<ElLog>();
	public final  CompilationConfig    cfg    = new CompilationConfig();
	public final  CIS                  _cis   = new CIS();
	private final Pipeline             pipelines;
	private final int                  _compilationNumber;
	private final ErrSink              errSink;
	//
	public final  DefaultLivingRepo    _repo  = new DefaultLivingRepo();
	//
	final         MOD                  mod    = new MOD(this);
	private final IO                   io;
	private final USE                  use    = new USE(this);
	//
	//
	//
	public        PipelineLogic        pipelineLogic;
	public        CompilationRunner    __cr;
	private       CompilerInstructions rootCI;
	private       ElSystem             saveSystem;

	private final LCM lcm;

	@Override
	public void hasInstructions(final @NotNull List<CompilerInstructions> cis) {
		assert cis.size() > 0;

		// don't inline yet b/c getProjectName
		rootCI = cis.get(0);

		lcm.asv(rootCI, LCM_Event_RootCI.instance());
	}

	@Override
	public void feedCmdLine(final @NotNull List<String> args) {
		feedCmdLine(args, new DefaultCompilerController());
	}

	@Override
	public String getProjectName() {
		return rootCI.getName();
	}

	@Override
	public OS_Module realParseElijjahFile(final String f, final @NotNull File file, final boolean do_out) throws Exception {
		return use.realParseElijjahFile(f, file).success();
	}

	@Override
	public Operation<CompilerInstructions> parseEzFile(final @NotNull File aFile) {
		try {
			final QueryEzFileToModuleParams       params = new QueryEzFileToModuleParams(aFile.getAbsolutePath(), io.readFile(aFile));
			final Operation<CompilerInstructions> x      = new QueryEzFileToModule(params).calculate();
			return x;
		} catch (final FileNotFoundException aE) {
			return Operation.failure(aE);
		}
	}

	@Override
	public void pushItem(final CompilerInstructions aci) {
		_cis.onNext(aci);
	}

	//
	//
	//

	@Override
	public List<ClassStatement> findClass(final String string) {
		final List<ClassStatement> l = new ArrayList<ClassStatement>();
		for (final OS_Module module : mod.modules) {
			if (module.hasClass(string)) {
				l.add((ClassStatement) module.findClass(string));
			}
		}
		return l;
	}

	@Override
	public void use(final @NotNull CompilerInstructions compilerInstructions, final boolean do_out) throws Exception {
		use.use(compilerInstructions);    // NOTE Rust
	}

	@Override
	public int errorCount() {
		return errSink.errorCount();
	}

	@Override
	public void writeLogs(final boolean aSilent, final @NotNull List<ElLog> aLogs) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		if (true) {
			for (final ElLog deduceLog : aLogs) {
				logMap.put(deduceLog.getFileName(), deduceLog);
			}
			for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
				final F202 f202 = new F202(getErrSink(), this);
				f202.processLogs(stringCollectionEntry.getValue());
			}
		}
	}

	@Override
	public IO getIO() {
		return io;
	}

//	public void setIO(final IO io) {
//		this.io = io;
//	}

	@Override
	public void addModule(final OS_Module module, final String fn) {
		mod.addModule(module, fn);
	}

	@Override
	public OS_Module fileNameToModule(final String fileName) {
		if (mod.fn2m.containsKey(fileName)) {
			return mod.fn2m.get(fileName);
		}
		return null;
	}

	//
	// region MODULE STUFF
	//

	@Override
	public ErrSink getErrSink() {
		return errSink;
	}

	@Override
	public boolean getSilence() {
		return cfg.silent;
	}

	// endregion

	//
	// region CLASS AND FUNCTION CODES
	//

	@Override
	public Operation2<OS_Module> findPrelude(final String prelude_name) {
		return use.findPrelude(prelude_name);
	}

	@Override
	public void addFunctionMapHook(final IFunctionMapHook aFunctionMapHook) {
		getDeducePhase().addFunctionMapHook(aFunctionMapHook);
	}

	@Override
	public @NotNull DeducePhase getDeducePhase() {
		// TODO subscribeDeducePhase??
		return pipelineLogic.dp;
	}

	@Override
	public int nextClassCode() {
		return _repo.nextClassCode();
	}

	@Override
	public int nextFunctionCode() {
		return _repo.nextFunctionCode();
	}

	// endregion

	//
	// region PACKAGES
	//

	@Override
	public OS_Package getPackage(final Qualident pkg_name) {
		return _repo.getPackage(pkg_name.toString());
	}

	@Override
	public OS_Package makePackage(final Qualident pkg_name) {
		return _repo.makePackage(pkg_name);
	}

	// endregion

	@Override
	public int compilationNumber() {
		return _compilationNumber;
	}

	@Override
	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

	@Override
	@Deprecated
	public int modules_size() {
		return mod.size();
	}

	@Override
	public @NotNull List<GeneratedNode> getLGC() {
		return getDeducePhase().generatedClasses.copy();
	}

	@Override
	public boolean isPackage(final String aPackageName) {
		return _repo.isPackage(aPackageName);
	}

	@Override
	public void feedCmdLine(final List<String> args, final CompilerController ctl) {
		if (args.size() == 0) {
			ctl.printUsage();
			return; // ab
		}

		if (ctl instanceof DefaultCompilerController) {
			final DefaultCompilerController dctl = (DefaultCompilerController) ctl;
			dctl._set(this, args);
		} else if (ctl instanceof UT_Controller) {
			final UT_Controller uctl = (UT_Controller) ctl;
			uctl._set(this, args);
		}

		ctl.processOptions();
		ctl.runner();
	}

	@Override
	public void addCodeOutput(final EOT_FileNameProvider aFileNameProvider, final Supplier<EOT_OutputFile> aOutputFileSupplier, final boolean addFlag) {
		final EOT_OutputFile eof = aOutputFileSupplier.get();
		final Finally.Output e   = reports().addCodeOutput(aFileNameProvider, eof);
		if (addFlag) {
			getOutputTree().add(eof);
		}
	}

	@Override
	public void provide(final ElSystem aSystem) {
		saveSystem = aSystem;

		aSystem.resultInto(this);
	}

	@Override
	public int getOutputTreeSize() {
		return getOutputTree().getList().size();
	}

	@Override
	public Pipeline getPipelines() {
		return pipelines;
	}

	@Override
	public ModuleBuilder moduleBuilder() {
		return new ModuleBuilder(this);
	}

	private final Finally _finally = new Finally();

	@Override
	public Finally reports() {
		return _finally;
	}

	@Override
	public void writeLogs() {
		writeLogs(cfg.silent, elLogs);
	}

	@Override
	public CompilationConfig _cfg() {
		return cfg;
	}

	@Override
	public ElLog.Verbosity testSilence() {
		final boolean x = getSilence();
		return x ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	@Override
	public Stream<OS_Module> modulesStream() {
		return mod.modules.stream();
	}

	@Override
	public void acceptElLog(final ElLog aLog) {
		elLogs.add(aLog);
	}

	public CompilationImpl(final ErrSink aErrSink, final IO aIo) {
		errSink            = aErrSink;
		io                 = aIo;
		_compilationNumber = new Random().nextInt(Integer.MAX_VALUE);
		pipelines          = new Pipeline(aErrSink);
		_fluffyComp        = new FluffyCompImpl(this);
		lcm                = new LCM(this);
	}

	public void testMapHooks(final List<IFunctionMapHook> aMapHooks) {
		throw new NotImplementedException();
	}

	private @Nullable EOT_OutputTree _output_tree = null;

	@Override
	public @NotNull EOT_OutputTree getOutputTree() {
		if (_output_tree == null) {
			_output_tree = new EOT_OutputTree();
		}

		return _output_tree;
	}

	@Override
	public @NotNull FluffyComp getFluffy() {
		return _fluffyComp;
	}

	private final @NotNull FluffyCompImpl _fluffyComp;

	public ICompilationAccess _access() {
		return new DefaultCompilationAccess(this);
	}

	@Override
	public OutputStrategyC strategy(final OutputStrategy aOutputStrategy) {
		return new OutputStrategyC(aOutputStrategy);
	}

	@Override
	public void addGenerateResultItem(final GenerateResultItem aGenerateResultItem, final Supplier<EOT_FileNameProvider> aFileNameProviderSupplier) {
		getOutputTree().addGenerateResultItem(aGenerateResultItem, aFileNameProviderSupplier);
	}

	public static class MOD {
		public final List<OS_Module>        modules = new ArrayList<OS_Module>();
		public final Map<String, OS_Module> fn2m    = new HashMap<String, OS_Module>();
//		private final Compilation            c;

		public MOD(final Compilation aCompilation) {
//			c = aCompilation;
		}

		public void addModule(final OS_Module module, final String fn) {
			modules.add(module);
			fn2m.put(fn, module);
		}

		public int size() {
			return modules.size();
		}

		public List<OS_Module> modules() {
			return modules;
		}
	}

	//
	//
	//
	public static class CompilationConfig {
		public Stages  stage    = Stages.O; // Output
		public boolean silent   = false;
		public boolean showTree = false;
	}

	public static class CIS implements Observer<CompilerInstructions> {

		private final Subject<CompilerInstructions> compilerInstructionsSubject = ReplaySubject.create();
		private       CompilerInstructionsObserver  _cio;

		@Override
		public void onSubscribe(@NonNull final Disposable d) {
			getCompilerInstructionsSubject().onSubscribe(d);
		}

		@Override
		public void onNext(@NonNull final CompilerInstructions aCompilerInstructions) {
			getCompilerInstructionsSubject().onNext(aCompilerInstructions);
		}

		@Override
		public void onError(@NonNull final Throwable e) {
			getCompilerInstructionsSubject().onError(e);
		}

		@Override
		public void onComplete() {
			throw new IllegalStateException();
			//compilerInstructionsSubject.onComplete();
		}

		public void almostComplete() {
			get_cio().almostComplete();
		}

		public void subscribe(final Observer<CompilerInstructions> aCio) {
			getCompilerInstructionsSubject().subscribe(aCio);
		}

		public Subject<CompilerInstructions> getCompilerInstructionsSubject() {
			return compilerInstructionsSubject;
		}

		public CompilerInstructionsObserver get_cio() {
			return _cio;
		}

		public void set_cio(CompilerInstructionsObserver a_cio) {
			_cio = a_cio;
		}
	}

	@Override
	public List<ElLog> _elLogs() {
		return elLogs;
	}

	@Override
	public CIS get_cis() {
		return _cis;
	}

	@Override
	public void setRunner(final CompilationRunner aCompilationRunner) {
		__cr = aCompilationRunner;
	}

	@Override
	public CompilationRunner getRunner() {
		return __cr;
	}

	@Override
	public LivingRepo _repo() {
		return _repo;
	}

	@Override
	public LCM_CompilerAccess getLCMAccess() {
		final var _c = this;
		return new LCM_CompilerAccess() {
			@Override
			public Compilation c() {
				return _c;
			}

			@Override
			public CompilationRunner cr() {
				return c().getRunner();
			}

			@Override
			public CompilationConfig cfg() {
				return c()._cfg();
			}
		};
	}

}

//
//
//
