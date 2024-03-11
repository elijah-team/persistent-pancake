/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.eljiah_pancake_durable.stages.gen_fn;

import tripleo.eljiah_pancake_durable.stages.deduce.FunctionInvocation;

import java.util.List;

/**
 * Created 6/22/21 3:40 AM
 */
public interface DependencyTracker {
	// for use in DeduceTypes2
	List<GenType> dependentTypes();
	List<FunctionInvocation> dependentFunctions();
}

//
//
//
