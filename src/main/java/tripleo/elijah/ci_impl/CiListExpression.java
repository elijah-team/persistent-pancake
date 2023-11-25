/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 */
package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.ci.CiExpressionList;
import tripleo.elijah.diagnostic.Locatable;

import java.io.File;

/*
 * Created on Sep 1, 2005 8:28:55 PM
 */
public class CiListExpression extends CiAbstractExpression implements Locatable, CiExpression {
	CiExpressionList contents;

	public void setContents(final CiExpressionList aList) {
		contents = aList;
	}

	@Override
	public boolean is_simple() {
		return false;
	}

//	public final Syntax syntax = new Syntax();
//
//	public class Syntax {
//		Token startToken;
//		Token endToken;
//		final List<Token> commas = new ArrayList<Token>();
//
//		public void start_and_end(final Token startToken, final Token endToken) {
//			this.startToken = startToken;
//			this.endToken   = endToken;
//		}
//
//		public void comma(final Token t) {
//			commas.add(t);
//		}
//	}

	@Override
	public int getLine() {
//		if (syntax.startToken != null)
//			return syntax.startToken.getLine();
		return 0;
	}

	@Override
	public int getColumn() {
//		if (syntax.startToken != null)
//			return syntax.startToken.getColumn();
		return 0;
	}

	@Override
	public int getLineEnd() {
//		if (syntax.endToken != null)
//			return syntax.endToken.getLine();
		return 0;
	}

	@Override
	public int getColumnEnd() {
//		if (syntax.endToken != null)
//			return syntax.endToken.getColumn();
		return 0;
	}

	@Override
	public File getFile() {
//		if (syntax.startToken != null) {
//			final String filename = syntax.startToken.getFilename();
//			if (filename != null)
//				return new File(filename);
//		}
		return null;
	}
}
