package tripleo.elijah_pancake.feb24.comp;

import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.eljiah_pancake_durable.comp.i.ICompilationAccess;

public class Startup {
	private final Compilation c;

	public Startup(final Compilation aC) {
		c = aC;
	}

	public void provideInitial(final CR_State aCRState) {
		System.err.println("initial CR_State");
	}

	public void provide(final ICompilationAccess aCa) {
		System.err.println("provide CompilationAccess");
		c.provide(ICompilationAccess.class, (o)-> aCa, new Class[]{}); // don't think, just put this in as is (calling lambda)
	}

	public void provide(final ProcessRecord aPr) {
		System.err.println("provide ProcessRecord");
		c.provide(ProcessRecord.class, (o)-> aPr, new Class[]{}); // don't think, just put this in as is (calling lambda)
	}

	public void completeInitial(final CompilationRunner aCompilationRunner) {
		c.provide(CompilationRunner.class, (o)-> aCompilationRunner, new Class[]{}); // don't think, just put this in as is (calling lambda)
	}
}
