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
import tripleo.elijah.factory.comp.CompilationFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 3/5/21 4:32 AM
 */
public class ClassInstantiationTest {

	@Test
	public void classInstantiation() throws Exception {
		final String      f = "test/basic1/class_instantiation/";
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));

		assertEquals(128, c.errorCount());
		
		assertTrue(c.reports().containsCodeOutput("______________/Bar_105.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_102.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_103.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Main.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_110.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_110.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_107.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_108.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_105.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_106.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_103.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_108.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_109.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_106.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_102.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_107.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Main.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_109.h"));
	}

	@Test
	public void classInstantiation2() throws Exception {
		final String      f = "test/basic1/class_instantiation2/";
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));

		assertEquals(128, c.errorCount());

		assertTrue(c.reports().containsCodeOutput("______________/Bar_127.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_104.c"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_102.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_118.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_116.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_114.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/IPrintable.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_112.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_118.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_115.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_111.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_113.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_111.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_127.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_106.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_104.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_106.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_102.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/ConstString.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_105.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_120.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_103.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_120.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_119.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_117.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_115.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_119.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_117.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_113.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Prelude.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Main.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Integer64.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_110.h"));
		assertTrue(c.reports().containsCodeOutput("null/Prelude/Integer64.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_116.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_114.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_112.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_110.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_107.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_105.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_103.h"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_109.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_107.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Main.c"));
		assertTrue(c.reports().containsCodeOutput("______________/Bar_109.h"));


	}

	@Test
	public void classInstantiation3() throws Exception {
		final String      f = "test/basic1/class_instantiation3/";
		final Compilation c = CompilationFactory.mkCompilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));
	}
}

//
//
//
