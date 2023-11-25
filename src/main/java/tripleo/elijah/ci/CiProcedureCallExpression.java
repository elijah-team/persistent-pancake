package tripleo.elijah.ci;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.OS_Type;

// FIXME wrap CiExpression and ExpressionList and ExpressionKind too
public interface CiProcedureCallExpression extends CiExpression {
	CiExpressionList exprList();

	CiExpressionList getExpressionList();

	@NotNull ExpressionKind getKind();

	CiExpression getLeft();

	@Override
	boolean is_simple();

	void identifier(CiExpression ee);

	String printableString();

	String repr_();

	@Override
	void setKind(ExpressionKind aIncrement);

	void setExpressionList(CiExpressionList ael);

	void setLeft(CiExpression iexpression);

	@Override
	String toString();

	void setArgs(CiExpressionList aEl);
}
