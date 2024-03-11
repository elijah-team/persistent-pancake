package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.eljiah_pancake_durable.stages.instructions.IntegerIA;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseTableEntry;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenericElementHolderWithIntegerIA;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IElementHolder;
import tripleo.eljiah_pancake_durable.stages.gen_fn.VariableTableEntry;

public class DTR_IdentExpresssion {
	private final DeduceTypeResolve deduceTypeResolve;
	private final IdentExpression   identExpression;
	private final BaseTableEntry    bte;

	public DTR_IdentExpresssion(final DeduceTypeResolve aDeduceTypeResolve, final IdentExpression aIdentExpression, final BaseTableEntry aBte) {
		deduceTypeResolve = aDeduceTypeResolve;
		identExpression   = aIdentExpression;
		bte               = aBte;
	}

	public void run(final IElementHolder eh, final GenType genType) {
		if (eh instanceof GenericElementHolderWithIntegerIA) {
			final GenericElementHolderWithIntegerIA eh1                = (GenericElementHolderWithIntegerIA) eh;
			final IntegerIA                         integerIA          = eh1.getIntegerIA();
			final @NotNull VariableTableEntry       variableTableEntry = integerIA.getEntry();

			assert variableTableEntry == bte;

			variableTableEntry.typeResolvePromise().then(
			  new DoneCallback<GenType>() {
				  @Override
				  public void onDone(final GenType result) {
					  genType.copy(result); // TODO genType = result?? because we want updates...
				  }
			  });
		} else {
			final DeduceLocalVariable dlv = ((VariableTableEntry) bte).dlv;
			final int                 y   = 2;
		}
	}
}
