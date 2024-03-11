package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import tripleo.eljiah_pancake_durable.lang.OS_Element;

import java.util.function.Consumer;

public interface DeduceElement3_LookingUpCtx {
	void force(OS_Element aElement);

	void onSuccess(Consumer<Void> cb);

	OS_Element getElement();
}
