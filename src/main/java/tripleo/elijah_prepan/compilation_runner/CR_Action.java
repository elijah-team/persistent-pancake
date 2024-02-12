package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah_pancake.feb24.comp.CR_State;

public interface CR_Action {
//		void attach(CompilationRunner cr);

	void execute(CR_State st);

	String name();
}
