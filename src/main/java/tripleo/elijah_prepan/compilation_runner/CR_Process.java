package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.comp.ICompilationBus;

import java.util.List;

public interface CR_Process {
	List<ICompilationBus.CB_Action> steps();
}
