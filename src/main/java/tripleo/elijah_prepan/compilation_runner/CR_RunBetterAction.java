package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.ICompilationAccess;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah_pancake.feb24.comp.Providing;

public class CR_RunBetterAction implements CR_Action {

	private final CompilationRunner compilationRunner;
	private final CK_Monitor        monitor;

	public CR_RunBetterAction(final CompilationRunner aCompilationRunner) {
		compilationRunner = aCompilationRunner;
		monitor           = new CR_CK_Monitor(compilationRunner);
	}

	@Override
	public void execute(final CR_State st) {
		final ICompilationAccess ca          = st.ca();
		final Compilation        compilation = ca.getCompilation();

		compilation.provide(
		  RuntimeProcesses.class
		  , (Providing o) -> {
			  var pr = o.get(ProcessRecord.class);
			  assert pr != null;

			  RuntimeProcesses rt = StageToRuntime.get(ca.getStage(), ca, (ProcessRecord) pr);
			  return rt;
		  }
		  , new Class[]{ProcessRecord.class}
		);

		compilation.waitProviders(
		  new Class[]{RuntimeProcesses.class}
		  , (o) -> {
			  final RuntimeProcesses rt = (RuntimeProcesses) o.get(RuntimeProcesses.class);

			  st.provide(rt);

			  try {
				  rt.run_better();
			  } catch (final Exception aE) {
				  monitor.reportFailure(CK_Monitor.PLACEHOLDER_CODE_2, "" + aE);
			  }
		  }
		);

		compilation.trigger(CS_RunBetter.class);
	}

	@Override
	public String name() {
		return "run better";
	}
}
