/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.ci_impl;

import tripleo.elijah.ci.CiExpressionList;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.lang.ExpressionList;
import tripleo.elijah.xlang.LocatableString;

/**
 * @author Tripleo
 * <p>
 * Created 	Apr 15, 2020 at 4:59:21 AM
 * Created 1/8/21 7:19 AM
 */
public class IndexingStatement {

	private final CompilerInstructions parent;
	private LocatableString  name;
	private CiExpressionList exprs;

	public IndexingStatement(final CompilerInstructions module) {
		this.parent = module;
	}

	public void setName(final LocatableString i1) {
		this.name = i1;
	}

	public void setExprs(final CiExpressionList el) {
		this.exprs = el;
	}

}

//
//
//
