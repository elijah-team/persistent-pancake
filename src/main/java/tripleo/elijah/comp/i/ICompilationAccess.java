package tripleo.elijah.comp.i;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah_prepan.compilation_runner.Stages;

import java.util.List;

public interface ICompilationAccess {
	ElLog.Verbosity testSilence();

	Compilation getCompilation();

	void writeLogs();

	Stages getStage();

	List<IFunctionMapHook> functionMapHooks();
}
