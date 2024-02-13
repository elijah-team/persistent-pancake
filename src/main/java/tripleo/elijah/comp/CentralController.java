package tripleo.elijah.comp;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

// aka AccessBus...
public interface CentralController {
	boolean hasGeneratePhase();

	boolean hasPipelineLogic();

	GeneratePhase getGeneratePhase();

	PipelineLogic getPipelineLogic();

	void provideGeneratePhase(GeneratePhase p);

	void providePipelineLogic(PipelineLogic p);

	void waitPipelineLogic(DoneCallback<PipelineLogic> cb);
}
