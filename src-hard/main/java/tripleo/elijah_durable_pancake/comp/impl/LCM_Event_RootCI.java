package tripleo.elijah_durable_pancake.comp.impl;

import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;
import tripleo.elijah_pancake.feb24.googly.P;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.i.LCM_CompilerAccess;
import tripleo.eljiah_pancake_durable.comp.i.LCM_Event;
import tripleo.eljiah_pancake_durable.comp.i.LCM_HandleEvent;

public class LCM_Event_RootCI implements LCM_Event {
	private LCM_Event_RootCI() {
	}

	// TODO 24/01/12 (https://openjdk.org/jeps/8312611) the function was a good call?
	public static LCM_Event_RootCI instance() {
		return LCM_Event_RootCI$.INSTANCE;
	}

	@Override
	public void handle(final LCM_HandleEvent aHandleEvent) {
		P.googly(P.CR_START_ROOT, this.getClass().getName());


		final LCM_CompilerAccess   c      = aHandleEvent.compilation();
		final CompilerInstructions rootCI = (CompilerInstructions) aHandleEvent.obj();

		try {
//			c.c().setRootCI(rootCI);

			final EDP_Compilation cc = (EDP_Compilation) c.c();
//			cc.onTrigger(EDP_CompilerController.CPP_Runner.class, new CompilationSignalTarget() {
//				@Override
//				public void run() {
			cc.waitProviders(new Class[]{EDP_CompilerController.CPP_Runner.class}, (o) -> {
				EDP_CompilerController.CPP_Runner cpprunner = (EDP_CompilerController.CPP_Runner) o.get(EDP_CompilerController.CPP_Runner.class);
				var                               cr        = cpprunner.cr();
				try {
					cr.start(rootCI);
					cc.central().waitPipelineLogic(pl -> pl.dp.checkFinishEventuals());
				} catch (Exception aE) {
//							throw new RuntimeException(aE);
					aHandleEvent.lcm().exception(aHandleEvent, aE);
				}
			});
//				}
//			});
		} catch (Exception aE) {
			aHandleEvent.lcm().exception(aHandleEvent, aE);
		}
	}

	private static class LCM_Event_RootCI$ {
		private static final LCM_Event_RootCI INSTANCE = new LCM_Event_RootCI();
	}
}
