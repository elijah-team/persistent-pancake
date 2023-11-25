package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;

import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.xlang.LocatableString;

public class CiStringExpression extends CiAbstractExpression {
	public CiStringExpression(final LocatableString aLocatableString) {
		string = aLocatableString.getString();
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.STRING_LITERAL;
	}

	@Override public boolean is_simple() {
		return true;
	}

	@Override
	public CiExpression getLeft() {
		throw new UnintendedUseException();
	}

	@Override
	public void setLeft(final CiExpression CiExpression) {
		throw new IllegalArgumentException("Should use set()");
	}

	@Override
	public String repr_() {return string;}

	public String getText() {
		return Helpers.remove_single_quotes_from_string(string); // TODO wont work with triple quoted string
	}

	@Override
	public String toString() {
		return String.format("<StringExpression %s>", string);
	}

	private String string;
}
