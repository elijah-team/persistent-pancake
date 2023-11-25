package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.comp.internal.CompilationBus;

import java.util.List;

public interface OptionsProcessor {
	String[] process(Compilation aC, List<CompilerInput> aInputs, CompilationBus aCb) throws Exception;
}
