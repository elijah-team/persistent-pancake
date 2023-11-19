/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tripleo.elijah.util.Helpers.List_of;

import org.junit.Test;

import tripleo.elijah.DebugFlags;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.comp.internal.CompilationImpl;
import tripleo.elijah.factory.comp.CompilationFactory;

/**
 * Created 3/5/21 4:32 AM
 */
public class ClassInstantiationTest {

	@Test
	public void classInstantiation() throws Exception {
		final String      f = "test/basic1/class_instantiation/";
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));

		assertEquals(141, c.errorCount());
		
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_105.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_102.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_103.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Main.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Foo.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_110.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_110.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_107.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_108.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_105.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_106.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_103.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_108.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_109.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_106.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_102.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_107.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation/Main.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation/Bar_109.h"));
	}

	@Test
	public void classInstantiation2() throws Exception {
		final String      f = "test/basic1/class_instantiation2/";
		final Compilation c = new CompilationImpl(new StdErrSink(), new IO());

		DebugFlags.classInstantiation2 = true;

		c.feedCmdLine(List_of(f));

		//assertEquals(128, c.errorCount());
		assertEquals(5, c.errorCount());

//		org.hamcrest.Matchers.containsInAnyOrder

		// FIXME 11/18 shouldn't have to edit source code to do this

//		assertEquals(12, c.reports().codeOutputSize());
		assertEquals(6, c.reports().codeOutputSize());

//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));

//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));

		////assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.c"));
		////assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.h"));

		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Main.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Main.c"));

		// confused
		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_103.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_103.h"));
		////assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_104.c"));
		////assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_104.h"));
		////assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_106.c"));
		////assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_106.h"));

//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_127.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_102.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_118.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_116.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_114.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_112.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_118.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_115.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_111.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_113.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_111.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_127.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_102.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_105.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_120.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_120.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_119.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_117.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_115.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_119.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_117.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_113.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_110.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_116.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_114.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_112.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_110.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_107.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_105.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_109.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_107.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation2/Bar_109.h"));


	}

	@Test
	public void classInstantiation3() throws Exception {
		final String      f = "test/basic1/class_instantiation3/";
		final Compilation c = CompilationFactory.mkCompilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));


		assertEquals(8, c.errorCount());
		assertEquals(10, c.reports().codeOutputSize());

		// pr should
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));

		// ok
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_105.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_102.c"));

		// def should
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));

		// does
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Foo.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Foo.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Main.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Main.c"));

		// confused
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_103.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_103.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_104.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_104.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.c"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.h"));

		// dups
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Foo.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Foo.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_103.c"));

		// pr should
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));

		// should not
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_110.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_110.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_107.h"));
		//assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_108.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_105.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_108.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_109.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_102.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_107.c"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_109.h"));

//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_127.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_104.c"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_102.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_118.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_116.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_114.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_112.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_118.c"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_115.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_111.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_113.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_111.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_127.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_104.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_106.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_102.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_105.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_120.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_120.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_119.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_117.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_115.h"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_119.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_117.c"));
//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_113.h"));

		// def should
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
//		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));

		// ??
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Main.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.h"));

		// def. should not
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_110.h"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_116.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_114.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_112.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_110.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_107.h"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_105.h"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_109.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_107.c"));
		assertFalse(c.reports().containsCodeOutput("class_instantiation3/Bar_109.h"));

//		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Bar_103.h"));
		assertTrue(c.reports().containsCodeOutput("class_instantiation3/Main.c"));
	}
}

//
//
//
