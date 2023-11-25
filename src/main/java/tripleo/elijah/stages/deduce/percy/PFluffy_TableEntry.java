package tripleo.elijah.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.gen_fn.Constructable;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah.stages.gen_fn.IdentTableEntry;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;

public class PFluffy_TableEntry {
	public static void handle001(final Constructable aCo,
	                             final ClassContext.OS_TypeNameElement aTypeNameElement,
	                             final @NotNull ClassInvocation clsinv,
	                             final @NotNull ProcTableEntry aPte,
	                             final DeduceTypes2 aDeduceTypes2) {
		if (aCo instanceof IdentTableEntry) {
			final @Nullable IdentTableEntry idte3 = (IdentTableEntry) aCo;
			idte3.getType().genTypeCI(clsinv);
			clsinv.resolvePromise().then(idte3::resolveTypeToClass);
		} else if (aCo instanceof VariableTableEntry) {
			final @NotNull VariableTableEntry vte = (VariableTableEntry) aCo;
			vte.type.genTypeCI(clsinv);
			clsinv.resolvePromise().then(vte::resolveTypeToClass);
		}
		aPte.setClassInvocation(clsinv);
		aPte.setResolvedElement(aTypeNameElement);
		// set FunctionInvocation with pte args
		{
			@Nullable ConstructorDef cc = null;
//			if (constructorName != null) {
//				final Collection<ConstructorDef> cs = aTypeNameElement.getConstructors();
//				for (@NotNull final ConstructorDef c : cs) {
//					if (c.name().equals(constructorName)) {
//						cc = c;
//						break;
//					}
//				}
//			}

			// TODO also check arguments
			{
				assert cc != null || aPte.getArgs().size() == 0;

				final GeneratePhase      generatePhase      = aDeduceTypes2._phase().generatePhase;
				final FunctionInvocation functionInvocation = aDeduceTypes2.newFunctionInvocation(cc, aPte, clsinv, generatePhase);
				if (functionInvocation != null) {
					aPte.setFunctionInvocation(functionInvocation);
				} else {
					System.err.println("FAIL 55");
				}
			}
		}
	}
}
