package tripleo.elijah.comp.i;

import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.percy.CN_CompilerInputWatcher;

public interface CompilationClosure {
	void compilerInputWatcher_Event(CN_CompilerInputWatcher.e aE, CompilerInput aInput, Object aO);

	ErrSink errSink();

	Compilation getCompilation();

	IO io();
}
