package tripleo.eljiah_pancake_durable.testing.comp;

import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedFunction;

import java.util.Collection;

public interface IFunctionMapHook {
	boolean matches(FunctionDef aFunctionDef);

	void apply(Collection<GeneratedFunction> aGeneratedFunctions);
}
