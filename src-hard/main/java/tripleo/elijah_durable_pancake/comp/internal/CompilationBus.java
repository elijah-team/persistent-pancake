package tripleo.elijah_durable_pancake.comp.internal;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah.comp.bus.CB_Action;
import tripleo.elijah.comp.bus.CB_Process;
import tripleo.elijah_durable_pancake.comp.ILazyCompilerInstructions;
import tripleo.elijah_durable_pancake.compilation_runner.CompilationChange;

// scala actors, xtend async,
public class CompilationBus implements ICompilationBus {
	private final Compilation c;

	public CompilationBus(final Compilation aC) {
		c = aC;
	}

	@Override
	public void option(final @NotNull CompilationChange aChange) {
		aChange.apply(c);
	}

	@Override
	public void inst(final @NotNull ILazyCompilerInstructions aLazyCompilerInstructions) {
		System.out.println("** [ci] " + aLazyCompilerInstructions.get());
	}

	public void add(final CB_Action action) {
		action.execute();
	}

	@Override
	public void add(final CB_Process aProcess) {
//		throw new NotImplementedException();

		aProcess.steps().stream()
		  .forEach(action -> {action.execute();});
	}
}
