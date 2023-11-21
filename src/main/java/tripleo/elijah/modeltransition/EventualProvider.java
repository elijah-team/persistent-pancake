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
	public void provide(T t) {
		prom.resolve(t);
	}
}
