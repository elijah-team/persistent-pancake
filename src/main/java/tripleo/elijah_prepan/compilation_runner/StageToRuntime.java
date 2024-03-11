package tripleo.elijah_prepan.compilation_runner;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.i.ICompilationAccess;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;

public class StageToRuntime {
	@Contract("_, _, _ -> new")
	@NotNull
	public static RuntimeProcesses get(final @NotNull Stages stage,
	                                   final @NotNull ICompilationAccess ca,
	                                   final @NotNull ProcessRecord aPr) {
		final RuntimeProcesses r = new RuntimeProcesses(ca, aPr);

		r.add(stage.getProcess(ca, aPr));

		return r;
	}
}
