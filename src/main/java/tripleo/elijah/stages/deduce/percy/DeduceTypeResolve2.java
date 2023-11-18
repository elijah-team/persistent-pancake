package tripleo.elijah.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.util.NotImplementedException;

public class DeduceTypeResolve2 {
	public void makeGenType(final @NotNull PercyWantType aPercyWantType, final PercyMakeType aMt) {
		aPercyWantType.provide(aMt);
	}

	public ClassInvocation getClassInvocation(final GenType aGenType, final PercyWantConstructor aPercyWantConstructor) {
		NotImplementedException.raise_stop();
		return null;
	}
}
