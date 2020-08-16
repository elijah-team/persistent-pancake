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
import org.jetbrains.annotations.Contract;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;


// Referenced classes of package pak2:
//			StatementClosure, VariableSequence, ProcedureCallExpression, Loop, 
//			YieldExpression, IfExpression

public final class AbstractStatementClosure implements StatementClosure, StatementItem {

	private OS_Element _parent;

	public AbstractStatementClosure(Scope aParent) {
		// TODO Auto-generated constructor stub
		parent_scope = aParent;
	}

	public AbstractStatementClosure(final ClassStatement classStatement) {
		// TODO check final member
		_parent = classStatement;
		parent_scope = new Scope() {

			@Override
			public void addDocString(Token s1) {
				classStatement.addDocString(s1);
			}

			@Override
			public void statementWrapper(IExpression aExpr) {
				throw new NotImplementedException();
			}

			@Override
			public StatementClosure statementClosure() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public BlockStatement blockStatement() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public void add(StatementItem aItem) {
				// TODO Auto-generated method stub
//				NotImplementedException.raise();
				classStatement.add((ClassItem) aItem);
			}

			@Override
			public TypeAliasExpression typeAlias() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public InvariantStatement invariantStatement() {
				throw new NotImplementedException();
//				// TODO Auto-generated method stub
//				return null;
			}

			@Override
			public OS_Element getParent() {
				return _parent;
			}

			@Override
			public OS_Element getElement() {
				return _parent;
			}

		};
	}

	public AbstractStatementClosure(Scope scope, OS_Element parent1) {
		parent_scope = scope;
		_parent = parent1;
	}

	@Override
	public BlockStatement blockClosure() {
		bs=new BlockStatement(this.parent_scope);
		add(bs);
		return bs;
	}
	public IExpression constructExpression() {
		ctex=new ConstructExpression(this.parent_scope);
		add(ctex);
		return ctex;
	}
	@Override
	public void constructExpression(IExpression aExpr, FormalArgList aO) {
		add((StatementItem) aExpr);
	}
	@Override
 	public IfConditional ifConditional() {
		ifex=new IfConditional(this.parent_scope);
		add(ifex);
		return ifex;
	}
	@Override
	public Loop loop() {
		loop = new Loop(this.parent_scope.getElement());
		add(loop);
		return loop;
	}
/*
	@Override
	public StatementClosure procCallExpr() {
		pcex=new AbstractStatementClosure(parent); //TODO:
//		add(pcex);
		return pcex;
	}
*/

	@Override
	public ProcedureCallExpression procedureCallExpression() {
		pce=new ProcedureCallExpression();
		add(new StatementWrapper(pce));
		return pce;
	}

	@Override
	public VariableSequence varSeq(Context ctx) {
		vsq=new VariableSequence();
		vsq.setParent(parent_scope.getParent()/*this.getParent()*/); // TODO look at this
		vsq.setContext(ctx);
		return (VariableSequence) add(vsq);
	}

	private OS_Element getParent() {
		return _parent;
	}

	@Override
	public void yield(IExpression aExpr) {
		add(new YieldExpression(aExpr));
	}

	@Contract("_ -> param1")
	private StatementItem add(StatementItem aItem) {
		parent_scope.add(aItem);
		return aItem;
	}
	
	private BlockStatement bs;
	private ConstructExpression ctex;
	private IfConditional ifex;
	
	private Loop loop;
	private ProcedureCallExpression pce;
	private AbstractStatementClosure pcex;
	private VariableSequence vsq;
	private YieldExpression yiex;

	final List<StatementItem> items =new ArrayList<StatementItem>();

	final Scope parent_scope;

	@Override
	public CaseConditional caseConditional() {
		final CaseConditional caseConditional = new CaseConditional(getParent());
		add(caseConditional);
		return caseConditional;
	}

	@Override
	public MatchConditional matchConditional() {
		final MatchConditional matchConditional = new MatchConditional(getParent());
		add(matchConditional);
		return matchConditional;
	}

	@Override
	public void statementWrapper(IExpression expr) {
		parent_scope.statementWrapper(expr);
	}
}

//
//
//
