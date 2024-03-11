package tripleo.elijah_pancake.feb24.googly;

import tripleo.eljiah_pancake_durable.lang.OS_Module;

public class P {
	public static final int PL_ADD_MODULE = 100;
	public static final int CR_START_ROOT = 101;

	public static void googly(final int code, final Object object) {
		switch (code) {
		case PL_ADD_MODULE -> {
			System.err.println("8787 " + ((OS_Module) object).getFileName());
		}
		case CR_START_ROOT -> {
			//((LCM_Event_RootCI)object).getClass().getName()
			//final CompilerInstructions rootCI = (CompilerInstructions) aHandleEvent.obj();
			System.err.println("VVVVV " + (String) object);
		}
		}
	}
}
