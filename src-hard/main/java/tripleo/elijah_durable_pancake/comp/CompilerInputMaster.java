package tripleo.elijah_durable_pancake.comp;

import tripleo.elijah_durable_pancake.comp.internal.CompilerInputListener;

public interface CompilerInputMaster {
	void addListener(CompilerInputListener compilerInputListener);

	void notifyChange(CompilerInput compilerInput, CompilerInput.CompilerInputField compilerInputField);
}
