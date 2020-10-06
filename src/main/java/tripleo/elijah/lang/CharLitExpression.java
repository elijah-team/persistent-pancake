/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Mar 27, 2019 at 2:20:38 PM
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

/**
 * @author Tripleo(sb)
 *
 */
public class CharLitExpression implements IExpression {

	private final Token char_lit_raw;

	public CharLitExpression(Token c) {
		// TODO Auto-generated constructor stub
		char_lit_raw = c;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#getType()
	 */
	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.CHAR_LITERAL;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#set(tripleo.elijah.lang.ExpressionType)
	 */
	@Override
	public void setKind(ExpressionKind aIncrement) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#getLeft()
	 */
	@Override
	public IExpression getLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#setLeft(tripleo.elijah.lang.IExpression)
	 */
	@Override
	public void setLeft(IExpression iexpression) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see tripleo.elijah.lang.IExpression#repr_()
	 */
	@Override
	public String repr_() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	OS_Type _type;

	@Override
	public void setType(OS_Type deducedExpression) {
		_type = deducedExpression;
    }

	@Override
	public OS_Type getType() {
    	return _type;
	}

}

//
//
//
