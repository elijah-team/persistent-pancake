package tripleo.elijah.test_help;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationAlways;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_generic.GenerateFiles;
import tripleo.elijah.stages.gen_generic.OutputFileFactory;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.CompilationImpl;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;

import java.util.function.Consumer;

public class Boilerplate {
	public Compilation        comp;
	public ICompilationAccess aca;
	public ProcessRecord      pr;
	public PipelineLogic      pipelineLogic;
	public GenerateFiles      generateFiles;
	OS_Module module;

	public void get() {
		comp = new CompilationImpl(new EDP_ErrSink(), new EDP_IO());
		aca  = ((CompilationImpl) comp)._access();
		pr   = new ProcessRecord(aca);

//		final RuntimeProcesses rt = StageToRuntime.get(ca.getStage(), ca, pr);

		pr.getAccessBus().getCompilation().central().waitPipelineLogic(pl -> pipelineLogic = pl);

		//getGenerateFiles(mod);

		if (module != null) {
			module.setParent(comp);
		}
	}

	public void getGenerateFiles(final @NotNull OS_Module mod) {
		generateFiles = OutputFileFactory.create(CompilationAlways.defaultPrelude(),
		  new OutputFileFactoryParams(mod,
			comp.getErrSink(),
			aca.testSilence(),
			pipelineLogic));
	}

	public OS_Module defaultMod() {
		if (module == null) {
			module = new OS_Module();
			module.setContext(new ModuleContext(module));
			if (comp != null)
				module.setParent(comp);
		}

		return module;
	}

	public BoilerplateModuleBuilder withModBuilder(final OS_Module aMod) {
		return new BoilerplateModuleBuilder(aMod);
	}

	public void simpleDeduceModule3(final OS_Module aMod, Consumer<DeduceTypes2> cb) {
		final @NotNull ElLog.Verbosity verbosity = CompilationAlways.gitlabCIVerbosity();
		final @NotNull String          s         = CompilationAlways.defaultPrelude();
		simpleDeduceModule2(aMod, s, verbosity, cb);
	}

	public void simpleDeduceModule2(final OS_Module mod,
	                                final @NotNull String aS,
	                                final ElLog.Verbosity aVerbosity,
	                                final Consumer<DeduceTypes2> cb) {
		final Compilation c = mod.getCompilation();

		mod.prelude = c.findPrelude(aS).success();

		simpleDeduceModule(aVerbosity, cb);
	}

	public void simpleDeduceModule(final ElLog.Verbosity verbosity, Consumer<DeduceTypes2> cons) {
		pr.getAccessBus().getCompilation().central().waitDeducePhase(dp -> {
			final DeduceTypes2 d = dp.deduceModule(module, verbosity);

//	    	d.processWachers();

			cons.accept(d);
		});
	}
}
