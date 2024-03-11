/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.lang;

import tripleo.elijah.diagnostic.Locatable;
import tripleo.elijah_pancake.sep1011.lang.EN_Name;
import tripleo.elijah_pancake.sep1011.lang._TypeName__Nameable;

/**
 * Created 8/16/20 2:16 AM
 */
public interface TypeName extends Locatable, _TypeName__Nameable {
	boolean isNull();

	void setContext(Context context);

	Context getContext();

	default String asString() {
		return toString();
	}

	@Override EN_Name getEnName();

	enum Type {
		NORMAL, GENERIC, TYPE_OF, FUNCTION
	}

	Type kindOfType();

	@Override
	boolean equals(Object o);

	enum Nullability {
		NOT_SPECIFIED, NEVER_NULL, NULLABLE
	}
}

//
//
//
