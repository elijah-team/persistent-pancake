/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */

package tripleo.eljiah_pancake_durable.stages.gen_fn;

import tripleo.eljiah_pancake_durable.lang.OS_Module;

/**
 * Created 10/29/20 4:51 AM
 */
public interface GeneratedNode {
    String identityString();
    OS_Module module();

	default String asString() {
		return toString();
	}
}

//
//
//
