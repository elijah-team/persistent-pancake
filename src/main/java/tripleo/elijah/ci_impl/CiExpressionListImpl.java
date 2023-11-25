package tripleo.elijah.ci_impl;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.ci.CiExpressionList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CiExpressionListImpl implements CiExpressionList {
	private final List<CiExpression> exprs = new ArrayList<>();

	@Override
	public void add(final CiExpression aExpr) {
		exprs.add(aExpr);
	}

	@Override
	public @NotNull Collection<CiExpression> expressions() {
		return exprs;
	}

	@Override
	public @NotNull Iterator<CiExpression> iterator() {
		return exprs.iterator();
	}

	@Override
	public @NotNull CiExpression next(final CiExpression aExpr) {
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
