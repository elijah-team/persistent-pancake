package tripleo.elijah_pancake.feb24.comp;

import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CompilationAccess;
import tripleo.eljiah_pancake_durable.comp.i.ICompilationAccess;
import tripleo.elijah_prepan.compilation_runner.RuntimeProcesses;

public class CR_State {
	private final CompilationRunner  compilationRunner;
	private final Startup            startup;
	public        CB_Action          cur;
	public        ICompilationAccess ca;
	public        ProcessRecord      pr;
	public        RuntimeProcesses   rt;

	public CR_State(final CompilationRunner aCompilationRunner, final Startup aStartup) {
		compilationRunner = aCompilationRunner;
		startup = aStartup;

		startup.provideInitial(this);
	}

	public ICompilationAccess ca() {
		if (ca == null) {
			ca = new EDP_CompilationAccess(compilationRunner._accessCompilation());
			pr = new ProcessRecord(ca);

			startup.provide(ca);
			startup.provide(pr);
		}

		return ca;
	}

	public void provide(final RuntimeProcesses aRt) {
		assert rt == null; // being lazy here (forgot spec)
		rt = aRt;
	}
}
