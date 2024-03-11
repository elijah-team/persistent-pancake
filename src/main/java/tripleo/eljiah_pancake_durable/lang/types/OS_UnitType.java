package tripleo.eljiah_pancake_durable.lang.types;

import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;


public class OS_UnitType extends __Abstract_OS_Type {

	@Override
	public OS_Element getElement() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.UNIT_TYPE;
	}

	@Override
	public boolean isUnitType() {
		return true;
	}

	@Override
	public String toString() {
		return "<UnitType>";
	}
	@Override
	public String asString() {
		return "<OS_UnitType>";
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.UNIT_TYPE;
	}
}


