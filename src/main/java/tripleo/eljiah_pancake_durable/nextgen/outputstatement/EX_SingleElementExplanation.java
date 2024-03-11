package tripleo.eljiah_pancake_durable.nextgen.outputstatement;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.lang.OS_Element;

public class EX_SingleElementExplanation implements EX_Explanation {

	private final @NotNull OS_Element _element;

	@Contract(pure = true)
	public EX_SingleElementExplanation(final OS_Element aElement) {
		_element = aElement;
	}

	public @NotNull OS_Element getElement() {
		return _element;
	}
}
