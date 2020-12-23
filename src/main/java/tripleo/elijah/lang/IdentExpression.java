/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/**
 * Created Apr 1, 2019 at 3:21:26 PM
 *
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

/**
 * @author Tripleo(sb)
 *
 */
public class IdentExpression implements IExpression, OS_Element, Resolvable {

	private Token text;
	public  Attached _a;
	private OS_Element _resolvedElement;

	public IdentExpression(final Token r1) {
		this.text = r1;
		this._a = new Attached();
	}

	public IdentExpression(final Token r1, final Context cur) {
		this.text = r1;
		this._a = new Attached();
		setContext(cur);
	}

	@Override
	public ExpressionKind getKind() {
		return ExpressionKind.IDENT;
	}

	/**
	 * same as getText()
	 */
	@Override
	public String toString() {
		return getText();
	}

	@Override
	public void setKind(final ExpressionKind aIncrement) {
		// log and ignore
		System.err.println("Trying to set ExpressionType of IdentExpression to "+aIncrement.toString());
	}

	@Override
	public IExpression getLeft() {
		return this;
	}

	@Override
	public void setLeft(final IExpression iexpression) {
//		if (iexpression instanceof IdentExpression) {
//			text = ((IdentExpression) iexpression).text;
//		} else {
//			// NOTE was System.err.println
			throw new IllegalArgumentException("Trying to set left-side of IdentExpression to " + iexpression.toString());
//		}
	}

	@Override
	public String repr_() {
		return String.format("IdentExpression(%s %d)", text.getText(), _a.getCode());
	}

	public String getText() {
		return text.getText();
	}

	@Override
	public boolean is_simple() {
		return true;
	}

	@Override
	public void visitGen(final ICodeGen visit) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public OS_Element getParent() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
//		return null;
	}

	@Override
	public Context getContext() {
		return _a.getContext();
	}

	OS_Type _type;

	@Override
	public void setType(final OS_Type deducedExpression) {
		_type = deducedExpression;
    }

	@Override
	public OS_Type getType() {
    	return _type;
	}

	public void setContext(final Context cur) {
		_a.setContext(cur);
	}

	@Override
	public boolean hasResolvedElement() {
		return _resolvedElement != null;
	}

	@Override
	public OS_Element getResolvedElement() {
		return _resolvedElement;
	}

	@Override
	public void setResolvedElement(final OS_Element element) {
		_resolvedElement = element;
	}

	public static IdentExpression forString(final String string) {
		return new IdentExpression(Helpers.makeToken(string));
	}

	public Token token() {
		return text;
	}
}

//
//
//
