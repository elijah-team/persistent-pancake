package tripleo.elijah.world.impl;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.world.i.LivingClass;

public class DefaultLivingClass implements LivingClass {
	private final ClassStatement _element;

	public DefaultLivingClass(final ClassStatement aElement) {
		_element = aElement;
	}

	@Override
	public ClassStatement getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return 0;
	}
}
