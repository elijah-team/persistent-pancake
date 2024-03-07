package tripleo.elijah.stages.deduce;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import tripleo.elijah.context_mocks.FunctionContextMock;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.FunctionDef;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.OS_Element;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.lang.VariableSequence;
import tripleo.elijah.lang.VariableStatement;
import tripleo.elijah.lang.types.OS_BuiltinType;
import tripleo.elijah.lang.types.OS_UserClassType;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.gen_fn.GeneratePhase;
import tripleo.elijah.stages.gen_fn.GeneratedFunction;
import tripleo.elijah.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.stages.gen_fn.TypeTableEntry;
import tripleo.elijah.stages.gen_fn.VariableTableEntry;
import tripleo.elijah.stages.instructions.VariableTableType;
import tripleo.elijah.util.Helpers;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;

public class DoAssignCall_ArgsIdent1_Test {
	/*
		model and test

			r f1 = factorial(b1)
	 */

	@Test
	void f1_eq_factorial_b1() {
		final EDP_Compilation c   = new EDP_Compilation(new EDP_ErrSink(), new EDP_IO());
		final OS_Module       mod = mock(OS_Module.class);
		final AccessBus       accessBus           = new AccessBus(c._access());
		final PipelineLogic   pipelineLogic = new PipelineLogic(accessBus);
		final GeneratePhase   generatePhase = new GeneratePhase(accessBus, pipelineLogic);
		final DeducePhase     phase         = new DeducePhase(accessBus, pipelineLogic, generatePhase);

		expect(mod.getCompilation()).andReturn(c);
		expect(mod.getFileName()).andReturn("foo.elijah");
//		expect(mod.getCompilation()).andReturn(c);
//		expect(mod.getCompilation()).andReturn(c);
//		expect(mod.getCompilation()).andReturn(c);

		replay(mod);

		final DeduceTypes2 d = new DeduceTypes2(mod, phase);

		final FunctionDef fd = mock(FunctionDef.class);
//		final GeneratedFunction generatedFunction = mock(GeneratedFunction.class);
		final GeneratedFunction generatedFunction = new GeneratedFunction(fd);
		final TypeTableEntry    self_type         = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, new OS_UserClassType(mock(ClassStatement.class)));
		final int               index_self        = generatedFunction.addVariableTableEntry("self", VariableTableType.SELF, self_type, null);
		final TypeTableEntry    result_type       = null;
		final int               index_result      = generatedFunction.addVariableTableEntry("Result", VariableTableType.RESULT, result_type, null);
		final OS_Type           sts_int           = new OS_BuiltinType(BuiltInTypes.SystemInteger);
		final TypeTableEntry    b1_type           = generatedFunction.newTypeTableEntry(TypeTableEntry.Type.SPECIFIED, sts_int);
		final OS_Type           b1_attached       = sts_int;
		b1_type.setAttached(sts_int, d.resolver());

		final String          b1_name       = "b1";
		final int             index_b1 = generatedFunction.addVariableTableEntry(b1_name, VariableTableType.VAR, b1_type, null);
		final Context ctx      = new FunctionContextMock();

		final LookupResultList  lrl_b1 = new LookupResultList();
		final VariableSequence  vs     = new VariableSequence();

		final Pair<VariableStatement, Context> x      = b_variable(vs, b1_name);
		final VariableStatement                result = x.getLeft();
		final Context                          b1_ctx = x.getRight();
		lrl_b1.add(b1_name, 1, result, b1_ctx);


		ctx.expect(b1_name, result).andContributeResolve(b1_ctx);

//		expect(ctx.lookup(b1_name)).andReturn(lrl_b1);

		replay(fd, /*generatedFunction,*/ b1_ctx);

		final TypeTableEntry vte_tte = null;
		final OS_Element     el      = null;

		final VariableTableEntry vte              = generatedFunction.getVarTableEntry(index_self);
		final int                instructionIndex = -1;
		final ProcTableEntry pte              = new ProcTableEntry(-2, null, null, new ArrayList()/*List_of()*/);
		final int            i                = 0;
		final TypeTableEntry tte = new TypeTableEntry(-3, TypeTableEntry.Type.SPECIFIED, null, null, null);
		final IdentExpression identExpression = Helpers.string_to_ident(b1_name); // TODO ctx

		d.do_assign_call_args_ident(generatedFunction, ctx, vte, instructionIndex, pte, i, tte, identExpression);

		d.onExitFunction(generatedFunction, ctx, ctx);

		verify(mod, fd /*,generatedFunction*/);
	}

	private static Pair<VariableStatement, Context> b_variable(final VariableSequence vs, final String aName) {
		final VariableStatement result = new VariableStatement(vs);
		result.setName(Helpers.string_to_ident(aName));

		final Context b1_ctx = mock(Context.class);
		return Pair.of(result,b1_ctx);
	}

}
