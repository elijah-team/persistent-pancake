package tripleo.eljiah_pancake_durable.world.impl;

import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.world.i.LivingFunction;

public class DefaultLivingFunction implements LivingFunction {
	private final BaseFunctionDef       _element;
	private final BaseGeneratedFunction _gf;

	public DefaultLivingFunction(final BaseFunctionDef aElement) {
		_element = aElement;
		_gf      = null;
	}

	public DefaultLivingFunction(final BaseGeneratedFunction aFunction) {
		_element = aFunction.getFD();
		_gf      = aFunction;
	}

	@Override
	public BaseFunctionDef getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return _gf.getCode();
	}
}
