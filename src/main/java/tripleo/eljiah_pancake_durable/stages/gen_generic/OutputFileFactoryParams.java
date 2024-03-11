package tripleo.eljiah_pancake_durable.stages.gen_generic;

import org.jetbrains.annotations.Contract;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

public class OutputFileFactoryParams {
	private final OS_Module       mod;
	private final ErrSink         errSink;
	private final ElLog.Verbosity verbosity;
	private final PipelineLogic   pipelineLogic;

	@Contract(pure = true)
	public OutputFileFactoryParams(final OS_Module aMod,
	                               final ErrSink aErrSink,
	                               final ElLog.Verbosity aVerbosity,
	                               final PipelineLogic aPipelineLogic) {
		mod           = aMod;
		errSink       = aErrSink;
		verbosity     = aVerbosity;
		pipelineLogic = aPipelineLogic;
	}

	public OS_Module getMod() {
		return mod;
	}

	public String getModFileName() {
		return mod.getFileName();
	}

	public ErrSink getErrSink() {
		return errSink;
	}

	public ElLog.Verbosity getVerbosity() {
		return verbosity;
	}

	public PipelineLogic getPipelineLogic() {
		return pipelineLogic;
	}

	public void addLog(final ElLog aLOG) {
		getPipelineLogic().addLog(aLOG);
	}
}