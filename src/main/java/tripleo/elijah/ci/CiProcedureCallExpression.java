package tripleo.elijah.ci;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.IExpression;
import tripleo.elijah.lang.OS_Type;

// FIXME wrap IExpression and ExpressionList and ExpressionKind too
public interface CiProcedureCallExpression extends IExpression {
	CiExpressionList exprList();

	CiExpressionList getExpressionList();

	@NotNull ExpressionKind getKind();

	IExpression getLeft();

	@Override
	OS_Type getType();

	@Override
	boolean is_simple();

	void identifier(IExpression ee);

	String printableString();

	String repr_();

	@Override
	void setKind(ExpressionKind aIncrement);

	void setExpressionList(CiExpressionList ael);

	void setLeft(IExpression iexpression);

	@Override
	void setType(OS_Type deducedExpression);

	@Override
	String toString();

	void setArgs(CiExpressionList aEl);
}
