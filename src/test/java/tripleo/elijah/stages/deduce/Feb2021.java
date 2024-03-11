/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

/**
 * Created 9/9/21 4:16 AM
 */
public class Feb2021 {
	private boolean __unboundedGate = false;

	@Test
	void testProperty() {
		final Compilation c = new EDP_Compilation(new EDP_ErrSink(), new EDP_IO());

		c.feedCmdLine(List_of("test/feb2021/property/"));

		assertEquals(0, c.errorCount());

		assertEquals(6, c.reports().codeOutputSize());

		assertTrue(c.reports().containsCodeOutput("property/Pr.h"));
		assertTrue(c.reports().containsCodeOutput("property/Pr.c"));
		assertTrue(c.reports().containsCodeOutput("property/Main.c"));
		assertTrue(c.reports().containsCodeOutput("property/Main.h"));
		assertTrue(c.reports().containsCodeOutput("property/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("property/Foo.c"));

		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));
		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));
		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
		assertTrue(!c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));

		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}

	@Test
	void testFunction() {
		final Compilation c = new EDP_Compilation(new EDP_ErrSink(), new EDP_IO());

		c.feedCmdLine(List_of("test/feb2021/function/"));

		assertEquals(1, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("function/Main.c"));
		assertTrue(c.reports().containsCodeOutput("function/Main.h"));

		assertEquals(2, c.reports().codeOutputSize());

		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}

	@Test
	void testHier() {
		final Compilation c = new EDP_Compilation(new EDP_ErrSink(), new EDP_IO());

		c.feedCmdLine(List_of("test/feb2021/hier/"));

		assertEquals(0, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("hier/Bar.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Main.c"));
		assertTrue(c.reports().containsCodeOutput("hier/Main.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("hier/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("hier/Bar.c"));

		assertEquals(6, c.reports().codeOutputSize());

		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}
}

//
//
//
