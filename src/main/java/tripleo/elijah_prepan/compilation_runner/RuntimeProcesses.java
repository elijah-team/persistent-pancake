package tripleo.elijah_prepan.compilation_runner;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.ICompilationAccess;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;

public class RuntimeProcesses {
	private final ICompilationAccess ca;
	private final ProcessRecord      pr;
	private       RuntimeProcess     process;

	public RuntimeProcesses(final @NotNull ICompilationAccess aca, final @NotNull ProcessRecord aPr) {
		ca = aca;
		pr = aPr;
	}

	public void add(final RuntimeProcess aProcess) {
		process = aProcess;
	}

	public int size() {
		return process == null ? 0 : 1;
	}

	public void run_better() throws Exception {
		// do nothing. job over
		if (ca.getStage() == Stages.E) return;

		// rt.prepare();
		logProgress("***** RuntimeProcess [prepare] named " + process);
		process.prepare();

		// rt.run();
		logProgress("***** RuntimeProcess [run    ] named " + process);
		process.run();

		// rt.postProcess(pr);
		logProgress("***** RuntimeProcess [postProcess] named " + process);
		process.postProcess();

		logProgress("***** RuntimeProcess^ [postProcess/writeLogs]");
		pr.writeLogs(ca);
	}

	private void logProgress(final String aS) {
//		System.err.println(aS);
	}
}
