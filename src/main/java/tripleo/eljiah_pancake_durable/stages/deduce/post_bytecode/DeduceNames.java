package tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode;

import org.jetbrains.annotations.NotNull;

import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.stages.gen_fn.VariableTableEntry;

import java.util.Objects;

public class DeduceNames {
	public static boolean sameName(final @NotNull VariableStatement aVariableStatement, final @NotNull VariableTableEntry aVte) {
		final String variableStatementName = aVariableStatement.getName();
		final String vteName               = aVte.getName();

		return Objects.equals(variableStatementName,vteName);
	}
}
