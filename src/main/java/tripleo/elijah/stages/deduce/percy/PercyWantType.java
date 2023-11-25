package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.stages.gen_fn.GenType;

public interface PercyWantType {
	void onFinalSuccess(GenType gt11);

	void provide(PercyMakeType aMt);
}
