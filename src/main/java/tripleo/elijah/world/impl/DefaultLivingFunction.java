package tripleo.elijah.world.impl;

import tripleo.elijah.lang.BaseFunctionDef;
import tripleo.elijah.world.i.LivingFunction;

public class DefaultLivingFunction implements LivingFunction {
	private final BaseFunctionDef _element;

	public DefaultLivingFunction(final BaseFunctionDef aElement) {
		_element = aElement;
	}

	@Override
	public BaseFunctionDef getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return 0;
	}
}
