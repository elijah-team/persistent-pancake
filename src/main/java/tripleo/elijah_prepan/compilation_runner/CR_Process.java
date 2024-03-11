package tripleo.elijah_prepan.compilation_runner;

import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;

import java.util.List;

public interface CR_Process {
	List<CB_Action> steps();
}
