/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang2;

import tripleo.elijah.lang.ExpressionKind;

/**
 * Created 10/2/20 10:16 AM
 */
public class SpecialFunctions {
	public static String of(final ExpressionKind kind) {
		switch (kind) {
		case LT_: 				return "__lt__";
		case GT: 				return "__gt__";
		case INCREMENT: 		return "__preinc__";
		case AUG_MULT:			return "__imult__";
		case ASSIGNMENT:		return "__assign__";
		case GET_ITEM:			return "__getitem__";
		default:
			throw new IllegalStateException("Unexpected value: " + kind);
		}
	}

	public static String reverse_name(final String pn) {
		if (pn.equals("__gt__")) // README  explicitly disallow
			return null;
		return null;
	}
}

//
//
//
