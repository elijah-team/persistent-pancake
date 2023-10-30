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
import tripleo.elijah.lang.IExpression;


import java.util.*;

/**
 * Created 9/6/20 12:04 PM
 */
public class GenerateStatementImpl implements GenerateStatement {
	public class Directive {

		private final IExpression expression;
		private final String name;

		public Directive(final @NotNull Token token_, final IExpression expression_) {
			name = token_.getText();
			expression = expression_;
		}

		public IExpression getExpression() {
			return expression;
		}

		public String getName() {
			return name;
		}
	}

	public final List<Directive> dirs = new ArrayList<Directive>();

	@Override
	public void addDirective(final @NotNull Token token, final IExpression expression) {
		dirs.add(new Directive(token, expression));
	}
}

//
//
//
