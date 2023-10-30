/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.ci_impl;

import antlr.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.ci.*;
import tripleo.elijah.lang.IExpression;

import java.util.*;

/**
 * Created 9/6/20 12:06 PM
 */
public class LibraryStatementPartImpl implements LibraryStatementPart {
	public class Directive {

		private final IExpression expression;
		private final String name;

		public Directive(final @NotNull Token token_, final IExpression expression_) {
			name = token_.getText();
			expression = expression_;
		}
	}

	private CompilerInstructions ci;
	private String dirName;

	private String name;

	private @Nullable List<Directive> dirs = null;

	@Override
	public void addDirective(final @NotNull Token token, final IExpression iExpression) {
		if (dirs == null)
			dirs = new ArrayList<>();
		dirs.add(new Directive(token, iExpression));
	}

	@Override
	public String getDirName() {
		return dirName;
	}

	@Override
	public CompilerInstructions getInstructions() {
		return ci;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setDirName(final @NotNull Token dirName) {
		this.dirName = dirName.getText();
	}

	@Override
	public void setInstructions(final @NotNull CompilerInstructions instructions) {
		ci = instructions;
	}

	@Override
	public void setName(final @NotNull Token i1) {
		name = i1.getText();
	}

}

//
//
//
