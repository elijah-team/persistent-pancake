package tripleo.elijah.test_help;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ICompilationAccess;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.comp.ProcessRecord;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.comp.internal.CompilationImpl;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.gen_generic.GenerateFiles;
import tripleo.elijah.stages.gen_generic.OutputFileFactory;
import tripleo.elijah.stages.gen_generic.OutputFileFactoryParams;

public class Boilerplate {
	public Compilation        comp;
	public ICompilationAccess aca;
	public ProcessRecord      pr;
	public PipelineLogic      pipelineLogic;
	public GenerateFiles      generateFiles;
	OS_Module module;

	public void get() {
		comp = new CompilationImpl(new StdErrSink(), new IO());
		aca  = ((CompilationImpl) comp)._access();
		pr   = new ProcessRecord(aca);

//		final RuntimeProcesses rt = StageToRuntime.get(ca.getStage(), ca, pr);

		pipelineLogic = pr.pipelineLogic;
		//getGenerateFiles(mod);

		if (module != null) {
			module.setParent(comp);
		}
	}

	public void getGenerateFiles(final @NotNull OS_Module mod) {
		generateFiles = OutputFileFactory.create(Compilation.CompilationAlways.defaultPrelude(),
		  new OutputFileFactoryParams(mod,
			comp.getErrSink(),
			aca.testSilence(),
			pipelineLogic));
	}

	public OS_Module defaultMod() {
		if (module == null) {
			module = new OS_Module();
			if (comp != null)
				module.setParent(comp);
		}

		return module;
	}

	public DeducePhase getDeducePhase() {
		return pr.pipelineLogic.dp;
	}
}
