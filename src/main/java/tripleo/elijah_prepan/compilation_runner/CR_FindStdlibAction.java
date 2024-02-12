package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

public class CR_FindStdlibAction implements CR_Action {
	private final CompilationRunner compilationRunner;

	public CR_FindStdlibAction(final CompilationRunner aCompilationRunner) {
		compilationRunner = aCompilationRunner;
	}

	@Override
	public void execute(final CR_State st) {
		final Operation<CompilerInstructions> x = compilationRunner.findStdLib(Compilation.CompilationAlways.defaultPrelude(), compilationRunner._accessCompilation());
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
