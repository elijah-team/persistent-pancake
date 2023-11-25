package tripleo.elijah.ci_impl;

import org.jetbrains.annotations.NotNull;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.ci.CiExpressionList;
import tripleo.elijah.ci.CiProcedureCallExpression;

import tripleo.elijah.lang.ExpressionKind;

import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.UnintendedUseException;

public class CiProcedureCallExpressionImpl implements CiProcedureCallExpression {
	private CiExpression      _left;
	private CiExpressionList expressionList = new CiExpressionListImpl();

	/**
	 * Get the argument list
	 *
	 * @return the argument list
	 */
	@Override
	public CiExpressionList exprList() {
		return expressionList;
	}

	@Override
	public CiExpressionList getExpressionList() {
		return expressionList;
	}

	/**
	 * change then argument list all at once
	 *
	 * @param ael the new value
	 */
	@Override
	public void setExpressionList(final CiExpressionList ael) {
		expressionList = ael;
	}

	@Override
	public @NotNull ExpressionKind getKind() {
		return ExpressionKind.PROCEDURE_CALL;
	}

	public void setKind(final ExpressionKind aIncrement) {
		throw new UnintendedUseException();
	}

	@Override
	public CiExpression getLeft() {
		return _left;
	}

	/**
	 * @see #identifier()
	 */
	@Override
	public void setLeft(final CiExpression iexpression) {
		_left = iexpression;
	}

	public @NotNull String getReturnTypeString() {
		return "int"; // TODO hardcoded
	}

	@Override
	public boolean is_simple() {
		throw new UnintendedUseException();
	}

	/**
	 * Set the left hand side of the procedure call expression, ie the method name
	 *
	 * @param xyz a method name might come as DotExpression or IdentExpression
	 */
	@Override
	public void identifier(final CiExpression xyz) {
		setLeft(xyz);
	}

	@Override
	public String printableString() {
		return String.format("%s%s", getLeft(), expressionList != null ? expressionList.toString() : "()");
	}

	@Override
	public String repr_() {
		String s = expressionList != null ? expressionList.toString() : "()";
		return "ProcedureCallExpression{%s %s}".formatted(getLeft(), s);
	}

	@Override
	public String toString() {
		return repr_();
	}

	@Override
	public void setArgs(CiExpressionList aEl) {
		throw new UnintendedUseException();
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}
