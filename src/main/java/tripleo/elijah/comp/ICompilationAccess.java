package tripleo.elijah.comp;

import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;

import java.util.List;

public interface ICompilationAccess {
	ElLog.Verbosity testSilence();

	Compilation getCompilation();

	void writeLogs();

	Stages getStage();

	List<IFunctionMapHook> functionMapHooks();
}
