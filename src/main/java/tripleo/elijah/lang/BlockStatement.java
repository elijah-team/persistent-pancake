/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

public class BlockStatement implements StatementItem {
	private final FormalArgList    fal = new FormalArgList();
	private final Scope            parent;
	private final NormalTypeName   tn;
	private final StatementClosure scope;

	public BlockStatement(final Scope aParent) {
		parent = aParent;
		scope  = new AbstractStatementClosure(parent);
//		tn     = new RegularTypeName(((AbstractStatementClosure) scope).getParent().getContext());
		tn     = new RegularTypeName(parent.getParent().getContext()); // FIXME 11/18 ??
	}

	public StatementClosure scope() {
		return scope;
	}

	public FormalArgList opfal() {
		return fal;
	}

	public NormalTypeName returnType() {
		return tn;
	}
}
