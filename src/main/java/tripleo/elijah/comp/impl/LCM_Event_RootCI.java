package tripleo.elijah.comp.impl;

import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.i.LCM_CompilerAccess;
import tripleo.elijah.comp.i.LCM_Event;
import tripleo.elijah.comp.i.LCM_HandleEvent;
import tripleo.elijah.comp.internal.CompilationImpl;

public class LCM_Event_RootCI implements LCM_Event {
	private static final LCM_Event_RootCI INSTANCE = new LCM_Event_RootCI();

	public static LCM_Event_RootCI instance() {
		return INSTANCE;
	}

	@Override
	public void handle(final LCM_HandleEvent aHandleEvent) {
		final LCM_CompilerAccess                c      = aHandleEvent.compilation();
		final CompilerInstructions              rootCI = (CompilerInstructions) aHandleEvent.obj();

		try {
//			c.c().setRootCI(rootCI);
			c.cr().start(rootCI, false);
		} catch (Exception aE) {
			aHandleEvent.lcm().exception(aHandleEvent, aE);
		}
	}
}
