package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilerInput;

import java.util.List;

public interface CompilerController {
	void printUsage();

	void processOptions();

	void runner();

	void _setInputs(List<CompilerInput> aCompilerInputs);
}
