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
	public void testProperty() throws Exception {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/property/"));

		assertEquals(96, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("null/Pr.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("null/Main.c"));
		assertTrue(c.reports().containsCodeOutput("null/Pr.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.h"));
		assertTrue(c.reports().containsCodeOutput("null/Main.h"));
		assertTrue(c.reports().containsCodeOutput("null/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.c"));
		assertTrue(c.reports().containsCodeOutput("null/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.h"));

	}

	@Test
	public void testFunction() throws Exception {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/function/"));

		assertEquals(0, c.errorCount());
	}

	@Test
	public void testHier() throws Exception {
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of("test/feb2021/hier/"));

		assertEquals(0, c.errorCount());
	}

}

//
//
//
