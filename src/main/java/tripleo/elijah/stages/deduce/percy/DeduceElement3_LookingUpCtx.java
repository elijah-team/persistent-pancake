package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.lang.OS_Element;

import java.util.function.Consumer;

public interface DeduceElement3_LookingUpCtx {
	void force(OS_Element aElement);

	void onSuccess(Consumer<Void> cb);

	OS_Element getElement();
}
