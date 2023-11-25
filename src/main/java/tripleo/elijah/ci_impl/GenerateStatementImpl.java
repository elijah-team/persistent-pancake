/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.ci_impl;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CiExpression;
import tripleo.elijah.ci.GenerateStatement;
import tripleo.elijah.xlang.LocatableString;

import java.util.*;

/**
 * Created 9/6/20 12:04 PM
 */
public class GenerateStatementImpl implements GenerateStatement {
	public static class Directive {
		private final CiExpression expression;
		private final String name;

		public Directive(final @NotNull LocatableString token_, final CiExpression expression_) {
			name = token_.getString();
			expression = expression_;
		}

		public CiExpression getExpression() {
			return expression;
		}

		public String getName() {
			return name;
		}
	}

	public final List<Directive> dirs = new ArrayList<>();

	@Override
	public void addDirective(final @NotNull LocatableString token, final CiExpression expression) {
		dirs.add(new Directive(token, expression));
	}
}
