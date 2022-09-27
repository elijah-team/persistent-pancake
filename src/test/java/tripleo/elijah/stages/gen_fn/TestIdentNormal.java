/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.FoundElement;
import tripleo.elijah.stages.instructions.IdentIA;
import tripleo.elijah.stages.instructions.InstructionArgument;
import tripleo.elijah.test_help.Boilerplate;
import tripleo.elijah.util.NotImplementedException;

import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * Created 3/4/21 3:53 AM
 */
public class TestIdentNormal {

	@Test(expected = IllegalStateException.class) // TODO proves nothing
	public void test() {

		final FunctionDef fd   = mock(FunctionDef.class);
		final Context ctx1 = mock(Context.class);
		final Context ctx2 = mock(Context.class);

		final Boilerplate boilerplate = new Boilerplate();
		boilerplate.get();
		boilerplate.getGenerateFiles(boilerplate.defaultMod());

		final GeneratePhase   generatePhase = boilerplate.pipelineLogic.generatePhase;

		final GenerateFunctions generateFunctions = new GenerateFunctions(generatePhase, boilerplate.defaultMod(), boilerplate.pipelineLogic);

		final GeneratedFunction generatedFunction = new GeneratedFunction(fd);
		final VariableSequence  seq = new VariableSequence(ctx1);
		final VariableStatement vs  = new VariableStatement(seq);
		final IdentExpression   x   = IdentExpression.forString("x");
		vs.setName(x);
		final IdentExpression         foo = IdentExpression.forString("foo");
		final ProcedureCallExpression pce = new ProcedureCallExpression();
		pce.setLeft(new DotExpression(x, foo));

		final InstructionArgument                s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
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

		final DeducePhase  phase = boilerplate.pr.pipelineLogic.dp;
		final DeduceTypes2 d2    = new DeduceTypes2(boilerplate.defaultMod(), phase);

		final List<InstructionArgument> ss = BaseGeneratedFunction._getIdentIAPathList(identIA);
		d2.resolveIdentIA2_(ctx2, null, ss/*identIA*/, generatedFunction, new FoundElement(phase) {
			@Override
			public void foundElement(final OS_Element e) {
				System.out.println(e);
			}

			@Override
			public void noFoundElement() {
				NotImplementedException.raise();
			}
		});
	}

	@Test // TODO just a mess
	public void test2() {
		Boilerplate boilerplate = new Boilerplate();
		boilerplate.get();
		boilerplate.getGenerateFiles(boilerplate.defaultMod());

//		Compilation comp = new Compilation(new StdErrSink(), new IO());
//		OS_Module mod = comp.moduleBuilder().build();
////		FunctionDef fd = mock(FunctionDef.class);
//		Context ctx2 = mock(Context.class);

//		final ElLog.Verbosity verbosity1 = new Compilation(new StdErrSink(), new IO()).gitlabCIVerbosity();
//		final PipelineLogic pl = new PipelineLogic(verbosity1);
//		final GeneratePhase generatePhase = new GeneratePhase(verbosity1, pl);
		DeducePhase phase = boilerplate.pipelineLogic.dp;

//		GenerateFunctions generateFunctions = generatePhase.getGenerateFunctions(mod);

		//
		//
		//

//		ClassStatement cs = new ClassStatement(mod, mod.getContext());
//		final IdentExpression capitalX = IdentExpression.forString("X");
//		cs.setName(capitalX);
//		FunctionDef fd = new FunctionDef(cs, cs.getContext());
//		Context ctx1 = fd.getContext();
//		fd.setName(IdentExpression.forString("main"));
//		FunctionDef fd2 = new FunctionDef(cs, cs.getContext());
//		fd2.setName(IdentExpression.forString("foo"));
//
////		GeneratedFunction generatedFunction = new GeneratedFunction(fd);
////		TypeTableEntry tte = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.TRANSIENT, new OS_Type(cs));
////		generatedFunction.addVariableTableEntry("x", VariableTableType.VAR, tte, cs);
//
//		//
//		//
//		//
//
//		VariableSequence seq = new VariableSequence(ctx1);
//		VariableStatement vs = seq.next();
//		final IdentExpression x = IdentExpression.forString("x");
//		vs.setName(x);
//		ProcedureCallExpression pce2 = new ProcedureCallExpression();
//		pce2.setLeft(capitalX);
//		vs.initial(pce2);
//		IBinaryExpression e = ExpressionBuilder.build(x, ExpressionKind.ASSIGNMENT, pce2);
//
//		final IdentExpression foo = IdentExpression.forString("foo");
//		ProcedureCallExpression pce = new ProcedureCallExpression();
//		pce.setLeft(new DotExpression(x, foo));
//
//		fd.scope(new Scope3(fd));
//		fd.add(seq);
//		fd.add(new StatementWrapper(pce2, ctx1, fd));
//		fd2.scope(new Scope3(fd2));
//		fd2.add(new StatementWrapper(pce, ctx2, fd2));
//
//		ClassInvocation ci = new ClassInvocation(cs, null);
//		ci = phase.registerClassInvocation(ci);
//		ProcTableEntry pte2 = null;
//		FunctionInvocation fi = new FunctionInvocation(fd, pte2, ci, generatePhase);
////		expect(fd.returnType()).andReturn(null);
//		final FormalArgList formalArgList = new FormalArgList();
////		expect(fd.fal()).andReturn(formalArgList);
////		expect(fd.fal()).andReturn(formalArgList);
////		expect(fd2.returnType()).andReturn(null);
//		GeneratedFunction generatedFunction = generateFunctions.generateFunction(fd, cs, fi);
//
///*
//		InstructionArgument es = generateFunctions.simplify_expression(e, generatedFunction, ctx2);
//
//		InstructionArgument s = generateFunctions.simplify_expression(pce, generatedFunction, ctx2);
//*/
//
//		//
//		//
//		//
//
//		LookupResultList lrl = new LookupResultList();
//		lrl.add("foo", 1, fd2, ctx2);
//
//		expect(ctx2.lookup("foo")).andReturn(lrl);
//
//		LookupResultList lrl2 = new LookupResultList();
//		lrl2.add("X", 1, cs, ctx1);
//
//		expect(ctx2.lookup("X")).andReturn(lrl2);
//
//		//
//		//
//		//
//
//
//		ClassInvocation invocation2 = new ClassInvocation(cs, null);
//		invocation2 = phase.registerClassInvocation(invocation2);
//		ProcTableEntry pte3 = null;
//		FunctionInvocation fi2 = new FunctionInvocation(fd2, pte3, invocation2, generatePhase);
//		GeneratedFunction generatedFunction2 = generateFunctions.generateFunction(fd2, fd2.getParent(), fi2);//new GeneratedFunction(fd2);
////		generatedFunction2.addVariableTableEntry("self", VariableTableType.SELF, null, null);
////		final TypeTableEntry type = null;
////		int res = generatedFunction2.addVariableTableEntry("Result", VariableTableType.RESULT, type, null);
//
//		//
//		//
//		//
//
//		replay(ctx2);
//
//		//
//		//
//		//
//
//		IdentIA identIA = new IdentIA(0, generatedFunction);
//
//		DeduceTypes2 d2 = new DeduceTypes2(mod, phase);
//
//		generatedFunction.getVarTableEntry(0).setConstructable(generatedFunction.getProcTableEntry(0));
//		identIA.getEntry().setCallablePTE(generatedFunction.getProcTableEntry(1));
//
//		d2.resolveIdentIA2_(ctx2, identIA, generatedFunction, new FoundElement(phase) {
//			@Override
//			public void foundElement(OS_Element e) {
//				assert e == fd2;
//			}
//
//			@Override
//			public void noFoundElement() {
//				assert false;
//			}
//		});
//
//		verify(ctx2);
	}

}

//
//
//
