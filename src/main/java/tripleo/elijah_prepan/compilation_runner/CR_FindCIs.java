package tripleo.elijah_prepan.compilation_runner;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.UnintendedUseException;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationRunner;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah.comp.ILazyCompilerInstructions;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.i.IProgressSink;
import tripleo.elijah.comp.i.ProgressSinkComponent;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.diagnostic.TooManyEz_ActuallyNone;
import tripleo.elijah.diagnostic.TooManyEz_BeSpecific;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;
import tripleo.elijah_pancake.feb24.comp.CR_State;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class CR_FindCIs implements CR_Action {
	private final CompilationRunner compilationRunner;
	private final String[]          args2;

	public CR_FindCIs(final CompilationRunner aCompilationRunner, final String[] aArgs2) {
		compilationRunner = aCompilationRunner;
		args2             = aArgs2;
	}

	public void execute(final CR_State st) {
		final Compilation c = st.ca().getCompilation();

		final IProgressSink ps = new IProgressSink() {
			@Override
			public void note(final int aCode, final ProgressSinkComponent aCci, final int aType, final Object[] aParams) {
				throw new UnintendedUseException("blank filler");
			}
		};

		final @NotNull ErrSink errSink = c.getErrSink();
		final @NotNull IO      io      = c.getIO();
		final ICompilationBus  cb      = compilationRunner._access_cb();
		CompilerInstructions   ez_file;
		for (final String file_name : args2) {
			final File    f        = new File(file_name);
			final boolean matches2 = Pattern.matches(".+\\.ez$", file_name);
			if (matches2) {
				final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(f, c);
				compilationRunner.cci_accept(new Maybe<>(ilci, null));

				cb.inst(ilci);
			} else {
				//errSink.reportError("9996 Not an .ez file "+file_name);
				if (f.isDirectory()) {
					final List<CompilerInstructions> ezs = compilationRunner.searchEzFiles(f, errSink, io, c);

					switch (ezs.size()) {
					case 0:
						final Diagnostic d_toomany = new TooManyEz_ActuallyNone();
						final Maybe<ILazyCompilerInstructions> m = new Maybe<>(null, d_toomany);
						compilationRunner.cci_accept(m);
						break;
					case 1:
						ez_file = ezs.get(0);
						final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(ez_file);
						compilationRunner.cci_accept(new Maybe<>(ilci, null));
						cb.inst(ilci);
						break;
					default:
						//final Diagnostic d_toomany = new TooManyEz_UseFirst();
						//add_ci(ezs.get(0));

						// more than 1 (negative is not possible)
						final Diagnostic d_toomany2 = new TooManyEz_BeSpecific();
						final Maybe<ILazyCompilerInstructions> m2 = new Maybe<>(null, d_toomany2);
						compilationRunner.cci_accept(m2);
						break;
					}
				} else
					errSink.reportError("9995 Not a directory " + f.getAbsolutePath());
			}
		}
	}

	@Override
	public String name() {
		return "find cis";
	}
}
