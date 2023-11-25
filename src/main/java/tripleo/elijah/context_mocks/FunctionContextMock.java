/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 */
package tripleo.elijah.context_mocks;

import tripleo.elijah.lang.BaseFunctionDef;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.LookupResultList;

import java.util.List;
import java.util.Objects;

/**
 * @author Tripleo
 * <p>
 * Created 	Nov 08, 2023
 */
public class FunctionContextMock extends Context {
	private final BaseFunctionDef carrier;
	private final Context _parent;

	public FunctionContextMock(final Context aParent, final BaseFunctionDef fd) {
		_parent = aParent;
		carrier = fd;
	}

	public FunctionContextMock() {
		carrier = null;
		_parent = null;
	}

	@Override
	public LookupResultList lookup(final String name,
	                               final int level,
	                               final LookupResultList Result,
	                               final List<Context> alreadySearched,
	                               final boolean one) {
		LookupResultList result = Result;

		for (final Expectation expectation : getExpectations()) {
			if (Objects.equals(expectation.getName(), name)) {
				expectation.contribute(Result);
			}
		}

		if (carrier != null) {
			if (carrier.getParent() != null) {
				final Context context = getParent();

				if (context != null) {
					result = context.lookup(name, level + 1, Result, alreadySearched, false);
				} else {
					assert false;
				}
			}
		}

		return result;
	}

	@Override
	public Context getParent() {
		return _parent;
	}
}

//
//
//
