package tripleo.eljiah_pancake_durable.stages.deduce;

import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.DefaultStateful;

class StatefulRunnable extends DefaultStateful implements IStateRunnable {
	private final Runnable runnable;

	public StatefulRunnable(final Runnable aRunnable) {
		runnable = aRunnable;
	}

	public void run() {
		runnable.run();
	}
}
