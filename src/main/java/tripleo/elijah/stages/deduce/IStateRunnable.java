package tripleo.elijah.stages.deduce;

import tripleo.elijah.stages.deduce.post_bytecode.State;
import tripleo.elijah.stages.deduce.post_bytecode.Stateful;

public abstract class IStateRunnable extends Stateful {
	public abstract void run();

	public static class ST {
		public static State EXIT_RUN;

		public static void register(final DeducePhase aDeducePhase) {
			EXIT_RUN = aDeducePhase.register(new ExitRunState());
		}

		private static class ExitRunState implements State {
			private boolean runAlready;

			@Override
			public void apply(final Stateful element) {
//				boolean b = ((StatefulBool) element).getValue();
				if (!runAlready) {
					runAlready = true;
					((StatefulRunnable) element).run();
				}
			}

			@Override
			public void setIdentity(final int aId) {

			}

			@Override
			public boolean checkState(final Stateful aElement3) {
				return true;
			}

		}

	}
}
