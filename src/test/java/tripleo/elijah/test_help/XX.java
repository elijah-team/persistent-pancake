package tripleo.elijah.test_help;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.util.Helpers;

public class XX {

	public IdentExpression ident(final String aX) {
		final IdentExpression identExpression = Helpers.string_to_ident(aX);
		return identExpression;
	}

	public TypeTableEntry regularTypeName_specifyTableEntry(final IdentExpression aIdentExpression,
															final @NotNull BaseGeneratedFunction aBaseGeneratedFunction,
															final String aTypeName) {
		final RegularTypeName typeName = RegularTypeName.makeWithStringTypeName(aTypeName);
		final OS_Type         type     = new OS_Type(typeName);
		final TypeTableEntry  tte      = aBaseGeneratedFunction.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, type, aIdentExpression);

		return tte;
	}

	public VariableStatement sequenceAndVarNamed(final IdentExpression aIdentExpression) {
		final VariableSequence  seq   = new VariableSequence();
		final VariableStatement x_var = new VariableStatement(seq);

		x_var.setName(aIdentExpression);

		return x_var;
	}
}
