package tripleo.elijah_durable_pancake.compilation_runner;

import tripleo.elijah.comp.Compilation;

public interface CompilationChange {
	void apply(final Compilation c);
}
