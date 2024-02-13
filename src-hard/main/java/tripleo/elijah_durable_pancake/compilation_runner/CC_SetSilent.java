package tripleo.elijah_durable_pancake.compilation_runner;

import tripleo.elijah.comp.Compilation;

public class CC_SetSilent implements CompilationChange {
	private final boolean flag;

	public CC_SetSilent(final boolean aB) {
		flag = aB;
	}

	@Override
	public void apply(final Compilation c) {
		c._cfg().silent = flag;
	}
}
