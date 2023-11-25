package tripleo.elijah.ci;

public interface CiBinaryExpression extends CiExpression {
	CiExpression getRight();

	void setRight(CiExpression iexpression);
}
