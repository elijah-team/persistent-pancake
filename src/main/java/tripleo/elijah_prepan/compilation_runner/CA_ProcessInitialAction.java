package tripleo.elijah_prepan.compilation_runner;

import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;
import tripleo.eljiah_pancake_durable.comp.bus.CB_OutputString;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_pancake.feb24.comp.CR_State;

public class CA_ProcessInitialAction implements CB_Action {
	private final CompilationRunner       compilationRunner;
	private final CR_ProcessInitialAction aa;
	private final CR_State                st1;

	public CA_ProcessInitialAction(final CompilationRunner aCompilationRunner, final CompilerInstructions aCi, final CR_State aSt1) {
		compilationRunner = aCompilationRunner;
		st1               = aSt1;
		aa                = new CR_ProcessInitialAction(compilationRunner, aCi);
	}

	@Override
	public String name() {
		return "process initial action";
	}

	@Override
	public void execute() {
		aa.execute(st1);
	}

	@Override
	public CB_OutputString[] outputStrings() {
		return new CB_OutputString[0];
	}
}
