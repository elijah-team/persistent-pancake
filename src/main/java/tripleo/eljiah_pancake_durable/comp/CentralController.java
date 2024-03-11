package tripleo.eljiah_pancake_durable.comp;

import org.jdeferred2.DoneCallback;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratePhase;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

// aka AccessBus...
// TODO waitKey/want??
//  see Compilation#provide
public interface CentralController {
	boolean hasGeneratePhase();
	boolean hasPipelineLogic();

	GeneratePhase getGeneratePhase();
	PipelineLogic getPipelineLogic();

	void provideGeneratePhase(GeneratePhase p);
	void providePipelineLogic(PipelineLogic p);
	void provideDeducePhase(DeducePhase p);

	void waitPipelineLogic(DoneCallback<PipelineLogic> cb);
	void waitDeducePhase(DoneCallback<DeducePhase> cb);
	void waitGeneratePhase(DoneCallback<GeneratePhase> cb);
}
