package tripleo.elijah.ci_impl;

import antlr.Token;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.xlang.LocatableString;

public class CiStringExpression_ {
	public static CiStringExpression of(final String aS) {
		return new CiStringExpression(LocatableString.of(aS));
	}

	public static CiExpression of(final Token aS) {
		return new CiStringExpression(LocatableString.of(aS));
	}
}
