package tripleo.elijah.comp;

import tripleo.elijah.comp.internal.*;

public interface CompilerInputMaster {
	void addListener(CompilerInputListener compilerInputListener);

	void notifyChange(CompilerInput compilerInput, CompilerInput.CompilerInputField compilerInputField);
}
