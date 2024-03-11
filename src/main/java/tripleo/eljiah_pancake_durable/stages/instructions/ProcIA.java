/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.stages.instructions;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;

/**
 * Created 1/12/21 4:22 AM
 */
public class ProcIA implements InstructionArgument {
	private final int index;
	private final BaseGeneratedFunction gf;

	public ProcIA(final int i, final BaseGeneratedFunction generatedFunction) {
		this.index = i;
		this.gf = generatedFunction;
	}

	@Override
	public String toString() {
		String procTableEntryString = "<<??>>";
		try {
			final ProcTableEntry procTableEntry;
			procTableEntry = gf.getProcTableEntry(index);
			procTableEntryString = procTableEntry.asString();
		} catch (NullPointerException npe) {}
		return "ProcIA{" +
				"index=" + index + ", " +
				"func=" + procTableEntryString +
				'}';
	}

	public int getIndex() {
		return index;
	}

	public @NotNull ProcTableEntry getEntry() {
		return gf.getProcTableEntry(index);
	}
}

//
//
//