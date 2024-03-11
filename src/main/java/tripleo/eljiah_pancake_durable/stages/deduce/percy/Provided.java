package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import org.jdeferred2.DoneCallback;

public interface Provided<T> {
	void on(DoneCallback<T> t);

	void provide(T pwc);

	boolean has();

	T get();
}
