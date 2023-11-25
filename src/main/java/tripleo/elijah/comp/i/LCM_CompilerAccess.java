package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.internal.CompilationImpl;

public interface LCM_CompilerAccess {
	Compilation c();

	CompilationRunner cr();

	CompilationImpl.CompilationConfig cfg();
}
