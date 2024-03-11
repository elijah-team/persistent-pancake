/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.comp.CompilationAlways;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CompilationAccess;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.eljiah_pancake_durable.contexts.FunctionContext;
import tripleo.eljiah_pancake_durable.contexts.ModuleContext;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.eljiah_pancake_durable.lang.NormalTypeName;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.Qualident;
import tripleo.eljiah_pancake_durable.lang.Scope3;
import tripleo.eljiah_pancake_durable.lang.VariableSequence;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.lang.VariableTypeName;
import tripleo.eljiah_pancake_durable.lang.types.OS_UserType;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceLookupUtils;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.test_help.Boilerplate;
import tripleo.eljiah_pancake_durable.util.Helpers;

public class DeduceTypesSecondTest {

	@Disabled
	@Test
	void testDeduceIdentExpression() throws ResolveError {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation c   = b.comp;
		final OS_Module    mod = b.defaultMod();

		mod.prelude = mod.parent.findPrelude("c").success();
		final ModuleContext mctx = new ModuleContext(mod);
		mod.setContext(mctx);
		final ClassStatement cs = new ClassStatement(mod, mctx);
		cs.setName(Helpers.string_to_ident("Test"));
		final FunctionDef fd = cs.funcDef();
		fd.setName((Helpers.string_to_ident("test")));
		final Scope3            scope3 = new Scope3(fd);
		final VariableSequence  vss    = scope3.varSeq();
		final VariableStatement vs     = vss.next();
		vs.setName((Helpers.string_to_ident("x")));
		final Qualident qu = new Qualident();
		qu.append(Helpers.string_to_ident("SystemInteger"));
		((NormalTypeName) vs.typeName()).setName(qu);
		final FunctionContext fc = (FunctionContext) fd.getContext();
		vs.typeName().setContext(fc);
		final IdentExpression x1 = Helpers.string_to_ident("x");
		x1.setContext(fc);
		fd.scope(scope3);
		fd.postConstruct();
		cs.postConstruct();
		mod.postConstruct();

		//
		//
		//
		final ElLog.Verbosity verbosity1 = CompilationAlways.gitlabCIVerbosity();
		final AccessBus     ab           = new AccessBus(new EDP_CompilationAccess(c));
		final PipelineLogic pl = new PipelineLogic(ab);
		final DeducePhase   dp = pl.dp;
		final DeduceTypes2  d  = dp.deduceModule(mod, verbosity1);
//		final DeduceTypes d = new DeduceTypes(mod);
		final GenType x = DeduceLookupUtils.deduceExpression(d, x1, fc);
		System.out.println(x);
//		Assert.assertEquals(new OS_Type(BuiltInTypes.SystemInteger).getBType(), x.getBType());
//		final RegularTypeName tn = new RegularTypeName();
		final VariableTypeName tn = new VariableTypeName();
		final Qualident tnq = new Qualident();
		tnq.append(Helpers.string_to_ident("SystemInteger"));
		tn.setName(tnq);
		tn.setContext(fd.getContext());

//		Assert.assertEquals(new OS_Type(tn).getTypeName(), x.getTypeName());
		Assertions.assertTrue(genTypeEquals(d.resolve_type(new OS_UserType(tn), tn.getContext()), x));
//		Assert.assertEquals(new OS_Type(tn).toString(), x.toString());
	}

	private boolean genTypeEquals(final GenType a, final GenType b) {
		if (a == null || b == null) return false;
		// TODO hack
		return a.getTypeName().isEqual(b.getTypeName()) &&
				a.getResolved().isEqual(b.getResolved());
	}
}
