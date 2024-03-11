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
import tripleo.eljiah_pancake_durable.lang.ClassItem;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.NamespaceStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Element2;
import tripleo.eljiah_pancake_durable.lang.PropertyStatement;
import tripleo.eljiah_pancake_durable.lang.VariableSequence;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 29, 2020 at 8:59:42 PM
 */
public class NamespaceContext extends Context {

	public final  NamespaceStatement carrier;
	private final Context            _parent;

//	public NamespaceContext(NamespaceStatement namespaceStatement) {
//		carrier = namespaceStatement;
//	}

	public NamespaceContext(final Context aParent, final NamespaceStatement ns) {
		_parent = aParent;
		carrier = ns;
	}

	@Override public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		alreadySearched.add(carrier.getContext());
		for (final ClassItem item: carrier.getItems()) {
			if (!(item instanceof ClassStatement) &&
				!(item instanceof NamespaceStatement) &&
				!(item instanceof VariableSequence) &&
				!(item instanceof AliasStatement) &&
				!(item instanceof FunctionDef) &&
				!(item instanceof PropertyStatement)
			) continue;
			if (item instanceof OS_Element2) {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item, this);
				}
			}
			if (item instanceof VariableSequence) {
				SimplePrintLoggerToRemoveSoon.println2("[NamespaceContext#lookup] VariableSequence " + item);
				for (final VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name))
						Result.add(name, level, vs, this);
				}
			}
		}
		if (getParent() != null) {
			final Context context = getParent();
			if (!alreadySearched.contains(context) || !one)
				return context.lookup(name, level + 1, Result, alreadySearched, false);
		}
		return Result;

	}

	@Override public Context getParent() {
		return _parent;
	}
}
