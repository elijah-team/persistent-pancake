package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah_pancake.feb24.comp.CR_State;

import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

public class CompilationRunnerProcess implements ICompilationBus.CB_Process {
	private final CompilationRunner         compilationRunner;
	// 1. find stdlib
	//   -- question placement
	//   -- ...
	final         ICompilationBus.CB_Action a;
	// 2. process the initial
	final         ICompilationBus.CB_Action b;
	// 3. do rest
	final         ICompilationBus.CB_Action c;

	public CompilationRunnerProcess(final CompilationRunner aCompilationRunner, final CR_State aCRState, final CompilerInstructions aCi) {
		compilationRunner = aCompilationRunner;
		a                 = new CA_FindStdlibAction(compilationRunner, aCRState);
		b                 = new CA_ProcessInitialAction(compilationRunner, aCi, aCRState);
		c                 = new CA_RunBetterAction(compilationRunner, aCRState);
	}

	@Override
	public List<ICompilationBus.CB_Action> steps() {
		return List_of(a, b, c);
	}
}
