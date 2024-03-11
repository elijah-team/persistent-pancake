package tripleo.elijah_prepan.compilation_runner;

import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.CompilationAlways;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_pancake.feb24.comp.CR_State;

public class CR_FindStdlibAction implements CR_Action {
	private final CompilationRunner compilationRunner;

	public CR_FindStdlibAction(final CompilationRunner aCompilationRunner) {
		compilationRunner = aCompilationRunner;
	}

	@Override
	public void execute(final CR_State st) {
		final Operation<CompilerInstructions> x = compilationRunner.findStdLib(CompilationAlways.defaultPrelude(), compilationRunner._accessCompilation());
		if (x.mode() == Mode.FAILURE) {
			compilationRunner._accessCompilation().getErrSink().exception(x.failure());
			return;
		}
		compilationRunner.logProgress(130, "GEN_LANG: " + x.success().genLang());
	}

	@Override
	public String name() {
		return "find stdlib";
	}
}
