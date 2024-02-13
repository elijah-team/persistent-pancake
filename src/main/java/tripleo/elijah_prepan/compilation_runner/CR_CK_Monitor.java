package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah_durable_pancake.comp.CompilationRunner;

public class CR_CK_Monitor implements CK_Monitor {
	private final CompilationRunner compilationRunner;

	public CR_CK_Monitor(final CompilationRunner aCompilationRunner) {
		compilationRunner = aCompilationRunner;
	}

	@Override
	public void reportFailure(final int code, final String message) {
		final String s = "CR_ProcessInitialAction >> " + code + " " + message;
		compilationRunner._accessCompilation().getErrSink().reportError(s);
	}
}
