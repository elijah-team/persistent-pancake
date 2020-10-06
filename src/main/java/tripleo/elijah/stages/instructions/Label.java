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
 * Created 9/10/20 3:17 PM
 */
public class Label implements InstructionArgument {
	private final GeneratedFunction gf;
	String name;
	long index;
	private int number;

//	public Label(String name) {
//		this.name = name;
//	}

	public Label(GeneratedFunction gf) {
		this.gf = gf;
	}

	/**
	 * Corresponds to pc
	 * @param index pc
	 */
	public void setIndex(long index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long getIndex() {
		return index;
	}

	/**
	 * Corresponds to the number of labels
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}

//
//
//
