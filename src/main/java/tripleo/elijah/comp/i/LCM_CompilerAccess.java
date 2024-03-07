package tripleo.elijah.comp.i;

import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_durable_pancake.comp.Compilation0101;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

public interface LCM_CompilerAccess {
	Compilation0101 c();

	CompilationRunner cr();

	EDP_Compilation.CompilationConfig cfg();
}
