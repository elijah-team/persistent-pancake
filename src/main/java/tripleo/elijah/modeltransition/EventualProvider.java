package tripleo.elijah.modeltransition;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.Eventual;
import tripleo.elijah.stages.deduce.percy.Provided;

public class EventualProvider<T> implements Provided<T> {
	private final Eventual<T> prom = new Eventual<>();

	@Override
	public void on(final DoneCallback<T> cb) {
		prom.then(cb);
	}

	@Override
	public void provide(final T t) {
		prom.resolve(t);
	}

	// FIXME 11/21 remove these three
	@Override
	public boolean has() {
		return prom.isResolved();
	}

	@Override
	public T get() {
		final Object[] result = new Object[1];
		prom.then(result1 -> result[0] =result1);
		//noinspection unchecked
		return (T) result[0];
	}
}
