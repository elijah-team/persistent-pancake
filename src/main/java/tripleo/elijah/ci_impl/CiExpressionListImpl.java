package tripleo.elijah.ci_impl;

import com.google.common.base.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.ci.*;
import tripleo.elijah.lang.IExpression;


import java.util.*;

public class CiExpressionListImpl implements CiExpressionList {
	private final List<IExpression> exprs = new ArrayList<>();

	@Override
	public void add(final IExpression aExpr) {
		exprs.add(aExpr);
	}

	@Override
	public @NotNull Collection<IExpression> expressions() {
		return exprs;
	}

	@Override
	public @NotNull Iterator<IExpression> iterator() {
		return exprs.iterator();
	}

	@Override
	public @NotNull IExpression next(final IExpression aExpr) {
		Preconditions.checkNotNull(aExpr);

		if (aExpr != null) {
			add(aExpr);
			return aExpr;
		} else {
			throw new IllegalArgumentException("expression cannot be null");
		}
	}

	@Override
	public String toString() {
		return exprs.toString();
	}

	public int size() {
		return exprs.size();
	}
}
