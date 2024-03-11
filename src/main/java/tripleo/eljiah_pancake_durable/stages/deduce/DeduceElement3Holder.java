package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jetbrains.annotations.Contract;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.IDeduceElement3;
import tripleo.eljiah_pancake_durable.stages.gen_fn.IElementHolder;

public class DeduceElement3Holder implements IElementHolder {
	private final IDeduceElement3 element;

	@Contract(pure = true)
	public DeduceElement3Holder(final IDeduceElement3 aElement) {
		element = aElement;
	}

	@Override
	public OS_Element getElement() {
		return element.getPrincipal();
	}

}
