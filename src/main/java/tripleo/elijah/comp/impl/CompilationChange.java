package tripleo.elijah.comp.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.Compilation;
import tripleo.elijah.comp.impl2.Stages;

public interface CompilationChange {
	void apply(final Compilation c);

	class CC_SetStage implements CompilationChange {
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

	class CC_SetShowTree implements CompilationChange {
		private final boolean flag;

		public CC_SetShowTree(final boolean aB) {
			flag = aB;
		}

		@Override
		public void apply(final Compilation c) {
			c._cfg().showTree = flag;
		}
	}

	class CC_SetSilent implements CompilationChange {
		private final boolean flag;

		public CC_SetSilent(final boolean aB) {
			flag = aB;
		}

		@Override
		public void apply(final Compilation c) {
			c._cfg().silent = flag;
		}
	}
}

