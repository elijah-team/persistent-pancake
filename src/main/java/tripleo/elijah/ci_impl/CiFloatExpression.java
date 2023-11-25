package tripleo.elijah.ci_impl;

import antlr.Token;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;
import tripleo.elijah.util.UnintendedUseException;

/*
 * Created on May 19, 2019 23:47
 * Created on Nov 24, 2023 23:40:xx
 */
public class CiFloatExpression implements CiExpression {

	final         float carrier;
	private final Token n;

	public CiFloatExpression(final Token n) {
		this.n  = n;
		carrier = Float.parseFloat(n.getText());
	}

	@Override
	public CiExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(final CiExpression aLeft) {
		throw new NotImplementedException(); // TODO
	}

	@Override
	public String repr_() {
		return toString();
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.FLOAT; // TODO
	}

	@Override
	public void setKind(final ExpressionKind aType) {
		// log and ignore
		SimplePrintLoggerToRemoveSoon.println_err2("Trying to set ExpressionType of FloatExpression to " + aType.toString());
	}

	@Override
	public String toString() {
		return String.format("FloatExpression (%f)", carrier);
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	@Override
	public void setType(final OS_BuiltinType aOSBuiltinType) {
		throw new UnintendedUseException();
	}
}
