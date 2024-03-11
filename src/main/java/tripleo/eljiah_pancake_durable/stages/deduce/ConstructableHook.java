package tripleo.eljiah_pancake_durable.stages.deduce;

import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.Constructable;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IdentTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.VariableTableEntry;

class ConstructableHook {
	private final Constructable co;

	public ConstructableHook(final Constructable aCo) {
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

	public void invoke2(final ClassInvocation clsinv2, final DeduceTypes2 aDeduceTypes, final Context aContext, final BaseGeneratedFunction aBaseGeneratedFunction) {
		if (co == null) {
			return;
		}

		if (co instanceof IdentTableEntry) {
			((IdentTableEntry) co).doConstructable(clsinv2);
		} else if (co instanceof VariableTableEntry) {
			((VariableTableEntry) co).doConstructable2(clsinv2, aDeduceTypes, aContext, aBaseGeneratedFunction);
		}
	}

}
