/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.contexts;

import tripleo.elijah.lang.BaseFunctionDef;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.DecideElObjectType;
import tripleo.elijah.lang.FormalArgListItem;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.FunctionItem;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.NamespaceStatement;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Element2;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;

import java.util.List;

/**
 * @author Tripleo
 *
 * Created 	Mar 26, 2020 at 6:13:58 AM
 */
public class FunctionContext extends Context {
	private final BaseFunctionDef carrier;
	private final Context _parent;

	public FunctionContext(final Context aParent, final BaseFunctionDef fd) {
		_parent = aParent;
		carrier = fd;
	}

	@Override public LookupResultList lookup(final String name, final int level, final LookupResultList Result, final List<Context> alreadySearched, final boolean one) {
		//alreadySearched.add(carrier.getContext());
		for (final FunctionItem item: carrier.getItems()) {

			switch (DecideElObjectType.getElObjectType(item)) {

			case CLASS, NAMESPACE, FUNCTION, CONSTRUCTOR /*DEF, */ -> {
				if (((OS_Element2) item).name().equals(name)) {
					Result.add(name, level, item, this);
				}
			}
			case VAR -> {
				tripleo.elijah.util.Stupidity.println2("[FunctionContext#lookup] VariableSequence " + item);
				for (final VariableStatement vs : ((VariableSequence) item).items()) {
					if (vs.getName().equals(name)) {
						Result.add(name, level, vs, this);
					}
				}
			}
			default -> {
				// README 11/07 if I made a mistake scream
				if (item instanceof OS_Element2) {
					assert false;
				}
			}

			}
		}
		for (final FormalArgListItem arg : carrier.getArgs()) {
			if (arg.name().equals(name)) {
				Result.add(name, level, arg, this);
			}
		}

		if (carrier.getParent() != null) {
			final Context context = getParent();
		//	if (!alreadySearched.contains(context) || !one)
			if (context != null) {
				return context.lookup(name, level + 1, Result, alreadySearched, false);
			} else {
				assert false;
			}
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
