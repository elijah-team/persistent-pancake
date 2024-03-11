package tripleo.elijah_prepan.compilation_runner;

import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;
import tripleo.eljiah_pancake_durable.comp.bus.CB_OutputString;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_pancake.feb24.comp.CR_State;

class CA_FindStdlibAction implements CB_Action {
	private final CompilationRunner   compilationRunner;
	private final CR_FindStdlibAction aa;
	private final CR_State            st1;

	public CA_FindStdlibAction(final CompilationRunner aCompilationRunner, final CR_State aSt1) {
		compilationRunner = aCompilationRunner;
		st1               = aSt1;
		aa                = new CR_FindStdlibAction(compilationRunner);
	}

	@Override
	public String name() {
		return "find stdlib";
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
