package tripleo.elijah.ci;

import org.jetbrains.annotations.*;
import tripleo.elijah.lang.IExpression;

import java.util.*;

public interface CiExpressionList {
	void add(IExpression aExpr);

	@NotNull Collection<IExpression> expressions();

	@NotNull Iterator<IExpression> iterator();

	@NotNull IExpression next(/*@NotNull*/ IExpression aExpr);

	@Override
	String toString();
}
