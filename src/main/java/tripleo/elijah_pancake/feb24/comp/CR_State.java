package tripleo.elijah_pancake.feb24.comp;

import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.DefaultCompilationAccess;
import tripleo.elijah.comp.ICompilationAccess;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah_prepan.compilation_runner.RuntimeProcesses;

public class CR_State {
	private final CompilationRunner         compilationRunner;
	private final Startup                   startup;
	public        ICompilationBus.CB_Action cur;
	public        ICompilationAccess        ca;
	public        ProcessRecord             pr;
	public        RuntimeProcesses          rt;

	public CR_State(final CompilationRunner aCompilationRunner, final Startup aStartup) {
		compilationRunner = aCompilationRunner;
		startup = aStartup;

		startup.provideInitial(this);
	}

	public ICompilationAccess ca() {
		if (ca == null) {
			ca = new DefaultCompilationAccess(compilationRunner._accessCompilation());
			pr = new ProcessRecord(ca);

			startup.provide(ca);
			startup.provide(pr);
		}

		return ca;
	}

	public void provide(final RuntimeProcesses aRt) {
		rt = aRt;
	}
}
