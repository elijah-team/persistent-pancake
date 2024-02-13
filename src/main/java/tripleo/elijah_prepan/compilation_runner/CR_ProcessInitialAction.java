package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;

public class CR_ProcessInitialAction implements CR_Action {
	private final CompilationRunner    compilationRunner;
	private final CompilerInstructions pia_RootCI;
	private final CK_Monitor           monitor;

	public CR_ProcessInitialAction(final CompilationRunner aCompilationRunner, final CompilerInstructions aCi) {
		compilationRunner = aCompilationRunner;
		pia_RootCI        = aCi;
		monitor           = new CR_CK_Monitor(compilationRunner);
	}

	@Override
	public void execute(final CR_State st) {
		try {
			compilationRunner._accessCompilation().use(pia_RootCI, false);
		} catch (final Exception aE) {
			monitor.reportFailure(CK_Monitor.PLACEHOLDER_CODE, "" + aE);
		}
	}

	@Override
	public String name() {
		return "process initial";
	}

}
