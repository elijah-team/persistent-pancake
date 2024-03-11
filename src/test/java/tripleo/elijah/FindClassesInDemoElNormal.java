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
import tripleo.eljiah_pancake_durable.entrypoints.MainClassEntryPoint;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.util.Helpers;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.util.List;

/**
 * @author Tripleo
 *
 */
public class FindClassesInDemoElNormal {

	@Test
	final void testParseFile() throws Exception {
		final List<String> args = Helpers.List_of("test/demo-el-normal", "test/demo-el-normal/main2", "-sE");
		final ErrSink      eee  = new EDP_ErrSink();
		final Compilation  c    = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(args);

		final List<ClassStatement> aClassList = c.findClass("Main");
		for (final ClassStatement classStatement : aClassList) {
			System.out.println(classStatement.getPackageName().getName());
		}
		Assertions.assertEquals(3, aClassList.size());  // NOTE this may change. be aware
	}


	@Test
	final void testListFolders() throws Exception {
		final List<String> args = Helpers.List_of("test/demo-el-normal/listfolders/", "-sE");
		final ErrSink      eee  = new EDP_ErrSink();
		final Compilation  c    = new EDP_Compilation(eee, new EDP_IO());

		c.feedCmdLine(args);

		final List<ClassStatement> aClassList = c.findClass("Main");
		Assertions.assertEquals(1, aClassList.size());

		Assertions.assertFalse(MainClassEntryPoint.isMainClass(aClassList.get(0)), "isMainClass");
	}

}
	
//
//
//
