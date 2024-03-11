package tripleo.eljiah_pancake_durable.stages.deduce;

import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.DefaultStateful;

public class StatefulBool extends DefaultStateful {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean aValue) {
		value = aValue;
	}
}
