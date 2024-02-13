package tripleo.elijah_durable_pancake.comp.impl;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.Eventual;
import tripleo.elijah.comp.CentralController;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah.util.EventualExtract;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

public class CentralControllerImpl implements CentralController {
	private final Eventual<GeneratePhase> _p_GeneratePhase = new Eventual<>();
	private final Eventual<PipelineLogic> _p_PipelineLogic = new Eventual<>();

	@Override
	public boolean hasGeneratePhase() {
		return _p_GeneratePhase.isResoved();
	}
	@Override
	public boolean hasPipelineLogic() {
		return _p_PipelineLogic.isResoved();
	}

	@Override
	public GeneratePhase getGeneratePhase() {
		return EventualExtract.of(_p_GeneratePhase);
	}
	@Override
	public PipelineLogic getPipelineLogic() {
		return EventualExtract.of(_p_PipelineLogic);
	}

	@Override
	public void provideGeneratePhase(GeneratePhase p) {
		_p_GeneratePhase.resolve(p);
	}
	@Override
	public void providePipelineLogic(PipelineLogic p) {
		_p_PipelineLogic.resolve(p);
	}

	@Override
	public void waitPipelineLogic(final DoneCallback<PipelineLogic> cb) {
		_p_PipelineLogic.then(cb);
	}
}
