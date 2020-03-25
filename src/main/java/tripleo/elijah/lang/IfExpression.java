/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import antlr.Token;

import java.util.ArrayList;
import java.util.List;

public class IfExpression implements StatementItem {
	
	private class IfExpressionScope implements Scope {
		@Override
		public void add(StatementItem aItem) {
			parent.add(aItem);
		}
		
		@Override
		public void addDocString(Token s) {
			parent.addDocString(s);
		}
		
		@Override
		public BlockStatement blockStatement() {
			return parent.blockStatement(); // TODO
		}
		
		@Override
		public InvariantStatement invariantStatement() {
			return null;
		}
		
		@Override
		public StatementClosure statementClosure() {
			return new AbstractStatementClosure(this); // TODO
		}
		
		@Override
		public void statementWrapper(IExpression aExpr) {
			parent.add(new FunctionDef.StatementWrapper(aExpr));
		}
		
		@Override
		public TypeAliasExpression typeAlias() {
			return null;
		}
	}
	
	private int order = 0;
	
	private Scope parent;
	private final IfExpression sibling;
//	private final IfExpression if_parent;

	private IExpression expr;
	private List<IfExpression> parts = new ArrayList<IfExpression>();

	public IfExpression(IfExpression ifExpression) {
		this.sibling = ifExpression;
		this.order = ++sibling/*if_parent*/.order;
	}

	public IfExpression(Scope aClosure) {
		this.parent = aClosure;
		this.sibling = null; // top
	}

	public IfExpression else_() {
		IfExpression elsepart = new IfExpression(this);
		parts.add(elsepart);
		return elsepart;
	}

	public IfExpression elseif() {
		IfExpression elseifpart = new IfExpression(this);
		parts.add(elseifpart);
		return elseifpart;
	}
	
	/** 
	 * will not be null during if or elseif
	 * 
	 * @param expr
	 */
	public void expr(IExpression expr) {
		this.expr = expr;
	}
	
	/**
	 * will always be nonnull
	 * 
	 */
	public Scope scope() {
		return new IfExpressionScope();
	}
}

//
//
//
