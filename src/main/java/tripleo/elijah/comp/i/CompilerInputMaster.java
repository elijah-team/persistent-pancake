package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.comp.internal.*;

public interface CompilerInputMaster {
	void addListener(CompilerInputListener compilerInputListener);

	void notifyChange(CompilerInput compilerInput, CompilerInput.CompilerInputField compilerInputField);
}
