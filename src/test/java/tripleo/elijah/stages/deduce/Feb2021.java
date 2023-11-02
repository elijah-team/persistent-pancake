/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.comp.internal.CompilationImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 9/9/21 4:16 AM
 */
public class Feb2021 {

	@Test
	public void testProperty() {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/property/"));

		assertEquals(96, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("property/Pr.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("property/Main.c"));
		assertTrue(c.reports().containsCodeOutput("property/Pr.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
		assertTrue(c.reports().containsCodeOutput("property/Main.h"));
		assertTrue(c.reports().containsCodeOutput("property/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));
		assertTrue(c.reports().containsCodeOutput("property/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));

		assertEquals(24, c.reports().codeOutputSize());
	}

	@Test
	public void testFunction() {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/function/"));

		assertEquals(1, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("function/Main.c"));
		assertTrue(c.reports().containsCodeOutput("function/Main.h"));

		assertEquals(6, c.reports().codeOutputSize());
	}

	@Test
	public void testHier() {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/hier/"));

		assertEquals(0, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("hier/Bar.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Main.c"));
		assertTrue(c.reports().containsCodeOutput("hier/Main.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("hier/Bar.c"));

		assertEquals(6, c.reports().codeOutputSize());
	}

}

//
//
//
