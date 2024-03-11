package tripleo.eljiah_pancake_durable.stages.deduce.zero;

import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.elijah.util.NotImplementedException;

class ZeroResolver {

	GenType gt;
	private DeduceTypes2 deduceTypes2;

	public Zero_Type resolve_type(final OS_Type ty) {
		try {
			gt = deduceTypes2.newPFluffyType().resolve_type(ty, ty.getTypeName().getContext());
			return new Zero_Type(gt);
		} catch (final ResolveError aE) {
			NotImplementedException.raise();
		}
		return null;
	}
}
