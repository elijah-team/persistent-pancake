package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.ICompilationBus;

class CA_FindStdlibAction implements ICompilationBus.CB_Action {
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
	public ICompilationBus.OutputString[] outputStrings() {
		return new ICompilationBus.OutputString[0];
	}
}
