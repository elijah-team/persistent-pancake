package tripleo.elijah.comp.i;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.DStageProcess;
import tripleo.elijah.comp.EmptyProcess;
import tripleo.elijah.comp.OStageProcess;
import tripleo.elijah.comp.RuntimeProcess;
import tripleo.elijah.comp.internal.ProcessRecord;
import tripleo.elijah.util.NotImplementedException;

public enum Stages {
	E("E") {
		@Override
		public void writeLogs(final ICompilationAccess aCompilationAccess) {
			NotImplementedException.raise();
		}

		@Override
		public RuntimeProcess getProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			return new EmptyProcess(aCa, aPr);
		}
	},
	D("D") {
		@Override
		public void writeLogs(final ICompilationAccess aCompilationAccess) {
			aCompilationAccess.writeLogs();
		}

		@Override
		public @NotNull RuntimeProcess getProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			return new DStageProcess(aCa, aPr);
		}
	},
	S("S") {
		@Override
		public void writeLogs(final ICompilationAccess aCompilationAccess) {
			aCompilationAccess.writeLogs();
		}

		@Override
		public RuntimeProcess getProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			throw new NotImplementedException();
		}
	},  // ??
	O("O") {
		@Override
		public void writeLogs(final ICompilationAccess aCompilationAccess) {
			aCompilationAccess.writeLogs();
		}

		@Override
		public RuntimeProcess getProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			return new OStageProcess(aCa, aPr);
		}
	}  // Output
	;

	private final String s;

	@Contract(pure = true)
	Stages(final String aO) {
		s = aO;
	}

	public abstract void writeLogs(final ICompilationAccess aCompilationAccess);

	public abstract RuntimeProcess getProcess(final ICompilationAccess aCa, final ProcessRecord aPr);
}
