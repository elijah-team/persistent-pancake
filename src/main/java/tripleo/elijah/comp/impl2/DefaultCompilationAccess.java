package tripleo.elijah.comp.impl2;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.CA_writeLogs;
import tripleo.elijah.comp.i.Compilation;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;

import java.util.List;

public class DefaultCompilationAccess implements ICompilationAccess {
	protected final Compilation compilation;

	public DefaultCompilationAccess(final Compilation aCompilation) {
		compilation = aCompilation;
	}

	@Override
	@NotNull
	public ElLog.Verbosity testSilence() {
		//final boolean isSilent = compilation.silent; // TODO No such thing. silent is a local var
		final boolean isSilent = false; // TODO fix this

		return isSilent ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	@Override
	public Compilation getCompilation() {
		return compilation;
	}

	@Override
	public void writeLogs() {
		final boolean     silent = testSilence() == ElLog.Verbosity.SILENT;
		final List<ElLog> logs   = compilation._elLogs();

		CA_writeLogs.apply(silent, logs, compilation);
	}

	@Override
	public List<IFunctionMapHook> functionMapHooks() {
		return compilation.getDeducePhase().functionMapHooks;
	}

	@Override
	public Stages getStage() {
		return getCompilation()._cfg().stage;
	}
}
