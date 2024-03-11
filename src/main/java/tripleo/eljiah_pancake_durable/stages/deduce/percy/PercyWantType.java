package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;

public interface PercyWantType {
	void onFinalSuccess(GenType gt11);

	void provide(PercyMakeType aMt);
}
