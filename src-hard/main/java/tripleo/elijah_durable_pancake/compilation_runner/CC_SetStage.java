package tripleo.elijah_durable_pancake.compilation_runner;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah_prepan.compilation_runner.Stages;

public class CC_SetStage implements CompilationChange {
	private final String s;

	@Contract(pure = true)
	public CC_SetStage(final String aS) {
		s = aS;
	}

	@Override
	public void apply(final @NotNull Compilation c) {
		c._cfg().stage = Stages.valueOf(s);
	}
}
