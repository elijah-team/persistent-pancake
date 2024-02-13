package tripleo.elijah.comp;

import tripleo.elijah.comp.bus.CB_Action;
import tripleo.elijah.comp.bus.CB_Process;
import tripleo.elijah_durable_pancake.compilation_runner.CompilationChange;
import tripleo.elijah_durable_pancake.comp.ILazyCompilerInstructions;

public interface ICompilationBus {
	void option(CompilationChange aChange);

	void inst(ILazyCompilerInstructions aLazyCompilerInstructions);

	void add(CB_Action aCBAction);

	void add(CB_Process aProcess);
}
