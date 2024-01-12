package tripleo.elijah;

import com.google.common.base.MoreObjects;
import org.jdeferred2.DoneCallback;
import org.jdeferred2.FailCallback;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.diagnostic.Diagnostic;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Eventual<P> {
	private final DiagnosticVoidDeferredObject<P>/*<P, Diagnostic, Void>*/ prom = new DiagnosticVoidDeferredObject();

	public void resolve(final P p) {
		for (final DoneCallback<? super P> doneCallback : prom.__doneCallbacks()) {
			if (doneCallback instanceof EventualDoneCallback edc) {
				//throw new UnintendedUseException();
				System.err.println("2121 "+edc);
			}
		}

		prom.resolve(p);
	}

	public void then(final DoneCallback<? super P> cb) {
		prom.then(cb);
	}

	public boolean isResoved() {
		return prom.isResolved();
	}

	public interface EventualDoneCallback<D> extends DoneCallback<D> {}

	public void then(final EventualDoneCallback<? super P> cb) {
		prom.then(cb);
	}

	public void register(final @NotNull EventualRegister ev) {
		ev.register(this);
	}

	public void fail(final Diagnostic d) {
		prom.reject(d);
	}

	public boolean isResolved() {
		return prom.isResolved();
	}

	/**
	 * Please overload this
	 */
	public String description() {
		return "GENERIC-DESCRIPTION";
	}

	public boolean isPending() {
		return prom.isPending();
	}

	public void reject(final Diagnostic aX) {
		System.err.println("8899 [Eventual::reject] "+aX);
	}

	public void onFail(final FailCallback<? super Diagnostic> aO) {
		prom.fail(aO);
	}

	public Optional<P> getOptional() {
		if (!prom.isResolved()) {
			return Optional.empty();
		}
		final @NotNull P[] xx = (P[]) new Object[]{null};
		prom.then(fg -> {
			xx[0] = fg;
		});
		return Optional.of(xx[0]);
	}

	public Optional<P> getOptional(final Supplier<P> s) {
		if (!prom.isResolved()) {
			return Optional.empty();
		}
		final @NotNull P[] xx = (P[]) new Object[]{null};
		prom.then(fg -> {
			xx[0] = s.get();
		});
		return Optional.of(xx[0]);
	}

	@Override
	public String toString() {
		final String[] value = new String[1];
		if (prom.isPending()) {
			value[0] = "<<EMPTY>>";
		} else {
			prom.then(s-> value[0] = ""+s);
		}
		return MoreObjects.toStringHelper(this)
		                  .add("state", prom.state())
		                  .add("value", value[0])
		                  .toString();
	}

	private static class DiagnosticVoidDeferredObject<P> extends DeferredObject<P, Diagnostic, Void> {
		public List<DoneCallback<? super P>> __doneCallbacks() {
			return doneCallbacks;
		}
	}
}
