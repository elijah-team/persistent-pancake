package tripleo.elijah_durable_pancake.comp;

public interface CompilerInputMaster {
	void addListener(tripleo.elijah.comp.internal.CompilerInputListener compilerInputListener);

	void notifyChange(CompilerInput compilerInput, CompilerInput.CompilerInputField compilerInputField);
}
