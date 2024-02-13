package tripleo.elijah_prepan.compilation_runner;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.bus.CB_Action;
import tripleo.elijah.comp.bus.CB_OutputString;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah_pancake.feb24.comp.CompilationSignalTarget;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;
import tripleo.elijah_pancake.feb24.comp.Providing;

public class CA_RunBetterAction implements CB_Action {
	private final CompilationRunner  compilationRunner;
	private final CR_RunBetterAction runBetterAction;
	private final CR_State           crState;

	public CA_RunBetterAction(final CompilationRunner aCompilationRunner, final CR_State aSt1) {
		compilationRunner = aCompilationRunner;
		crState           = aSt1;
		runBetterAction   = new CR_RunBetterAction(compilationRunner);
	}

	@Override
	public String name() {
		return "run better";
	}

	@Override
	public void execute() {
		runBetterAction.execute(crState);
	}

	@Override
	public CB_OutputString[] outputStrings() {
		return new CB_OutputString[0];
	}

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

			// implement failure mode (aka all not provided, what happened to my Compilation?)
			compilation.provide(
			  // function in args 2 creates this
			  RuntimeProcesses.class
			  , (final Providing o) -> {
				  final var pr = o.get(ProcessRecord.class);
				  assert pr != null;

				  final RuntimeProcesses rt = StageToRuntime.get(ca.getStage(), ca, (ProcessRecord) pr);
				  return rt;
			  }
			  // everything here should (MUST) be available from o#get when it is called
			  , new Class[]{ProcessRecord.class}
			);

			// FIXME Not sure where this should run ("the UI thread") or where it should be placed (can we xml this?)
			compilation.onTrigger(CS_RunBetter.class, new CompilationSignalTarget(){
				@Override public void run() {
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
				}
			});

			compilation.trigger(CS_RunBetter.class);
		}

		@Override
		public String name() {
			return "run better";
		}
	}
}
