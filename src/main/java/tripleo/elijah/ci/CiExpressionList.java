package tripleo.elijah.ci;

import org.jetbrains.annotations.*;

import java.util.*;

public interface CiExpressionList {
	void add(CiExpression aExpr);

	@NotNull Collection<CiExpression> expressions();

	@NotNull Iterator<CiExpression> iterator();

	@NotNull CiExpression next(/*@NotNull*/ CiExpression aExpr);

	@Override
	String toString();
}
