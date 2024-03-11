package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jdeferred2.DoneCallback;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;

public class DeduceTypeWatcher {
	private DoneCallback<GenType> typeCallback;
	private OS_Element            _element;

	public void onType(final DoneCallback<GenType> aTypeCallback) {
		typeCallback = aTypeCallback;
	}

	public void setType(final GenType gt) {
		typeCallback.onDone(gt);
	}

	public OS_Element element() {
		return _element;
	}

	public void element(final OS_Element e) {
		_element = e;
	}
}
