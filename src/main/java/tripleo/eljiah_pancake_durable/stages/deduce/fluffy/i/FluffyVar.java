package tripleo.eljiah_pancake_durable.stages.deduce.fluffy.i;

import tripleo.elijah.diagnostic.Locatable;
import tripleo.eljiah_pancake_durable.nextgen.composable.IComposable;

public interface FluffyVar {
	String name();

	Locatable nameLocatable();

	IComposable nameComposable();

	FluffyVarTarget target();
}
