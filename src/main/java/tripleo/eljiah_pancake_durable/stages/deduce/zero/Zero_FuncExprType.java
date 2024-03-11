package tripleo.eljiah_pancake_durable.stages.deduce.zero;

import org.jetbrains.annotations.NotNull;

import tripleo.eljiah_pancake_durable.lang.BaseFunctionDef;
import tripleo.eljiah_pancake_durable.lang.types.OS_FuncExprType;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.FunctionInvocation;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenerateFunctions;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.WlGenerateFunction;

public class Zero_FuncExprType implements IZero {
	private final OS_FuncExprType funcExprType;

	public Zero_FuncExprType(final OS_FuncExprType aFuncExprType) {
		funcExprType = aFuncExprType;
	}

	public GeneratedFunction genCIForGenType2(final DeduceTypes2 aDeduceTypes2) {
		final @NotNull GenerateFunctions genf = aDeduceTypes2.getGenerateFunctions(funcExprType.getElement().getContext().module());
		final FunctionInvocation fi = aDeduceTypes2.newFunctionInvocation((BaseFunctionDef) funcExprType.getElement(),
		  null,
		  null,
		  aDeduceTypes2._phase().generatePhase);
		final WlGenerateFunction gen = new WlGenerateFunction(genf, fi, aDeduceTypes2._phase().codeRegistrar);
		gen.run(null);
		return gen.getResult();
	}
}
