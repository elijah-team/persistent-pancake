package tripleo.elijah.ci_impl;

import antlr.Token;

import tripleo.elijah.ci.CiExpression;

import tripleo.elijah.lang.ExpressionKind;

import tripleo.elijah.lang.types.OS_BuiltinType;

import tripleo.elijah.util.UnintendedUseException;

public class CiGetItemExpression extends  CiAbstractExpression implements CiExpression {

	public final CiExpression index; // TODO what about multidimensional arrays?

	public CiGetItemExpression(final CiExpression ee, final CiExpression expr) {
		this.left    = ee;
		this.index   = expr;
		this._kind   = ExpressionKind.GET_ITEM;
	}

	@Override
	public String repr_() {
		throw new UnintendedUseException();
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.GET_ITEM;
	}

	@Override
	public void setKind(final ExpressionKind aKind) {
		throw new UnintendedUseException();
	}

	@Override
	public CiExpression getLeft() {
		throw new UnintendedUseException();
	}

	@Override
	public void setLeft(final CiExpression iexpression) {
		throw new UnintendedUseException();
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}

	public void parens(final Token lb, final Token rb) {
		// TODO implement me later
		throw new UnintendedUseException();
	}


}
