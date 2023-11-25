package tripleo.elijah.ci;

import org.jetbrains.annotations.Contract;
import tripleo.elijah.ci_impl.CiBasicBinaryExpression;
import tripleo.elijah.ci_impl.CiCharLitExpression;
import tripleo.elijah.ci_impl.CiFloatExpression;
import tripleo.elijah.ci_impl.CiNumericExpression;
import tripleo.elijah.ci_impl.CiStringExpression;
import tripleo.elijah.lang.ExpressionKind;
import tripleo.elijah.lang.types.OS_BuiltinType;

/*
 * Created on Sep 2, 2005 14:08:03
 * Created on Nov 24, 2023 23:40:xx
 */
public interface CiExpression {

	static boolean isConstant(final CiExpression expression) {
		return expression instanceof CiStringExpression ||
		  expression instanceof CiCharLitExpression ||
		  expression instanceof CiFloatExpression ||
		  expression instanceof CiNumericExpression;
	}

	CiExpression UNASSIGNED = new CiBasicBinaryExpression() {
		@Override
		public String toString() {
			return "<UNASSIGNED expression>";
		}
	};

	@Deprecated String repr_();

	@Contract(pure = true)
	ExpressionKind getKind();

	void setKind(ExpressionKind aKind);

	CiExpression getLeft();

	void setLeft(CiExpression iexpression);

	boolean is_simple();

	void setType(OS_BuiltinType aOSBuiltinType);
}
