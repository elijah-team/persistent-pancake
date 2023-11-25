package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;

public class CiExpressionBuilder {
	public static CiExpression build(final CiExpression aEe, final ExpressionKind aEk, final CiExpression aE2) {
		return new CiBasicBinaryExpression(aEe, aEk, aE2);
	}
}
