package tripleo.elijah_prepan.compilation_runner;

import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;
import tripleo.eljiah_pancake_durable.comp.bus.CB_Process;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_pancake.feb24.comp.CR_State;

import java.util.List;

import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

public class CompilationRunnerProcess implements CB_Process {
	private final CompilationRunner compilationRunner;
	// 1. find stdlib
	//   -- question placement
	//   -- ...
	final         CB_Action         a;
	// 2. process the initial
	final         CB_Action         b;
	// 3. do rest
	final         CB_Action         c;

	public CompilationRunnerProcess(final CompilationRunner aCompilationRunner, final CR_State aCRState, final CompilerInstructions aCi) {
		compilationRunner = aCompilationRunner;
		a                 = new CA_FindStdlibAction(compilationRunner, aCRState);
		b                 = new CA_ProcessInitialAction(compilationRunner, aCi, aCRState);
		c                 = new CA_RunBetterAction(compilationRunner, aCRState);
	}

	@Override
	public List<CB_Action> steps() {
		return List_of(a, b, c);
	}
}
