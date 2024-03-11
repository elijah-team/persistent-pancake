package tripleo.elijah_durable_pancake.compilation_runner;

import tripleo.eljiah_pancake_durable.comp.Compilation;

public interface CompilationChange {
	void apply(final Compilation c);
}
