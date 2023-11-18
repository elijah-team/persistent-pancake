/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.ci_impl;

import antlr.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.ci.*;

/**
 * @author Tripleo
 * <p>
 * Created Apr 15, 2020 at 4:59:21 AM Created 1/8/21 7:19 AM
 */
public class CiIndexingStatementImpl implements CiIndexingStatement {
	private final @NotNull CompilerInstructions parent;
	private CiExpressionList exprs;
	private Token            name;

	public CiIndexingStatementImpl(final @NotNull CompilerInstructions module) {
		this.parent = module;
	}

	@Override
	public void setExprs(final CiExpressionList el) {
		exprs = el;
	}

	@Override
	public void setName(final Token aToken) {
		name = aToken;
	}
}

//
//
//
