package tripleo.elijah.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.types.OS_UserClassType;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.BaseTableEntry;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedClass;
import tripleo.elijah.stages.gen_fn.GenericElementHolder;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;

class Resolve_var_table_entry {
	private final DeduceTypes2 deduceTypes2;

	public Resolve_var_table_entry(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
	}

	public void act(@NotNull final VariableTableEntry vte, final BaseGeneratedFunction generatedFunction, final Context ctx) {
		if (vte.getResolvedElement() == null)
			return;
		{
			if (vte.type.getAttached() == null && vte.constructable_pte != null) {
				final ClassStatement   c        = vte.constructable_pte.getFunctionInvocation().getClassInvocation().getKlass();
				final @NotNull OS_Type attached = new OS_UserClassType(c);
				// TODO this should have been set somewhere already
				//  typeName and nonGenericTypeName are not set
				//  but at this point probably wont be needed
				vte.type.getGenType(deduceTypes2).setResolved(attached);
				vte.type.setAttached(attached, deduceTypes2.resolver());
			}
			vte.setStatus(BaseTableEntry.Status.KNOWN, new GenericElementHolder(vte.getResolvedElement()));
			{
				final GenType genType = vte.type.getGenType(deduceTypes2);
				if (genType.getResolved() != null && genType.getNode() == null) {
					genType.genCI(genType.getNonGenericTypeName(), deduceTypes2, deduceTypes2._errSink(), deduceTypes2._phase());
//					genType.node = makeNode(genType);
					//
					// registerClassInvocation does the job of makeNode, so results should be immediately available
					//
					((ClassInvocation) genType.getCi()).resolvePromise().then(new DoneCallback<GeneratedClass>() {
						@Override
						public void onDone(final GeneratedClass result) {
							genType.setNode(result);
							if (!vte.typePromise().isResolved()) // HACK
								vte.resolveType(genType);
						}
					});
				}
			}
		}
	}
}
