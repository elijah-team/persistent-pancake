/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;

import java.io.File;
import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

/**
 * @author Tripleo(envy)
 *
 */
public class CompilationTest {

	@Test
	public final void testEz() throws Exception {
		final List<String> args = List_of("test/comp_test/main3", "-sE"/*, "-out"*/);
		final ErrSink eee = new StdErrSink();
		final Compilation c = new Compilation(eee, new IO());

		c.feedCmdLine(args);

		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.ez")));
		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.elijah")));
		Assert.assertTrue(c.getIO().recordedRead(new File("test/comp_test/fact1.elijah")));

		Assert.assertTrue(c.instructionCount() > 0);

		c.modules
				.stream()
				.forEach(mod -> System.out.println(String.format("**48** %s %s", mod, mod.getFileName())));
//		Assert.assertThat(c.modules.size(), new IsEqual<Integer>(3));
		Assert.assertTrue(c.modules.size() > 2);
	}

}
	
//
//
//
