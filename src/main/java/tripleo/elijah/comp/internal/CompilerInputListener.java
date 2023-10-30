package tripleo.elijah.comp.internal;

import tripleo.elijah.comp.*;

public interface CompilerInputListener {
	default void baseNotify(CompilerInput compilerInput, CompilerInput.CompilerInputField compilerInputField) {
		change(compilerInput, compilerInputField);
	}

	void change(CompilerInput i, CompilerInput.CompilerInputField field);
}
