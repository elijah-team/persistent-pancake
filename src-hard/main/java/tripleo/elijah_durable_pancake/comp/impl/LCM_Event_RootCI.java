package tripleo.elijah_durable_pancake.comp.impl;

import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.i.LCM_CompilerAccess;
import tripleo.eljiah_pancake_durable.comp.i.LCM_Event;
import tripleo.eljiah_pancake_durable.comp.i.LCM_HandleEvent;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;
import tripleo.elijah_pancake.feb24.googly.P;

public class LCM_Event_RootCI implements LCM_Event {
	private static class LCM_Event_RootCI$ {
		private static final LCM_Event_RootCI INSTANCE = new LCM_Event_RootCI();
	}

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
			c.cr().start(rootCI);

			EDP_Compilation cc = (EDP_Compilation) c.c();
			cc.central().waitPipelineLogic(pl -> pl.dp.checkFinishEventuals());
		} catch (Exception aE) {
			aHandleEvent.lcm().exception(aHandleEvent, aE);
		}
	}
}
