package tripleo.elijah.stages.deduce;

import tripleo.elijah.stages.deduce.post_bytecode.Stateful;

class StatefulRunnable extends Stateful {
	private final Runnable runnable;

	private StatefulRunnable(final Runnable aRunnable) {
		runnable = aRunnable;
	}

	public void run() {
		runnable.run();
	}
}
