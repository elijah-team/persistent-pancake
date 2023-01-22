package tripleo.elijah.stages.deduce;

import tripleo.elijah.stages.deduce.post_bytecode.Stateful;

public class StatefulBool extends Stateful {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean aValue) {
		value = aValue;
	}
}
