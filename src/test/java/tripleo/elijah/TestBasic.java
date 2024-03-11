/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.factory.comp.CompilationFactory;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_SequenceStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Statement;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

/**
 * @author Tripleo(envy)
 */
public class TestBasic {

	private final boolean __unboundedGate = false;

	@Test
	final void testBasicParse() throws Exception {
		final List<String> ez_files = Files.readLines(new File("test/basic/ez_files.txt"), Charsets.UTF_8);
		final List<String> args     = new ArrayList<String>();
		args.addAll(ez_files);
		args.add("-sE");
		final ErrSink     eee = new EDP_ErrSink();
		final Compilation c   = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(args);

		assertEquals(0, c.errorCount());
	}

	@Test
	final void testBasic_listfolders3() {
		final String s = "test/basic/listfolders3/listfolders3.ez";

		final ErrSink     eee = new EDP_ErrSink();
		final Compilation c   = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(List_of(s, "-sO"));

		if (c.errorCount() != 0)
			System.err.printf("Error count should be 0 but is %d for %s%n", c.errorCount(), s);

		assertEquals(35, c.errorCount()); // TODO Error count obviously should be 0

		assertEquals(36, c.getOutputTreeSize());
		assertEquals(8, c.reports().codeOutputSize());

		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Boolean.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Boolean.h"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/wpkotlin_c.demo.list_folders/__MODULE__.h"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/wpkotlin_c.demo.list_folders/__MODULE__.c"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/wpkotlin_c.demo.list_folders/MainLogic.c"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/wpkotlin_c.demo.list_folders/MainLogic.h"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/Main.c"));
		assertTrue(c.reports().containsCodeOutput("listfolders3/Main.h"));

		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Arguments.h"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Arguments.c"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Integer64.h"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Integer64.c"));

		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}

	@Test
	final void testBasic_listfolders4() {
		final String s = "test/basic/listfolders4/listfolders4.ez";

		final ErrSink     eee = new EDP_ErrSink();
		final Compilation c   = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(List_of(s, "-sO"));

		if (c.errorCount() != 0)
			System.err.printf("Error count should be 0 but is %d for %s%n", c.errorCount(), s);

		assertEquals(51, c.errorCount()); // TODO Error count obviously should be 0

		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}

	@Test
	final void testBasic_fact1() {
		final String        s  = "test/basic/fact1/main2";
		final Compilation   c  = CompilationFactory.mkCompilation(new EDP_ErrSink(), new EDP_IO());

//		final CompilerInput i1 = new CompilerInput_(s);
//		final CompilerInput i2 = new CompilerInput_("-sO");
//		c.feedInputs(List_of(i1, i2), new DefaultCompilerController(((CompilationImpl) c).getCompilationAccess3()));
		c.feedCmdLine(List_of(s, "-sO"));

		if (c.errorCount() != 0) {
			System.err.printf("Error count should be 0 but is %d for %s%n", c.errorCount(), s);
		}

		final @NotNull EOT_OutputTree cot = c.getOutputTree();

		assertEquals(28, cot.getList().size()); // TODO why not 6?

		select(cot.getList(), f -> f.getFilename().equals("/main2/Main.h"))
		  .then(f -> {
			  System.out.println(((EG_SequenceStatement) f.getStatementSequence())._list().stream().map(EG_Statement::getText).collect(Collectors.toList()));
		  });
		select(cot.getList(), f -> f.getFilename().equals("/main2/Main.c"))
		  .then(f -> {
			  System.out.println(((EG_SequenceStatement) f.getStatementSequence())._list().stream().map(EG_Statement::getText).collect(Collectors.toList()));
		  });

		// TODO Error count obviously should be 0
		assertEquals(13, c.errorCount()); // FIXME why 123?? 04/15

		assertEquals(10, c.reports().codeOutputSize());

		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Arguments.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Arguments.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/ConstString.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Boolean.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Boolean.h"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.c"));
		assertTrue(c.reports().containsCodeOutput("prelude/Prelude/Integer64.h"));
		assertTrue(c.reports().containsCodeOutput("main2/Main.c"));
		assertTrue(c.reports().containsCodeOutput("main2/Main.h"));

		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Prelude.c"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Prelude.h"));
		assertFalse(c.reports().containsCodeOutput("main2/wprust.demo.fact/fact1.c"));
		assertFalse(c.reports().containsCodeOutput("main2/wprust.demo.fact/fact1.h"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Unsigned64.c"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/Unsigned64.h"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.c"));
		assertFalse(c.reports().containsCodeOutput("prelude/Prelude/IPrintable.h"));


		if (__unboundedGate) {
			assertEquals(c.reports().codeOutputSize(), c.reports().unboundedCodeOutputSize(), "unboundedCodeOutputSize");
		}
	}

	static <T> @NotNull Promise<T, Void, Void> select(@NotNull final List<T> list, final Predicate<T> p) {
		final DeferredObject<T, Void, Void> d = new DeferredObject<T, Void, Void>();
		for (final T t : list) {
			if (p.test(t)) {
				d.resolve(t);
				return d;
			}
		}
		d.reject(null);
		return d;
	}
}

//
//
//
