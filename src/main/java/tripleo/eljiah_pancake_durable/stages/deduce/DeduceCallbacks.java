package tripleo.eljiah_pancake_durable.stages.deduce;

import org.jetbrains.annotations.NotNull;

import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.stages.gen_fn.BaseGeneratedFunction;
import tripleo.eljiah_pancake_durable.stages.gen_fn.ProcTableEntry;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;

public class DeduceCallbacks {

	public static void callback_001(@NotNull FunctionInvocation functionInvocation, FunctionDef aFunctionDef,
	                                ProcTableEntry aProcTableEntry, @NotNull BaseGeneratedFunction aGeneratedFunction,
	                                DeduceTypes2 deduceTypes2, @NotNull DeducePhase phase) {
		// TODO Auto-generated method stub

		final ClassInvocation ci = functionInvocation.getClassInvocation();
		final NamespaceInvocation nsi = functionInvocation.getNamespaceInvocation();

		// do we register?? probably not
		assert ci != null || nsi != null;


		@NotNull
		final FunctionInvocation fi = deduceTypes2.newFunctionInvocation(aFunctionDef, aProcTableEntry,
				DeduceTypes2.choose(ci, nsi), phase);

		{
			// NOTE 11/18 we are creating/harassing the garbage collector just to fail...
			// this is a NOTE instead of a FIXME b/c in some languages it's OK
			if (FunctionInvocation.sameAs(functionInvocation, fi)) {
				SimplePrintLoggerToRemoveSoon.println_err2("955 It seems like we are generating the same thing...");
			} else {
				final int ok = 2;
			}

		}
		aGeneratedFunction.addDependentFunction(fi);
	}
	
}
