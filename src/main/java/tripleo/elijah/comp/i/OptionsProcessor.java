package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilerInput;

import java.util.List;

public interface OptionsProcessor {
	String[] process(Compilation aC, List<CompilerInput> aInputs, ICompilationBus aCb) throws Exception;
}
