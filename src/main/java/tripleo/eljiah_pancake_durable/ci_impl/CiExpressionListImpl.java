package tripleo.eljiah_pancake_durable.ci_impl;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.ci.CiExpressionList;
import tripleo.eljiah_pancake_durable.lang.IExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
