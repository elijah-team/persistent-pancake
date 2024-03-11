package tripleo.elijah_durable_pancake.comp.internal;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.i.LCM_HandleEvent;
import tripleo.eljiah_pancake_durable.comp.i.LCM_Event;

public class LCM {
	private final EventBus          eventBus;
	@SuppressWarnings("FieldCanBeLocal")
	private final LCM_EventListener listener;
	private final Compilation       _compilation;

	public LCM(final Compilation aCompilation) {
		_compilation = aCompilation;

		eventBus     = new EventBus();
		listener     = new LCM_EventListener();

		eventBus.register(listener);
	}

	public void asv(final Object obj, final LCM_Event event) {
		eventBus.post(new LCM_HandleEvent(_compilation.getLCMAccess(), this, obj, event));
	}

	public void exception(final LCM_HandleEvent aHandleEvent, final Exception aE) {
		throw new RuntimeException(aE); // FIXME 11/24 rethrow
	}

	public class LCM_EventListener {
		private static final Logger LOG = LoggerFactory.getLogger(LCM_EventListener.class);
		private static       int    eventsHandled;

		@Subscribe
		public void handleDeadEvent(@NotNull final DeadEvent deadEvent) {
			LOG.info("unhandled event [" + deadEvent.getEvent() + "]");
			eventsHandled++;
		}

		@Subscribe
		public void someLCM_Event(@NotNull final LCM_HandleEvent aHandleEvent) {
			LOG.info("do event [" + aHandleEvent + "]");
			aHandleEvent.event().handle(aHandleEvent);
			eventsHandled++;
		}

		void resetEventsHandled() {
			eventsHandled = 0;
		}

		int getEventsHandled() {
			return eventsHandled;
		}
	}
}
