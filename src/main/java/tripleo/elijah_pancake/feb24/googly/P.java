package tripleo.elijah_pancake.feb24.googly;

import tripleo.elijah.lang.OS_Module;

public class P {
	public static final int PL_ADD_MODULE = 100;

	public static void googly(final int code, final Object object) {
		switch (code) {
		case PL_ADD_MODULE -> {
			System.err.println("8787 " + ((OS_Module) object).getFileName());
		}
		}
	}
}
