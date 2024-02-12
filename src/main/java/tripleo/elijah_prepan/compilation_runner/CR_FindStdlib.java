package tripleo.elijah_prepan.compilation_runner;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.UnintendedUseException;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

public class CR_FindStdlib implements CR_Action {

	private final CompilationRunner compilationRunner;
	private       String            prelude_name;

	CR_FindStdlib(final CompilationRunner aCompilationRunner, final String aPreludeName) {
		compilationRunner = aCompilationRunner;
		prelude_name      = aPreludeName;
	}

//	@Override
//	public void attach(final CompilationRunner cr) {
//
//	}

	@Override
	public void execute(final CR_State st) {
		@NotNull final Operation<CompilerInstructions> op = compilationRunner.findStdLib(prelude_name, compilationRunner._accessCompilation());
		assert op.mode() == Mode.SUCCESS; // TODO .NOTHING??
	}

	@Override
	public String name() {
//		return null;
		throw new UnintendedUseException("????");
	}
}
