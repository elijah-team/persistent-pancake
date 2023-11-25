package tripleo.elijah.comp.impl2;

import tripleo.elijah.Eventual;
import tripleo.elijah.comp.PipelineLogic;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah.util.EventualExtract;

public class CentralController {
	private final Eventual<GeneratePhase> _p_GeneratePhase = new Eventual<>();
	private final Eventual<PipelineLogic> _p_PipelineLogic = new Eventual<>();

	public boolean hasGeneratePhase() {
		return _p_GeneratePhase.isResoved();
	}

	public boolean hasPipelineLogic() {
		return _p_PipelineLogic.isResoved();
	}

	public GeneratePhase getGeneratePhase() {
		return EventualExtract.of(_p_GeneratePhase);
	}

	public PipelineLogic getPipelineLogic() {
		return EventualExtract.of(_p_PipelineLogic);
	}
	public void provideGeneratePhase(GeneratePhase p) {
		_p_GeneratePhase.resolve(p);
	}

	public void provideipelineLogic(PipelineLogic p) {
		_p_PipelineLogic.resolve(p);
	}
}
