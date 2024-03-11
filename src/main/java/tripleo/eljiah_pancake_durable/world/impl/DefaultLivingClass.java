package tripleo.eljiah_pancake_durable.world.impl;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedClass;
import tripleo.eljiah_pancake_durable.world.i.LivingClass;

public class DefaultLivingClass implements LivingClass {
	private final ClassStatement _element;
	private final GeneratedClass _gc;

	public DefaultLivingClass(final ClassStatement aElement) {
		_element = aElement;
		_gc      = null;
	}

	public DefaultLivingClass(final @NotNull GeneratedClass aClass) {
		_element = aClass.getKlass();
		_gc      = aClass;
	}

	@Override
	public ClassStatement getElement() {
		return _element;
	}

	@Override
	public int getCode() {
		return _gc.getCode();
	}
}
