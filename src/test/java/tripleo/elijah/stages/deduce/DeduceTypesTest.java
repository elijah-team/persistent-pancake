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
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.OS_Type;
import tripleo.eljiah_pancake_durable.lang.Qualident;
import tripleo.eljiah_pancake_durable.lang.RegularTypeName;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.lang.VariableTypeName;
import tripleo.eljiah_pancake_durable.lang.types.OS_BuiltinType;
import tripleo.eljiah_pancake_durable.lang.types.OS_UserType;
import tripleo.eljiah_pancake_durable.lang2.BuiltInTypes;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceLookupUtils;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypeWatcher;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.test_help.Boilerplate;
import tripleo.eljiah_pancake_durable.util.Helpers;

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
	@Test
	void testDeduceIdentExpression1() {
//		assert x == null;

		Assertions.assertTrue(xx.isResolved(), "Promise not resolved");

		xx.then((GenType aGenType) -> {
//			Assert.assertEquals(OS_Type.Type.USER, aGenType.resolved.getType());
			System.out.println("1 " + new OS_BuiltinType(BuiltInTypes.SystemInteger).getBType());
			System.out.println("2 " + aGenType.getResolved().getBType());
			System.out.println("2.5 " + aGenType.getResolved());
			Assertions.assertNotEquals(new OS_BuiltinType(BuiltInTypes.SystemInteger).getBType(), aGenType.getResolved().getBType());

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
