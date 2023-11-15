/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import tripleo.elijah.util.NotImplementedException;

// TODO is ExpressionList an IExpression?
// TODO Is ProcedureCallExpression a StatementItem?
public class ProcedureCallExpression implements StatementItem, /*FunctionItem,*/ IExpression {
	private ExpressionList args = new ExpressionList();

	private IExpression _left;
	private OS_Type _type;

	/**
	 * Set  the left hand side of the procedure call expression, ie the method name
	 *
	 * @param xyz a method name in Qualident form (might come as DotExpression in future)
	 */
	public void identifier(final Qualident xyz) {
		setLeft(xyz);
	}

	/**
	 * Set  the left hand side of the procedure call expression, ie the method name
	 *
	 * @param xyz a method name might come as DotExpression or IdentExpression
	 */
	public void identifier(final IExpression xyz) {
		setLeft(xyz);
	}

//	@Override
//	public void visitGen(ICodeGen visit) {
//		// TODO Auto-generated method stub
//		NotImplementedException.raise();
//	}

	/**
	 * Get the argument list
	 *
	 * @return the argument list
	 */
	public ExpressionList exprList() {
		return args;
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.PROCEDURE_CALL;
	}

	@Override
	public void setKind(final ExpressionKind aIncrement) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		return _left;
	}

	/**
	 * @see #identifier(Qualident)
	 */
	@Override
	public void setLeft(final IExpression iexpression) {
		_left = iexpression;
	}

	@Override
	public String repr_() {
		return toString();
	}

	public String getReturnTypeString() {
		return "int"; // TODO hardcoded
	}

	public OS_Element getParent() {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("ProcedureCallExpression{%s %s}", getLeft(), args != null ? args.toString() : "()");
	}

	@Override
	public boolean is_simple() {
		return false; // TODO is this correct?
	}

	public ExpressionList getArgs() {
		assert args != null;
		return args;
	}

	/**
	 * change then argument list all at once
	 *
	 * @param ael the new value
	 */
	public void setArgs(final ExpressionList ael) {
		if (ael == null) {
//			throw new AssertionError();
		} else {
			args = ael;
		}
	}

	@Override
	public OS_Type getType() {
		return _type;
	}

	@Override
	public void setType(final OS_Type deducedExpression) {
		_type = deducedExpression;
	}

}

//
//
//
