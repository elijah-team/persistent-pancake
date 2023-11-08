package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.NotNull;

import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;

import java.util.Objects;

public class DeduceNames {
	public static boolean sameName(final @NotNull VariableStatement aVariableStatement, final @NotNull VariableTableEntry aVte) {
		final String variableStatementName = aVariableStatement.getName();
		final String vteName               = aVte.getName();

		return Objects.equals(variableStatementName,vteName);
	}
}
