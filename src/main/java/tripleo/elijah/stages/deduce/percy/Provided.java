package tripleo.elijah.stages.deduce.percy;

import org.jdeferred2.DoneCallback;

public interface Provided<T> {
	void on(DoneCallback<T> t);

	void provide(PercyWantConstructor pwc);
}
