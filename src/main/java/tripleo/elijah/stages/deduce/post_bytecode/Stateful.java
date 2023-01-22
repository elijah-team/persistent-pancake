package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.NotNull;

public abstract class Stateful {
	private State _state;

	public void mvState(State aO, @NotNull State aState) {
		assert aO == null;

		if (!aState.checkState(this))
			throw new BadState();

		aState.apply(this);
		this.setState(aState);
	}

	private void setState(final State aState) {
		_state = aState;
	}
}
