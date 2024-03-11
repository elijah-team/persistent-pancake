/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah_durable_pancake.comp.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.UnintendedUseException;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.CentralController;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.CompilerController;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.comp.Finally;
import tripleo.eljiah_pancake_durable.comp.IO;
import tripleo.eljiah_pancake_durable.comp.i.ICompilationAccess;
import tripleo.eljiah_pancake_durable.comp.i.LCM_CompilerAccess;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.OS_Package;
import tripleo.eljiah_pancake_durable.lang.Qualident;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputFile;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputTree;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.deduce.fluffy.i.FluffyComp;
import tripleo.eljiah_pancake_durable.stages.deduce.fluffy.impl.FluffyCompImpl;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedNode;
import tripleo.eljiah_pancake_durable.stages.gen_generic.GenerateResultItem;
import tripleo.eljiah_pancake_durable.stages.generate.ElSystem;
import tripleo.eljiah_pancake_durable.stages.generate.OutputStrategy;
import tripleo.eljiah_pancake_durable.stages.generate.OutputStrategyC;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.testing.comp.IFunctionMapHook;
import tripleo.elijah.ut.UT_Controller;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.Operation;
import tripleo.elijah.util.Operation2;
import tripleo.eljiah_pancake_durable.world.i.LivingRepo;
import tripleo.eljiah_pancake_durable.world.impl.DefaultLivingRepo;
import tripleo.elijah_durable_pancake.comp.Compilation0101;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_durable_pancake.comp.CompilerInstructionsObserver;
import tripleo.elijah_durable_pancake.comp.ModuleBuilder;
import tripleo.elijah_durable_pancake.comp.Pipeline;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.elijah_durable_pancake.comp.functionality.f202.F202;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CentralController;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CompilationAccess;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CompilerController;
import tripleo.elijah_durable_pancake.comp.impl.LCM_Event_RootCI;
import tripleo.elijah_durable_pancake.comp.queries.QueryEzFileToModule;
import tripleo.elijah_durable_pancake.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah_durable_pancake.input.EDP_MOD;
import tripleo.elijah_durable_pancake.input.EDP_USE;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah_pancake.feb24.comp.CompilationSignalTarget;
import tripleo.elijah_pancake.feb24.comp.Providing;
import tripleo.elijah_pancake.sep1011.modeltransition.ElSystemSink;
import tripleo.elijah_prepan.compilation_runner.CK_Monitor;
import tripleo.elijah_prepan.compilation_runner.CR_CK_Monitor;
import tripleo.elijah_prepan.compilation_runner.RuntimeProcesses;
import tripleo.elijah_prepan.compilation_runner.Stages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EDP_Compilation implements Compilation0101, ElSystemSink, Compilation {

	public final  List<ElLog>          elLogs = new LinkedList<ElLog>();
	public final  CompilationConfig    cfg    = new CompilationConfig();
	public final  CIS                  _cis   = new CIS();
	private final Pipeline             pipelines;
	private final int                  _compilationNumber;
	private final ErrSink              errSink;
	//
	public final  DefaultLivingRepo _repo = new DefaultLivingRepo();
	//
	final         EDP_MOD           mod   = new EDP_MOD(this);
	private final IO                io;
	private final EDP_USE           use = new EDP_USE(this);
	private       CentralController central;
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
		assert !cis.isEmpty();

		// don't inline yet b/c getProjectName
		rootCI = cis.get(0);

		lcm.asv(rootCI, LCM_Event_RootCI.instance());
	}

	public void feedCmdLine(final @NotNull List<String> args) {
		feedCmdLine(args, new EDP_CompilerController());
	}

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

	public ErrSink getErrSink() {
		return errSink;
	}

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

	public int compilationNumber() {
		return _compilationNumber;
	}

	public String getCompilationNumberString() {
		return String.format("%08x", _compilationNumber);
	}

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
		if (args.isEmpty()) {
			ctl.printUsage();
			return; // ab
		}

		if (ctl instanceof EDP_CompilerController) {
			final EDP_CompilerController dctl = (EDP_CompilerController) ctl;
			dctl._set(this, args);
		} else if (ctl instanceof UT_Controller) {
			final UT_Controller uctl = (UT_Controller) ctl;
			uctl._set(this, args);
		}

		ctl.processOptions();
		ctl.runner();
	}

	public void addCodeOutput(final EOT_FileNameProvider aFileNameProvider, final Supplier<EOT_OutputFile> aOutputFileSupplier, final boolean addFlag) {
		final EOT_OutputFile eof = aOutputFileSupplier.get();
		final Finally.Output e   = reports().addCodeOutput(aFileNameProvider, eof);
		if (addFlag) {
			getOutputTree().add(eof);
		}
	}

	public void provide(final ElSystem aSystem) {
		saveSystem = aSystem;

		aSystem.resultInto(this);
	}

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

	public Finally reports() {
		return _finally;
	}

	public void writeLogs() {
		writeLogs(cfg.silent, elLogs);
	}

	public CompilationConfig _cfg() {
		return cfg;
	}

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

	public EDP_Compilation(final ErrSink aErrSink, final IO aIo) {
		errSink            = aErrSink;
		io                 = aIo;
		_compilationNumber = new Random().nextInt(Integer.MAX_VALUE);
		pipelines          = new Pipeline(aErrSink);
		_fluffyComp        = new FluffyCompImpl(this);
		lcm                = new LCM(this);

		P.starting(null);
	}

	public void testMapHooks(final List<IFunctionMapHook> aMapHooks) {
		throw new NotImplementedException();
	}

	private @Nullable EOT_OutputTree _output_tree = null;

	public @NotNull EOT_OutputTree getOutputTree() {
		if (_output_tree == null) {
			_output_tree = new EOT_OutputTree();
		}

		return _output_tree;
	}

	public @NotNull FluffyComp getFluffy() {
		return _fluffyComp;
	}

	private final @NotNull FluffyCompImpl _fluffyComp;

	public ICompilationAccess _access() {
		return new EDP_CompilationAccess(this);
	}

	@Override
	public OutputStrategyC strategy(final OutputStrategy aOutputStrategy) {
		return new OutputStrategyC(aOutputStrategy);
	}

	@Override
	public void addGenerateResultItem(final GenerateResultItem aGenerateResultItem, final Supplier<EOT_FileNameProvider> aFileNameProviderSupplier) {
		getOutputTree().addGenerateResultItem(aGenerateResultItem, aFileNameProviderSupplier);
	}

	@Override public CentralController central() {
		//JavaCompiler.CompilationTask
		if (this.central == null)
			this.central = new EDP_CentralController();
		return this.central;
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

		public void set_cio(final CompilerInstructionsObserver a_cio) {
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

	public LivingRepo _repo() {
		return _repo;
	}

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

	// everything below here in a mixin

	private final Map<Class<?>, Object> provided = new HashMap<>();

	public <T> void provide(final Class<T> aClass, final Function<Providing, T> cb, final Class<?>[] aClasses) {
		if (provided.containsKey(aClass)) {
			final var o = provided.get(aClass);
			final var m = new HashMap<>();
			m.put(aClass, o);
			final var p = new Providing() {
				@Override
				public Object get(final Class<?> c) {
					return m.get(c);
				}
			};
			final var o2 = cb.apply(p);
			System.err.println("9997-573 " + o2.getClass().getName());
			return;
		}

		if (Objects.equals(aClass, RuntimeProcesses.class)) {
			final int y = 2;
			{
				final var m = new HashMap<>();
				for (final Class<?> aClass1 : aClasses) {
					System.err.println("9997-581 " + aClass1.getName());
					for (final Map.Entry<Class<?>, Object> classObjectEntry : provided.entrySet()) {
						if (Objects.equals(aClass1, classObjectEntry.getKey())) {
							m.put(aClass1, classObjectEntry.getValue());
						}
					}
				}
				final var p = new Providing() {
					@Override
					public Object get(final Class<?> c) {
						return m.get(c);
					}
				};
				final T o = cb.apply(p);
				provided.put(aClass, o);
				System.err.println("9997-573 Called Provider for " + o.getClass().getName());
			}
		} else if (aClasses.length == 0) {
			if (provided.containsKey(aClass)) {
				System.err.println("9997-597 Already " + aClass.getName());
			} else {
				final Providing p = c -> {
					throw new UnintendedUseException("why are you calling this?");//m.get(c);
				};
				final var o = cb.apply(p);
				provided.put(aClass, o);
				System.err.println("9997-607 Record single " + o.getClass().getName());
			}
		} else {
			throw new UnintendedUseException("implement signals");
		}
	}

	public void waitProviders(final Class[] aClasses, final Consumer<Providing> cb) {
		for (final Class<?> aClass : aClasses) {
			if (!(provided.containsKey(aClass))) throw new UnintendedUseException("lazy (Providing not ready)");
		}

		final Providing p = c -> {
			if (provided.containsKey(c))
				return provided.get(c);
			throw new UnintendedUseException("lazy, asked what i don't know");
		};

		cb.accept(p);
	}

	public void trigger(final Class<?> aSignalClass) {
//		throw new UnintendedUseException("implement signals");
		if (!(triggers.containsKey(aSignalClass))) {
			//triggers.put(aSignalClass, aSignalTarget);
			// README nice intention when you use getClass here
			System.err.println("9997-597 Dead trigger for " + aSignalClass.getName());
		} else {
//			triggers.get(aSignalClass);
			final Collection<CompilationSignalTarget> trs = triggers.get(aSignalClass);
			for (final CompilationSignalTarget target : trs) {
				// TODO run only once...
				target.run();
			}
		}
	}

	public void onTrigger(final Class<?> aSignalClass, final CompilationSignalTarget aSignalTarget) {
//		if (Objects.equals(aSignalClass, CS_RunBetter.class)) {
//			_CS_RunBetter();
//		} else {
//			throw new UnintendedUseException("un-implemented signal");
//		}

		if (!(triggers.containsEntry(aSignalClass, aSignalTarget))) {
			triggers.put(aSignalClass, aSignalTarget);
		} else {
			final Collection<CompilationSignalTarget> trs = triggers.get(aSignalClass);
			for (final CompilationSignalTarget target : trs) {
				// TODO run only once...
				target.run();
			}
		}
	}

	private final Multimap<Class<?>, CompilationSignalTarget> triggers = ArrayListMultimap.create();

	public void _CS_RunBetter() {
		final CR_State st = null;
		this.waitProviders(
		  new Class[]{RuntimeProcesses.class, CompilationRunner.class}
		  , (o) -> {
			  final CompilationRunner compilationRunner = (CompilationRunner) o.get(CompilationRunner.class);
			  final CR_CK_Monitor     monitor           = new CR_CK_Monitor(compilationRunner);
			  final RuntimeProcesses  rt                = (RuntimeProcesses) o.get(RuntimeProcesses.class);

			  if (st != null)
				  st.provide(rt);

			  try {
				  rt.run_better();
			  } catch (final Exception aE) {
				  monitor.reportFailure(CK_Monitor.PLACEHOLDER_CODE_2, "" + aE);
			  }
		  }
		);
	}
}

//
//
//
