package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;

public class CiAbstractExpression implements CiExpression {
	public CiExpression    left;
	public ExpressionKind _kind;
	private OS_BuiltinType ty;

	public CiAbstractExpression() {
		left  = null;
		_kind = null;
	}

	public CiAbstractExpression(final CiExpression aLeft, final ExpressionKind aType) {
		left  = aLeft;
		_kind = aType;
	}

	@Override
	public ExpressionKind getKind() {
		return _kind;
	}

	@Override
	public void setKind(final ExpressionKind type1) {
		_kind = type1;
	}

	@Override
	public CiExpression getLeft() {
		return left;
	}

	@Override
	public String repr_() {
		return String.format("<Expression %s %s>", left, _kind);
	}

	@Override
	public boolean is_simple() {
		return true; // 11/24 !!
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		ty = aOSBuiltinType;
	}

	@Override
	public void setLeft(final CiExpression aLeft) {
		left = aLeft;
	}
}
