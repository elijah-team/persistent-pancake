/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.elijah.stages.gen_fn;

import org.jdeferred2.DoneCallback;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.post_bytecode.DeduceElement3_VarTableEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created 2/28/21 3:23 AM
 */
public interface GeneratedContainer extends GeneratedNode {
	OS_Element getElement();

	VarTableEntry getVariable(String aVarName);

	public class VarTableEntry {
		public final VariableStatement vs;
		public final  IdentExpression                                    nameToken;
		public final  IExpression                                        initialValue;
		private final OS_Element                                         parent;
		public        DeferredObject<UpdatePotentialTypesCB, Void, Void> updatePotentialTypesCBPromise = new DeferredObject<>();
		public        TypeName                                           typeName;
		public        OS_Type                                            varType;
		public        List<TypeTableEntry>                               potentialTypes                = new ArrayList<TypeTableEntry>();
		private       GeneratedNode                                      _resolvedType;

		public VarTableEntry(final VariableStatement aVs,
							 final @NotNull IdentExpression aNameToken,
							 final IExpression aInitialValue,
							 final @NotNull TypeName aTypeName,
							 final @NotNull OS_Element aElement) {
			vs              = aVs;
			nameToken       = aNameToken;
			initialValue    = aInitialValue;
			typeName        = aTypeName;
			varType         = new OS_Type(typeName);
			parent          = aElement;
		}

		public DeduceElement3_VarTableEntry getDeduceElement3() {
			return new DeduceElement3_VarTableEntry(this);
		}

		public void addPotentialTypes(@NotNull Collection<TypeTableEntry> aPotentialTypes) {
			potentialTypes.addAll(aPotentialTypes);
		}

		public void resolve(@NotNull GeneratedNode aResolvedType) {
			System.out.println(String.format("** [GeneratedContainer 56] resolving VarTableEntry %s to %s", nameToken, aResolvedType.identityString()));
			_resolvedType = aResolvedType;
		}

		public @Nullable GeneratedNode resolvedType() {
			return _resolvedType;
		}

		public @NotNull OS_Element getParent() {
			return parent;
		}

		public void connect(final VariableTableEntry aVte, final GeneratedConstructor aConstructor) {
			connectionPairs.add(new ConnectionPair(aVte, aConstructor));
		}

		public List<ConnectionPair> connectionPairs = new ArrayList<>();

		public void resolve_varType(final OS_Type aOS_type) {
			_resolve_varType_Promise.resolve(aOS_type);
			varType = aOS_type;
		}

		public void resolve_varType_cb(final DoneCallback<OS_Type> aCallback) {
			_resolve_varType_Promise.then(aCallback);
		}

		private DeferredObject<OS_Type, Void, Void> _resolve_varType_Promise = new DeferredObject<>();

		public interface UpdatePotentialTypesCB {
			void call(final @NotNull GeneratedContainer aGeneratedContainer);
		}

		UpdatePotentialTypesCB updatePotentialTypesCB;

		public void updatePotentialTypes(final @NotNull GeneratedContainer aGeneratedContainer) {
//			assert aGeneratedContainer == GeneratedContainer.this;
			updatePotentialTypesCBPromise.then(new DoneCallback<UpdatePotentialTypesCB>() {
				@Override
				public void onDone(final UpdatePotentialTypesCB result) {
					result.call(aGeneratedContainer);
				}
			});
		}

		public static class ConnectionPair {
			public final VariableTableEntry vte;
			final GeneratedConstructor constructor;

			@Contract(pure = true)
			public ConnectionPair(final VariableTableEntry aVte, final GeneratedConstructor aConstructor) {
				vte = aVte;
				constructor = aConstructor;
			}
		}
	}
}

//
//
//
