package tripleo.eljiah_pancake_durable.lang.types;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.TypeName;

import java.text.MessageFormat;


public class OS_UserType extends __Abstract_OS_Type {
	private final TypeName typeName;

	public OS_UserType(final TypeName aTypeName) {
		typeName = aTypeName;
	}

	@Override
	public OS_Element getElement() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.USER;
	}

	@Override
	public OS_Type resolve(final Context ctx) {
		assert ctx != null;

		final LookupResultList r    = ctx.lookup(getTypeName().toString()); // TODO
		final OS_Element       best = r.chooseBest(null);
		return ((ClassStatement) best).getOS_Type();
	}

	@Override
	public TypeName getTypeName() {
		return typeName;
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.USER && typeName.equals(aType.getTypeName());
	}

	@Override
	public String asString() {
		return MessageFormat.format("<OS_UserType {0}>", typeName);
	}

}

