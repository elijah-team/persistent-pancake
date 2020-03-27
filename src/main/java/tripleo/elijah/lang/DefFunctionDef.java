/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 * 
 * The contents of this library are released under the LGPL licence v3, 
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 * 
 */
/*
 * Created on Aug 30, 2005 8:43:27 PM
 * 
 * $Id$
 */
package tripleo.elijah.lang;

import antlr.Token;
import tripleo.elijah.contexts.DefFunctionContext;
import tripleo.elijah.gen.ICodeGen;
import tripleo.elijah.gen.java.JavaCodeGen;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.TabbedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefFunctionDef implements ClassItem {
	public static final int DEF_FUN = 1;
	public static final int REG_FUN = 0;
	
	private IExpression _expr;
	private int _type;
	private FormalArgList fal;
	
	public void setExpr(IExpression aExpr) {
		_expr=aExpr;
	}
	
	public void setType(int aType) {
		_type = aType;
	}
	
	public void setOpfal(FormalArgList fal) {
		this.fal=fal;
	}
	
	static class StatementWrapper implements StatementItem, FunctionItem {

		private IExpression expr;

		public StatementWrapper(IExpression aexpr) {
			expr = aexpr;
		}

		@Override
		public void print_osi(TabbedOutputStream aTos) throws IOException {
			// TODO Auto-generated method stub
			int y=2;
			if (expr instanceof AbstractBinaryExpression) {
				AbstractBinaryExpression abe = (AbstractBinaryExpression)expr;
				if (abe.getKind() == ExpressionKind.ASSIGNMENT) {
					aTos.put_string_ln("Assignment {");
					aTos.incr_tabs();
					aTos.put_string_ln(abe.getLeft().toString());
					aTos.put_string_ln(abe.getRight().toString());
					aTos.dec_tabs();
					aTos.put_string_ln("}");
				} else if (abe.getKind() == ExpressionKind.AUG_MULT) {
					aTos.put_string_ln("AssignmentMultiplication {");
					aTos.incr_tabs();
					aTos.put_string_ln(abe.getLeft().toString());
					aTos.put_string_ln(abe.getRight().toString());
					aTos.dec_tabs();
					aTos.put_string_ln("}");
				}
			}
		}

		@Override
		public void visitGen(ICodeGen visit) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public OS_Element getParent() {
			// TODO this is unimplemented
			NotImplementedException.raise();
			return null;
		}

		@Override
		public Context getContext() {
			// TODO Auto-generated method stub
			return null;
		}

	
	}
	
	private final class FunctionDefScope implements Scope {

		private final AbstractStatementClosure asc = new AbstractStatementClosure(this);

		@Override
		public void add(StatementItem aItem) {
			if (aItem instanceof FunctionItem)
				items.add((FunctionItem) aItem);
			else
				System.err.println(String.format("adding false StatementItem %s",
					aItem.getClass().getName()));
		}
		
		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}
		
		@Override
		public void addDocString(Token aS) {
			docstrings.add(aS.getText());
		}

		@Override
		public BlockStatement blockStatement() {
			return new BlockStatement(this);
		}

		@Override
		public StatementClosure statementClosure() {
			return asc;
		}

		@Override
		public void statementWrapper(IExpression aExpr) {
			add(new StatementWrapper(aExpr));
//			throw new NotImplementedException(); // TODO
		}
	}

	private List<String> docstrings = new ArrayList<String>(); // TODO do we allow this?
	public String funName;
	private List<FunctionItem> items = new ArrayList<FunctionItem>();
	private final FormalArgList mFal = new FormalArgList();
//	private FunctionDefScope mScope;
	private OS_Element/*ClassStatement*/ parent;
	private final FunctionDefScope mScope2 = new FunctionDefScope();
	private TypeName _returnType = new RegularTypeName();
	private Attached _a = new Attached(new DefFunctionContext(this));

	public DefFunctionDef(OS_Element aStatement) {
		parent = aStatement;
		if (aStatement instanceof ClassStatement) {
			((ClassStatement)parent).add(this);
		} else {
			System.err.println("adding FunctionDef to "+aStatement.getClass().getName());
		}
	}

	public FormalArgList fal() {
		return mFal;
	}

	@Override
	public void print_osi(TabbedOutputStream tos) throws IOException {
		System.out.println("Function print_osi");
		tos.put_string("Function (");
		tos.put_string(funName);
		tos.put_string_ln(") {");
		tos.put_string_ln("//");
		tos.incr_tabs();
		for (FunctionItem item : items) {
			item.print_osi(tos);
		}
		tos.dec_tabs();
		tos.put_string_ln((String.format("} // function %s",  funName)));
	}

	public Scope scope() {
		//assert mScope == null;
		return mScope2;
	}

	public void setName(Token aText) {
		funName = aText.getText();
	}

	public void visit(JavaCodeGen gen) {
		// TODO Auto-generated method stub
		for (FunctionItem element : items)
			gen.addFunctionItem(element);
	}

	@Override
	public void visitGen(ICodeGen visit) {
		// TODO Auto-generated method stub
		
	}

	public TypeName returnType() {
		// TODO Auto-generated method stub
		return _returnType ;
	}

	@Override
	public OS_Element getParent() {
		return parent;
	}

	@Override
	public Context getContext() {
		return _a ._context;
	}

	
}
