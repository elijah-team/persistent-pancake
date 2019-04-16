/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
package tripleo.elijah.lang;

import java.io.IOException;

import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

// Referenced classes of package pak2:
//			ParserClosure, ExpressionList

public class ProcedureCallExpression implements StatementItem, FunctionItem, IBinaryExpression {
	
	private IExpression _left;
	
	public void identifier(Qualident xyz) {
//		target=xyz;
		setLeft(xyz);
	}

	public ExpressionList exprList() {
		return args;
	}
	
//	public Qualident target;
	public ExpressionList args=new ExpressionList();
	
	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		// TODO Auto-generated method stub
		tos.incr_tabs();
		tos.put_string_ln("ProcedureCall {");
		tos.put_string("name = ");
//		target.print_osi(tos);
		getLeft().print_osi(tos);
//		tos.put_string(target.toString());
		tos.put_string("args = ");
		args.print_osi(tos);
		tos.dec_tabs();
		tos.put_string_ln("}");
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		NotImplementedException.raise();
	}

	@Override
	public ExpressionType getType() {
		// TODO Auto-generated method stub
		return ExpressionType.PROCEDURE_CALL;
	}

	@Override
	public void set(ExpressionType aIncrement) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public IExpression getLeft() {
		// TODO fix this
		return _left;
	}

	@Override
	public void setLeft(IExpression iexpression) {
		// TODO fix this
//		target = iexpression;
		_left = iexpression;
	}

	@Override
	public String repr_() {
		// TODO garbage method
		return String.format("%s %s", getLeft(), getRight());
	}

	@Override
	public IExpression getRight() {
		// TODO fix this
		return /* args */null;
	}

	@Override
	public void setRight(IExpression iexpression) {
		// TODO fix this
//		args = iexpression;
	}

	@Override
	public void shift(ExpressionType aType) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void set(IBinaryExpression aEx) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
}
