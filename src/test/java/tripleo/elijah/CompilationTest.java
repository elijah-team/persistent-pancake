/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.io.File;
import java.util.List;

import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

/**
 * @author Tripleo(envy)
 *
 */
public class CompilationTest {

	@Test
	final void testEz() throws Exception {
		final List<String> args = List_of("test/comp_test/main3", "-sE"/*, "-out"*/);
		final ErrSink      eee  = new EDP_ErrSink();
		final Compilation  c    = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(args);

		Assertions.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.ez")));
		Assertions.assertTrue(c.getIO().recordedRead(new File("test/comp_test/main3/main3.elijah")));
		Assertions.assertTrue(c.getIO().recordedRead(new File("test/comp_test/fact1.elijah")));
//		Assert.assertTrue(c.cis.size() > 0);
		Assertions.assertTrue(c.modules_size() > 2);
	}

}
	
//
//
//
