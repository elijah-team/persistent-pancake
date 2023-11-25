package tripleo.elijah.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;

import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang.types.OS_UserType;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeducePhase.RCI2;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.ResolveError;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.Operation;

public class DeduceTypeResolve2 {
	private final DeduceTypes2 deduceTypes2;

	public DeduceTypeResolve2(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
	}

	public void makeGenType(final @NotNull PercyWantType aPercyWantType, final PercyMakeType aMt) {
		aPercyWantType.provide(aMt);
	}

	public ClassInvocation getClassInvocation(final GenType aGenType, final PercyWantConstructor aPercyWantConstructor) {
		NotImplementedException.raise_stop();
		return null;
	}

	public @NotNull Operation<GenType> of(TypeName typeName) {
		@NotNull
		GenType result;
		try {
			result = deduceTypes2.newPFluffyType().resolve_type(new OS_UserType(typeName), typeName.getContext());
		} catch (ResolveError e) {
//			e.printStackTrace();
			return Operation.failure(e);
		}
		return Operation.success(result);
	}

	public RCI2 registerClassInvocation(ClassInvocation clsinv) {
		return deduceTypes2._phase().registerClassInvocation2(clsinv);
	}
}
