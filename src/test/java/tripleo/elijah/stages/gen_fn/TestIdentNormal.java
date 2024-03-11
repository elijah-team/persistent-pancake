/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tripleo.eljiah_pancake_durable.comp.CompilationAlways;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah_durable_pancake.comp.impl.EDP_CompilationAccess;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.DotExpression;
import tripleo.eljiah_pancake_durable.lang.ExpressionBuilder;
import tripleo.eljiah_pancake_durable.lang.ExpressionKind;
import tripleo.eljiah_pancake_durable.lang.FormalArgList;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.IBinaryExpression;
import tripleo.eljiah_pancake_durable.lang.IdentExpression;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.ProcedureCallExpression;
import tripleo.eljiah_pancake_durable.lang.Scope3;
import tripleo.eljiah_pancake_durable.lang.StatementWrapper;
import tripleo.eljiah_pancake_durable.lang.VariableSequence;
import tripleo.eljiah_pancake_durable.lang.VariableStatement;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.FoundElement;
import tripleo.eljiah_pancake_durable.stages.deduce.FunctionInvocation;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenerateFunctions;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratePhase;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;
import tripleo.eljiah_pancake_durable.stages.instructions.IdentIA;
import tripleo.eljiah_pancake_durable.stages.instructions.InstructionArgument;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.test_help.Boilerplate;

/**
 * Created 3/4/21 3:53 AM
 */
public class TestIdentNormal {

//	@Test(expected = IllegalStateException.class) // TODO proves nothing
	public void test() {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation comp = b.comp;
		final OS_Module mod = comp.moduleBuilder()
		                          .addToCompilation()
		                          .build();
		final FunctionDef fd   = mock(FunctionDef.class);
		final Context     ctx1 = mock(Context.class);
		final Context     ctx2 = mock(Context.class);

		final ElLog.Verbosity verbosity1    = CompilationAlways.gitlabCIVerbosity();
		final AccessBus     ab           = new AccessBus(new EDP_CompilationAccess(comp));
		final PipelineLogic pl            = new PipelineLogic(ab);
		final GeneratePhase generatePhase = pl.generatePhase;
//		GenerateFunctions generateFunctions = new GenerateFunctions(generatePhase, mod, pl);
		final GenerateFunctions generateFunctions = generatePhase.getGenerateFunctions(mod);
		final GeneratedFunction generatedFunction = new GeneratedFunction(fd);
		final VariableSequence  seq               = new VariableSequence(ctx1);
		final VariableStatement vs = new VariableStatement(seq);
		final IdentExpression x = IdentExpression.forString("x");
		vs.setName(x);
		final IdentExpression foo = IdentExpression.forString("foo");
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		final InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
		@NotNull final List<InstructionArgument> l = BaseGeneratedFunction._getIdentIAPathList(s);
		System.out.println(l);
//      System.out.println(generatedFunction.getIdentIAPathNormal());

		//
		//
		//

		final LookupResultList lrl = new LookupResultList();
		lrl.add("x", 1, vs, ctx2);
		expect(ctx2.lookup("x")).andReturn(lrl);

		replay(ctx2);

		//
		//
		//

		final IdentIA identIA = new IdentIA(1, generatedFunction);

		final DeduceTypes2 d2 = new DeduceTypes2(mod, pl.dp);

		final List<InstructionArgument> ss = BaseGeneratedFunction._getIdentIAPathList(identIA);
		d2.resolveIdentIA2_(ctx2, null, ss/*identIA*/, generatedFunction, new FoundElement(pl.dp) {
			@Override
			public void foundElement(final OS_Element e) {
				System.out.println(e);
			}

			@Override
			public void noFoundElement() {
				final int y = 2;
			}
		});
	}

	// TODO just a mess
	@Disabled
	@Test
	void test2() {
		final Boilerplate b = new Boilerplate();
		b.get();
		final Compilation comp = b.comp;
		final OS_Module    mod  = b.defaultMod();

//		FunctionDef fd = mock(FunctionDef.class);
		final Context ctx2 = mock(Context.class);

		final ElLog.Verbosity verbosity1    = CompilationAlways.gitlabCIVerbosity();
		final AccessBus     ab           = new AccessBus(new EDP_CompilationAccess(comp));
		final PipelineLogic   pl            = new PipelineLogic(ab);
		final GeneratePhase   generatePhase = pl.generatePhase;
		final DeducePhase     phase         = pl.dp;

		final GenerateFunctions generateFunctions = generatePhase.getGenerateFunctions(mod);

		//
		//
		//

		final ClassStatement  cs       = new ClassStatement(mod, mod.getContext());
		final IdentExpression capitalX = IdentExpression.forString("X");
		cs.setName(capitalX);
		final FunctionDef fd = new FunctionDef(cs, cs.getContext());
		final Context ctx1 = fd.getContext();
		fd.setName(IdentExpression.forString("main"));
		final FunctionDef fd2 = new FunctionDef(cs, cs.getContext());
		fd2.setName(IdentExpression.forString("foo"));

//		GeneratedFunction generatedFunction = new GeneratedFunction(fd);
//		TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(cs));
//		generatedFunction.addVariableTableEntry("x", VariableTableType.VAR, tte, cs);

		//
		//
		//

		final VariableSequence seq = new VariableSequence(ctx1);
		final VariableStatement vs = seq.next();
		final IdentExpression x = IdentExpression.forString("x");
		vs.setName(x);
		final ProcedureCallExpression pce2 = new ProcedureCallExpression();
		pce2.setLeft(capitalX);
		vs.initial(pce2);
		final IBinaryExpression e = ExpressionBuilder.build(x, ExpressionKind.ASSIGNMENT, pce2);

		final IdentExpression foo = IdentExpression.forString("foo");
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		fd.scope(new Scope3(fd));
		fd.add(seq);
		fd.add(new StatementWrapper(pce2, ctx1, fd));
		fd2.scope(new Scope3(fd2));
		fd2.add(new StatementWrapper(pce, ctx2, fd2));

		ClassInvocation ci = new ClassInvocation(cs, null);
		ci = phase.registerClassInvocation(ci);
		final ProcTableEntry     pte2 = null;
		final FunctionInvocation fi   = new FunctionInvocation(fd, pte2, ci, generatePhase);
//		expect(fd.returnType()).andReturn(null);
		final FormalArgList formalArgList = new FormalArgList();
//		expect(fd.fal()).andReturn(formalArgList);
//		expect(fd.fal()).andReturn(formalArgList);
//		expect(fd2.returnType()).andReturn(null);
		final GeneratedFunction generatedFunction = generateFunctions.generateFunction(fd, cs, fi);

/*
		InstructionArgument es = generateFunctions.simplify_expression(e, generatedFunction, ctx2);

		InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
*/

		//
		//
		//

		final LookupResultList lrl = new LookupResultList();
		lrl.add("foo", 1, fd2, ctx2);

		expect(ctx2.lookup("foo")).andReturn(lrl);

		final LookupResultList lrl2 = new LookupResultList();
		lrl2.add("X", 1, cs, ctx1);

		expect(ctx2.lookup("X")).andReturn(lrl2);

		//
		//
		//


		ClassInvocation invocation2 = new ClassInvocation(cs, null);
		invocation2 = phase.registerClassInvocation(invocation2);
		final ProcTableEntry pte3 = null;
		final FunctionInvocation fi2 = new FunctionInvocation(fd2, pte3, invocation2, generatePhase);
		final GeneratedFunction generatedFunction2 = generateFunctions.generateFunction(fd2, fd2.getParent(), fi2);//new GeneratedFunction(fd2);
//		generatedFunction2.addVariableTableEntry("self", VariableTableType.SELF, null, null);
//		final TypeTableEntry type = null;
//		int res = generatedFunction2.addVariableTableEntry("Result", VariableTableType.RESULT, type, null);

		//
		//
		//

		replay(ctx2);

		//
		//
		//

		final IdentIA identIA = new IdentIA(0, generatedFunction);

		final DeduceTypes2 d2 = new DeduceTypes2(mod, phase);

		generatedFunction.getVarTableEntry(0).setConstructable(generatedFunction.getProcTableEntry(0));
		identIA.getEntry().setCallablePTE(generatedFunction.getProcTableEntry(1));

		d2.resolveIdentIA2_(ctx2, identIA, generatedFunction, new FoundElement(phase) {
			@Override
			public void foundElement(final OS_Element e) {
				assert e == fd2;
			}

			@Override
			public void noFoundElement() {
				assert false;
			}
		});

		verify(ctx2);
	}

}

//
//
//
