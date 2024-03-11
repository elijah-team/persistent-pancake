/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.lang.builder;

import tripleo.eljiah_pancake_durable.lang.IExpression;

/**
 * Created 5/8/21 6:13 AM
 */
public class BaseScope2 extends BaseScope {
	@Override
	public void statementWrapper(final IExpression expr) {
		final StatementWrapperBuilder swb = new StatementWrapperBuilder(expr);
		bs.add(swb);
	}
}

//
//
//
