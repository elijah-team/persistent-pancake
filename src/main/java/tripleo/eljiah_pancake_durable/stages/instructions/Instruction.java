/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.stages.instructions;

import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceElement;

import java.util.List;

/**
 * Created 9/10/20 3:16 PM
 */
public class Instruction {
	private InstructionName name;
	private int             index = -1;
	public  DeduceElement deduceElement;
	List<InstructionArgument> args;
	private Context       context;

	public void setName(final InstructionName aName) {
		name = aName;
	}

	public void setIndex(final int l) {
		index = l;
	}

	public void setArgs(final List<InstructionArgument> args_) {
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

	public InstructionArgument getArg(final int i) {
		return args.get(i);
	}

	public void setContext(final Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public int getArgsSize() {
		return args.size();
	}

	public boolean isEmpty() {
		return getArgsSize() == 0;
	}

//	public List<InstructionArgument> getArgs() {
//		return args;
//	}
}

//
//
//
