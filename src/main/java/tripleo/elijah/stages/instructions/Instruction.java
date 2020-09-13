/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.instructions;

import java.util.List;

/**
 * Created 9/10/20 3:16 PM
 */
public class Instruction {
	private InstructionName name;
	private int index;
	List<InstructionArgument> args;

	public void setName(InstructionName aName) {
		name = aName;
	}

	public void setIndex(int l) {
		index = l;
	}

	public void setArgs(List<InstructionArgument> args_) {
		args = args_;
	}

	public InstructionName getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "Instruction{" +
				"name=" + name +
				", index=" + index +
				", args=" + args +
				'}';
	}
}

//
//
//