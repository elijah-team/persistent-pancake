/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 */
package tripleo.elijah.ci_impl;

import antlr.Token;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.xlang.LocatableString;

/**
 * Created Mar 27, 2019 at 2:20:38 PM
 *
 * @author Tripleo(sb)
 */
public class CiCharLitExpression implements CiExpression {
	private final LocatableString char_lit_raw;

	public CiCharLitExpression(final Token c) {
		char_lit_raw = LocatableString.of(c);
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.CHAR_LITERAL;
	}

	@Override
	public void setKind(final ExpressionKind aIncrement) {
		throw new UnintendedUseException();
	}

	@Override
	public CiExpression getLeft() {
		throw new UnintendedUseException();
	}

	@Override
	public void setLeft(final CiExpression CiExpression) {
		throw new UnintendedUseException();
	}

	@Override
	public String repr_() {
		return String.format("<CharLitExpression %s>", char_lit_raw);
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	@Override
	public String toString() {
		return Helpers.remove_single_quotes_from_string(char_lit_raw.getString());
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}

//
//
//
