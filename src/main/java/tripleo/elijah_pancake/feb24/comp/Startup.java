package tripleo.elijah_pancake.feb24.comp;

import tripleo.elijah.comp.ICompilationAccess;

public class Startup {

	public void provideInitial(final CR_State aCRState) {
		System.err.println("initial CR_State");
	}

	public void provide(final ICompilationAccess aCa) {
		System.err.println("provide CompilationAccess");
	}

	public void provide(final ProcessRecord aPr) {
		System.err.println("provide ProcessRecord");
	}
}
