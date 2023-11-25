package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.UnintendedUseException;

/**
 * Created 8/6/20 1:15 PM
 */
public class CiSetItemExpression extends CiBasicBinaryExpression implements CiExpression {
	public CiSetItemExpression(final CiGetItemExpression left_, final CiExpression right_) {
		this.setLeft(left_);
		this.setRight(right_);
		this._kind = ExpressionKind.SET_ITEM;
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}
