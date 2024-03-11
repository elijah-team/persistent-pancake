package tripleo.eljiah_pancake_durable.comp.i;

import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.testing.comp.IFunctionMapHook;
import tripleo.elijah_prepan.compilation_runner.Stages;

import java.util.List;

public interface ICompilationAccess {
	ElLog.Verbosity testSilence();

	Compilation getCompilation();

	void writeLogs();

	Stages getStage();

	List<IFunctionMapHook> functionMapHooks();
}
