/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jdeferred2.Promise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.lang.RegularTypeName;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.lang.VariableTypeName;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.lang.types.OS_UserType;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.test_help.Boilerplate;
import tripleo.elijah.util.Helpers;

/**
 * Useless tests. We really want to know if a TypeName will resolve to the same types
 */
public class DeduceTypesTest {

	private GenType                              x;
	private Promise<GenType, ResolveError, Void> xx;

	@BeforeEach
	public void setUp() {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation c   = b.comp;
		final OS_Module   mod = b.defaultMod();

		final DeduceTypeWatcher dtw = new DeduceTypeWatcher();

		b.withModBuilder(mod)
		 .addClass(cc -> {
			 cc.name("Test");
			 cc.addFunction(f -> {
				 f.name("test");
				 f.vars("x", "Integer", dtw);
			 });
		 });

/*
		final FunctionDef fd = null;
		final FunctionContext fc = (FunctionContext) fd.getContext(); // TODO needs to be mocked
		final IdentExpression x1 = Helpers.string_to_ident("x");
		x1.setContext(fc);
*/

		b.simpleDeduceModule3(mod, (final DeduceTypes2 d) -> {
			final IdentExpression nameToken = ((VariableStatement) dtw.element()).getNameToken();
			this.xx = DeduceLookupUtils.deduceExpression_p(d, nameToken, nameToken/*dtw.element()*/.getContext());
			xx.then(a -> this.x = a);
			xx.fail(e -> c.getErrSink().reportDiagnostic(e));
			dtw.onType(a -> this.x = a);
			System.out.println(this.x);
		});
	}

	/**
	 * TODO This test fails beacause we are comparing a BUILT_IN vs a USER OS_Type.
	 *   It fails because Integer is an interface and not a BUILT_IN
	 */
	@Disabled
	@Test
	void testDeduceIdentExpression1() {
//		assert x == null;

		Assertions.assertTrue(xx.isResolved(), "Promise not resolved");

		xx.then(xxx -> {
//			Assert.assertEquals(OS_Type.Type.USER, xxx.resolved.getType());
			System.out.println("1 " + new OS_BuiltinType(BuiltInTypes.SystemInteger).getBType());
			System.out.println("2 " + xxx.getResolved().getBType());
			System.out.println("2.5 " + xxx.getResolved());
			Assertions.assertNotEquals(new OS_BuiltinType(BuiltInTypes.SystemInteger).getBType(), xxx.getResolved().getBType());

			assert false; // never reached
		});
//		xx.fail(() -> {
//			if (false) throw new AssertionError();
//		});
	}

	/**
	 * Now comparing {@link RegularTypeName} to {@link VariableTypeName} works
	 */
@Disabled
	@Test
	void testDeduceIdentExpression2() {
		final RegularTypeName tn  = new RegularTypeName(null); // README 11/18 better than nothing?
		final Qualident       tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);

		Assertions.assertTrue(xx.isResolved(), "Promise not resolved");

		Assertions.assertTrue(genTypeTypenameEquals(new OS_UserType(tn), x/*.getTypeName()*/));
	}

	@Contract(value = "null, _ -> false", pure = true)
	private boolean genTypeTypenameEquals(final OS_Type aType, final @NotNull GenType genType) {
		return genType.getTypeName().isEqual(aType); // minikanren 04/15
	}

	@Disabled
	@Test
	void testDeduceIdentExpression3() {
		final VariableTypeName tn  = new VariableTypeName();
		final Qualident        tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);

		Assertions.assertTrue(xx.isResolved(), "Promise not resolved");

		Assertions.assertEquals(new OS_UserType(tn).getTypeName(), x.getTypeName().getTypeName());
		Assertions.assertTrue(genTypeTypenameEquals(new OS_UserType(tn), x));
	}

	@Disabled
	@Test
	void testDeduceIdentExpression4() {
		final VariableTypeName tn  = new VariableTypeName();
		final Qualident        tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("Integer"));
		tn.setName(tnq);

		Assertions.assertTrue(xx.isResolved(), "Promise not resolved");

		Assertions.assertEquals(new OS_UserType(tn).getTypeName(), x.getTypeName().getTypeName());
		Assertions.assertTrue(genTypeTypenameEquals(new OS_UserType(tn), x));
		Assertions.assertEquals(new OS_UserType(tn).asString(), x.getTypeName().asString());
	}

}

//
//
//
