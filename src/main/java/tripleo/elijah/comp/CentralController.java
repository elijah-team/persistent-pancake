package tripleo.elijah.comp;

import tripleo.elijah.Eventual;
import tripleo.elijah.stages.gen_fn.GeneratePhase;

public class CentralController {
	private final Eventual<GeneratePhase> _p_GeneratePhase = new Eventual<>();

	public boolean hasGeneratePhase() {
		return _p_GeneratePhase.isResoved();
	}
}
