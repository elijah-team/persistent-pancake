/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.lang.types;

import tripleo.eljiah_pancake_durable.contexts.ClassContext;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.TypeName;

import java.text.MessageFormat;


/**
 * Created 7/8/21 6:00 AM
 */
public class OS_GenericTypeNameType extends __Abstract_OS_Type {

	private final ClassContext.OS_TypeNameElement genericTypename;

	public OS_GenericTypeNameType(final ClassContext.OS_TypeNameElement aGenericTypename) {
		genericTypename = aGenericTypename;
	}

	@Override
	public OS_Element getElement() {
		return genericTypename;
	}

	@Override
	public Type getType() {
		return Type.GENERIC_TYPENAME;
	}

	public TypeName getRealTypeName() {
		return genericTypename.getTypeName();
	}

	@Override
	public String asString() {
		return MessageFormat.format("<OS_GenericTypeNameType {0}>", genericTypename);
	}

	protected boolean _isEqual(final OS_Type aType) {
		return aType.getType() == Type.GENERIC_TYPENAME && genericTypename.equals(((OS_GenericTypeNameType) aType).genericTypename);
	}
}


//
//
//
