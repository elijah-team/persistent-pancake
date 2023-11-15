package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.*;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.util.NotImplementedException;

import java.util.List;
import java.util.Map;

public class DeduceElement3_VarTableEntry implements IDeduceElement3 {
	private final GeneratedContainer.VarTableEntry _principal;

	@Contract(pure = true)
	public DeduceElement3_VarTableEntry(final GeneratedContainer.VarTableEntry aVarTableEntry) {
		_principal = aVarTableEntry;
	}

	@Override
	public void resolve(final IdentIA aIdentIA, final Context aContext, final FoundElement aFoundElement) {
		throw new NotImplementedException();
	}

	@Override
	public void resolve(final Context aContext, final DeduceTypes2 dt2) {
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getPrincipal() {
		return _principal.vs;
	}

	@Override
	public DED elementDiscriminator() {
		return DED.dispatch(this);
	}

	@Override
	public DeduceTypes2 deduceTypes2() {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public GeneratedFunction generatedFunction() {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public GenType genType() {
		throw new NotImplementedException();
		//return null;
	}

	@Override
	public DeduceElement3_Kind kind() {
		return DeduceElement3_Kind.GEN_FN__GC_VTE;
	}

	private static class STOP extends Exception {

	}

	public void resolve_var_table_entries(final DeducePhase aDeducePhase, final ClassInvocation ci) {
		final GeneratedContainer.VarTableEntry varTableEntry  = _principal;

		final List<TypeTableEntry>             potentialTypes = varTableEntry.potentialTypes;
		final TypeName                         typeName       = varTableEntry.typeName;

		try {
			if (potentialTypes.size() == 0 && (varTableEntry.varType == null || typeName.isNull())) {
				__zero_potential(varTableEntry, typeName);
			} else {
				System.err.println(String.format("108 %s %s", varTableEntry.nameToken, potentialTypes));

				if (potentialTypes.size() == 1) {
					__one_potential(aDeducePhase, varTableEntry, potentialTypes, typeName, ci);
				}
			}
		} catch (STOP stop) {
			NotImplementedException.raise();
		}
	}

	private static void __one_potential(final DeducePhase aDeducePhase,
										final GeneratedContainer.VarTableEntry varTableEntry,
										final @NotNull List<TypeTableEntry> potentialTypes,
										final TypeName typeName, final ClassInvocation ci) throws STOP {
		boolean sc = false;

		TypeTableEntry potentialType = potentialTypes.get(0);
		if (potentialType.resolved() == null) {
			assert potentialType.getAttached() != null;

			final OS_Type.Type attachedType = potentialType.getAttached().getType();
			//assert attachedType == OS_Type.Type.USER_CLASS;
			if (attachedType != OS_Type.Type.USER_CLASS) {
				final OS_Type att = potentialType.getAttached();
				System.err.println("105 "+att);
			}

			{
				//
				// HACK
				//

				if (attachedType != OS_Type.Type.USER_CLASS) {
					final TypeName t = potentialType.getAttached().getTypeName();
					if (ci.genericPart != null) {
						for (Map.Entry<TypeName, OS_Type> typeEntry : ci.genericPart.entrySet()) {
							if (typeEntry.getKey().equals(t)) {
								final OS_Type v = typeEntry.getValue();
								potentialType.setAttached(v);
								assert attachedType == OS_Type.Type.USER_CLASS;
								break;
							}
						}
					}
				}
			}


			//
			if (attachedType == OS_Type.Type.USER_CLASS) {
				ClassInvocation xci = new ClassInvocation(potentialType.getAttached().getClassOf(), null);
				{
					for (Map.Entry<TypeName, OS_Type> entry : xci.genericPart.entrySet()) {
						if (entry.getKey().equals(typeName)) {
							xci.genericPart.put(entry.getKey(), varTableEntry.varType);
						}
					}
				}
				xci = aDeducePhase.registerClassInvocation(xci);
				@NotNull GenerateFunctions gf  = aDeducePhase.generatePhase.getGenerateFunctions(xci.getKlass().getContext().module());
				WlGenerateClass            wgc = new WlGenerateClass(gf, xci, aDeducePhase.generatedClasses);
				wgc.run(null); // !
				potentialType.genType.ci = xci; // just for completeness
				potentialType.resolve(wgc.getResult());
				sc = true;
			} else {
				int y=2;
				System.err.println("177 not a USER_CLASS "+potentialType.getAttached());
			}
		}
		if (potentialType.resolved() != null)
			varTableEntry.resolve(potentialType.resolved());
		else
			System.err.println("114 Can't resolve "+ varTableEntry);

		if (!sc)
			throw new STOP();
	}

	private void __zero_potential(final GeneratedContainer.VarTableEntry varTableEntry, final TypeName tn) {
		if (tn != null) {
			if (tn instanceof NormalTypeName) {
				final NormalTypeName tn2 = (NormalTypeName) tn;
				__zero_potential__1(varTableEntry, tn2);
			}
		} else {
			// must be unknown
		}
	}

	private void __zero_potential__1(final @NotNull GeneratedContainer.VarTableEntry varTableEntry,
									 final @NotNull NormalTypeName aNormalTypeName) {
		// 0. preflight
		if (aNormalTypeName.isNull())
			throw new NotImplementedException();

		// 1. st...
		final String           typeNameName = aNormalTypeName.getName();

		// 2. stage 1
		final LookupResultList lrl          = aNormalTypeName.getContext().lookup(typeNameName);
		OS_Element             best         = lrl.chooseBest(null);

		// 3. validation
		if (best != null) {
			// A)

			//  4. handle special case here
			while (best instanceof AliasStatement) {
				NotImplementedException.raise();
				//assert false;
				best = DeduceLookupUtils._resolveAlias((AliasStatement) best, deduceTypes2());
			}

			// 5. effect
			assert best instanceof ClassStatement;
			varTableEntry.resolve_varType (((ClassStatement) best).getOS_Type());
		} else {
			// B)

			//  4. do later...

			// TODO shouldn't this already be calculated?
			throw new NotImplementedException();
		}
	}
}
