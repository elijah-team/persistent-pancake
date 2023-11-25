package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;

/**
 * Created 	Mar 27, 2020 at 12:59:41 AM
 *
 * @author Tripleo(envy)
 */
public class CiDotExpression extends CiBasicBinaryExpression {
	public CiDotExpression(final CiExpression ee, final CiIdentExpression identExpression) {
		left = ee;
		right = identExpression;
		_kind = ExpressionKind.DOT_EXP;
	}

	@Override
	public String toString() {
		return String.format("%s.%s", left, right);
	}

	@Override
	public boolean is_simple() {
		return false; // TODO when is this true or not? see {@link Qualident}
	}
}
