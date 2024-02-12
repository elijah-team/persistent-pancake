package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah.comp.CompilationRunner;

public class CR_AlmostComplete implements CR_Action {

	private final CompilationRunner compilationRunner;

	public CR_AlmostComplete(final CompilationRunner aCompilationRunner) {
		compilationRunner = aCompilationRunner;
	}

	@Override
	public void execute(final CR_State st) {
		compilationRunner._almostComplete();
	}

	@Override
	public String name() {
		return "cis almostComplete";
	}
}
