/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.IdentExpression;

/**
 * Created 9/12/20 10:27 PM
 */
public class IdentTableEntry {
    final int index;
    private final IdentExpression ident;

    public IdentTableEntry(int index, IdentExpression ident) {
        this.index = index;
        this.ident = ident;
    }
}

//
//
//