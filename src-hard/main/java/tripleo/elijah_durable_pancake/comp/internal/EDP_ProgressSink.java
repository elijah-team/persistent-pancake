package tripleo.elijah_durable_pancake.comp.internal;

import tripleo.eljiah_pancake_durable.comp.i.IProgressSink;
import tripleo.eljiah_pancake_durable.comp.i.ProgressSinkComponent;

public class EDP_ProgressSink implements IProgressSink {
	@Override
	public void note(final int code, final ProgressSinkComponent component, final int type, final Object[] params) {
//		component.note(code, type, params);
		if (component.isPrintErr(code, type)) {
			final String s = component.printErr(code, type, params);
			System.err.println(s);
		}
	}
}
