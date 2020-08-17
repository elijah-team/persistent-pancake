/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.contexts.ClassContext;
import tripleo.elijah.contexts.FunctionContext;
import tripleo.elijah.contexts.ModuleContext;
import tripleo.elijah.gen.nodes.Helpers;
import tripleo.elijah.lang.*;

public class DeduceTypesTest {

	private OS_Type x;

	@Before
	public void setUp() {
		OS_Module mod = new OS_Module();
		mod.parent = new Compilation(new StdErrSink(), new IO());
		DeduceTypes d = new DeduceTypes(mod);
		final ModuleContext mctx = new ModuleContext(mod);
		mod.setContext(mctx);
		ClassStatement cs = new ClassStatement(mod);
		cs.setName(new IdentExpression(Helpers.makeToken("Test")));
		final ClassContext cctx = new ClassContext(mctx, cs);
		cs.setContext(cctx);
		final FunctionDef fd = cs.funcDef();
		fd.setName(new IdentExpression(Helpers.makeToken("test")));
		final FunctionContext fctx = new FunctionContext(cctx, fd);
		fd.setContext(fctx);
		VariableSequence vss = fd.scope().statementClosure().varSeq(fctx);
		final VariableStatement vs = vss.next();
		vs.setName(Helpers.makeToken("x"));
		final Qualident qu = new Qualident();
		qu.append(Helpers.makeToken("Integer"));
		((NormalTypeName)vs.typeName()).setName(qu);
		fd.postConstruct();
		cs.postConstruct();
		mod.postConstruct();
		FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		x = d.deduceExpression(new IdentExpression(Helpers.makeToken("x")), fc);
		System.out.println(x);
	}
	/** TODO This test fails beacause we are comparing a BUILT_IN vs a USER OS_Type.
	 *  Eventually it should pass when we resolve the primitive types.
	 */
//	@Test
//	public void testDeduceIdentExpression1() {
//		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
//	}
	/**
	 * was comparing a TypeName to an OS_Type and that's why it failed
	 */
//	@Test
//	public void testDeduceIdentExpression2() {
//		final RegularTypeName tn = new RegularTypeName();
//		Qualident tnq = new Qualident();
//		tnq.append(Helpers.makeToken("Integer"));
//		tn.setName(tnq);
//		Assert.assertEquals(new OS_Type(tn), x.getTypeName());
//	}
	@Test
	public void testDeduceIdentExpression3() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
	}
	@Test
	public void testDeduceIdentExpression3_5() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
	}
	@Test
	public void testDeduceIdentExpression4() {
		final VariableTypeName tn = new VariableTypeName();
		Qualident tnq = new Qualident();
		tnq.append(Helpers.makeToken("Integer"));
		tn.setName(tnq);
//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
//		Assert.assertEquals(new OS_Type(tn), x); // TODO this fails even when true
		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

}
