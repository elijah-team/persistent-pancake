/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.ParserClosure;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

public class Out {
	private final ParserClosure pc;

	public Out(final String fn, final Compilation compilation) {
		pc = new ParserClosure(fn, compilation);
	}

	public static void println(final String s) {
		SimplePrintLoggerToRemoveSoon.println2(s);
	}

	@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("NM_METHOD_NAMING_CONVENTION")
	public void FinishModule() {
		pc.module.finish();
	}

	public ParserClosure closure() {
		return pc;
	}

	public @NotNull OS_Module module() {
		return pc.module;
	}
}

//
//
//
