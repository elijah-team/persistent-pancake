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

import static tripleo.elijah.util.Helpers.List_of;

/**
 * Created 3/5/21 4:32 AM
 */
public class ClassInstantiationTest {

	@Test
	public void classInstantiation() throws Exception {
		String f = "test/basic1/class_instantiation/";
		Compilation c = new Compilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));
	}

	@Test
	public void classInstantiation2() throws Exception {
		String f = "test/basic1/class_instantiation2/";
		Compilation c = new Compilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));
	}

	@Test
	public void classInstantiation3() throws Exception {
		String f = "test/basic1/class_instantiation3/";
		Compilation c = new Compilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));
	}

	@Test
	public void classInstantiation4() throws Exception {
		String f = "test/basic1/class_instantiation4/";
		Compilation c = new Compilation(new StdErrSink(), new IO());

		c.feedCmdLine(List_of(f));

		System.err.println("Errorcount is" + c.errorCount());
	}
}

//
//
//
