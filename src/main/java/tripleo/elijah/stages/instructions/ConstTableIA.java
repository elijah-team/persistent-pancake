/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

import tripleo.elijah.stages.gen_fn.GeneratedFunction;

/**
 * Created 9/10/20 3:35 PM
 */
public class ConstTableIA implements InstructionArgument {
    private final GeneratedFunction gf;
    private final int index;

    @Override
    public String toString() {
        return String.format("(ct %d) [%s]", index, gf.cte_list.get(index).getName());
    }

    public ConstTableIA(int index, GeneratedFunction generatedFunction) {
        this.index = index;
        this.gf = generatedFunction;
    }
}

//
//
//