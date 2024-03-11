/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 * 
 */
package tripleo.eljiah_pancake_durable.contexts;

import tripleo.eljiah_pancake_durable.lang.AliasStatement;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.Loop;
import tripleo.eljiah_pancake_durable.lang.NamespaceStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Element2;
import tripleo.eljiah_pancake_durable.lang.StatementItem;
import tripleo.eljiah_pancake_durable.lang.VariableSequence;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 9:40:43 PM
 */
public class LoopContext extends Context {

	private final Loop    carrier;
	private final Context _parent;

	public LoopContext(final Context cur, final Loop loop) {
		carrier = loop;
		_parent = cur;
	}

	@Override public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		alreadySearched.add(carrier.getContext());

		if (carrier.getIterNameToken() != null) {
			if (carrier.getIterName() != null) {
				if (name.equals(carrier.getIterName())) { // reversed to prevent NPEs
					final IdentExpression ie = carrier.getIterNameToken();
					Result.add(name, level, ie, this);
				}
			}
		}

		for (final StatementItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
					!(item instanceof NamespaceStatement) &&
					!(item instanceof FunctionDef) &&
					!(item instanceof VariableSequence) &&
					!(item instanceof AliasStatement)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, (OS_Element) item, this);
				}
			}
			if (item instanceof VariableSequence) {
				SimplePrintLoggerToRemoveSoon.println2("1102 " + item);
				for (final VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs, this);
				}
			}
		}

		if (carrier.getParent() != null) {
			final Context context = getParent();
			if (!alreadySearched.contains(context) || !one)
				context.lookup(name, level + 1, Result, alreadySearched, false); // TODO test this
		}
		return Result;
		
	}

	@Override
	public Context getParent() {
		return _parent;
	}

}

//
//
//
