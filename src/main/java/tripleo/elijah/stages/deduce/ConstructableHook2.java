package tripleo.elijah.stages.deduce;

import tripleo.elijah.stages.gen_fn.Constructable;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;

class ConstructableHook2 {
	private final Constructable co;

	public ConstructableHook2(final Constructable aCo) {
		co = aCo;
	}

	public void invoke(final ClassInvocation clsinv2) {
		if (co == null) {
			return;
		}

		if (co instanceof IdentTableEntry) {
			((IdentTableEntry) co).doConstructable(clsinv2);
		} else if (co instanceof VariableTableEntry) {
			((VariableTableEntry) co).doConstructable(clsinv2);
		}
	}
}
