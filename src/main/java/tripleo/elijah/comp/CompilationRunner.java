package tripleo.elijah.comp;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.diagnostic.TooManyEz_ActuallyNone;
import tripleo.elijah.comp.diagnostic.TooManyEz_BeSpecific;
import tripleo.elijah.comp.internal.ProcessRecord;
import tripleo.elijah.comp.queries.QueryEzFileToModule;
import tripleo.elijah.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.nextgen.query.Mode;
import tripleo.elijah.nextgen.query.Operation2;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static tripleo.elijah.util.Helpers.List_of;

public class CompilationRunner {
	final         Map<String, CompilerInstructions> fn2ci = new HashMap<String, CompilerInstructions>();
	private final Compilation                       compilation;
	private final Compilation.CIS                   cis;
	private final CCI                               cci;
	private final ICompilationBus                   cb;

	@Contract(pure = true)
	public CompilationRunner(final Compilation aCompilation, final Compilation.CIS a_cis, final ICompilationBus aCb) {
		compilation = aCompilation;
		cis         = a_cis;
		cci         = new CCI(compilation, a_cis);
		cb          = aCb;
	}

	void start(final CompilerInstructions ci, final boolean do_out) throws Exception {
		final CR_State st1 = new CR_State();

		// 1. find stdlib
		//   -- question placement
		//   -- ...
		add(cb, st1, new CR_FindStdlibAction());

		// 2. process the initial
		add(cb, st1, new CR_ProcessInitialAction(ci, do_out));

		// 3. do rest
		add(cb, st1, new CR_RunBetterAction());
	}

	void add(@NotNull final ICompilationBus cb, final CR_State st, final CR_Action a) {
		cb.add(new ICompilationBus.CB_Action() {
			@Override
			public void execute() {
				a.execute(st);
			}
		});
	}

	private void logProgress(final int number, final String text) {
		if (number == 130) return;

		System.err.println(MessageFormat.format("{0} {1}", number, text));
	}

	public void doFindCIs(final String[] args2, final ICompilationBus cb) {
//		// TODO map + "extract"
//		find_cis(args2, compilation, errSink1, io, cb);

		final CR_State st1 = new CR_State();
		add(cb, st1, new CR_FindCIs(args2));
		add(cb, st1, new CR_AlmostComplete());
	}

	/*
	 * Design question:
	 *   - why push and return?
	 *     we only want to check error
	 *     utilize exceptions --> only one usage
	 *     or inline (esp use of Compilation)
	 */
	private @NotNull Operation<CompilerInstructions> findStdLib(final String prelude_name, final @NotNull Compilation c) {
		final ErrSink errSink = c.getErrSink();
		final IO      io      = c.getIO();

		// TODO stdlib path here
		final File local_stdlib = new File("lib_elijjah/lib-" + prelude_name + "/stdlib.ez");
		if (local_stdlib.exists()) {
			try {
				final Operation<CompilerInstructions> oci = realParseEzFile(local_stdlib.getName(), io.readFile(local_stdlib), local_stdlib, c);
				if (oci.mode() == Mode.SUCCESS) {
					c.pushItem(oci.success());
					return oci;
				}
			} catch (final Exception e) {
				return Operation.failure(e);
			}
		}

		return Operation.failure(new Exception() {
			public String message() {
				return "No stdlib found";
			}
		});
	}

	private @NotNull List<CompilerInstructions> searchEzFiles(final @NotNull File directory, final ErrSink errSink, final IO io, final Compilation c) {
		final QuerySearchEzFiles                     q    = new QuerySearchEzFiles(c, errSink, io, this);
		final Operation2<List<CompilerInstructions>> olci = q.process(directory);

		if (olci.mode() == Mode.SUCCESS) {
			return olci.success();
		}

		errSink.reportDiagnostic(olci.failure());
		return List_of();
	}

	public Operation<CompilerInstructions> realParseEzFile(final String f,
	                                                       final InputStream s,
	                                                       final @NotNull File file,
	                                                       final Compilation c) {
		final String absolutePath;
		try {
			absolutePath = file.getCanonicalFile().toString();
		} catch (final IOException aE) {
			//throw new RuntimeException(aE);
			return Operation.failure(aE);
		}

		if (fn2ci.containsKey(absolutePath)) { // don't parse twice
			return Operation.success(fn2ci.get(absolutePath));
		}

		try {
			try {
				final Operation<CompilerInstructions> cio = parseEzFile_(f, s);

				if (cio.mode() == Mode.FAILURE) {
					final Exception e = cio.failure();
					assert e != null;

					tripleo.elijah.util.Stupidity.println_err2(("parser exception: " + e));
					e.printStackTrace(System.err);
					//s.close();
					return cio;
				}

				final CompilerInstructions R = cio.success();
				R.setFilename(file.toString());
				fn2ci.put(absolutePath, R);
				return cio;
			} catch (final ANTLRException e) {
				tripleo.elijah.util.Stupidity.println_err2(("parser exception: " + e));
				e.printStackTrace(System.err);
				return Operation.failure(e);
			}
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (final IOException aE) {
					// TODO return inside finally: is this ok??
					return new Operation<>(null, aE, Mode.FAILURE);
				}
			}
		}
	}

	private Operation<CompilerInstructions> parseEzFile_(final String f, final InputStream s) throws RecognitionException, TokenStreamException {
		final QueryEzFileToModuleParams qp = new QueryEzFileToModuleParams(f, s);
		return new QueryEzFileToModule(qp).calculate();
	}

	public interface CR_Action {
		void attach(CompilationRunner cr);

		void execute(CR_State st);
	}

	class CR_State {
		ICompilationAccess ca;
		ProcessRecord      pr;
		RuntimeProcesses   rt;
	}

	class CR_AlmostComplete implements CR_Action {

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			cis.almostComplete();
		}
	}

	class CR_FindCIs implements CR_Action {

		private final String[] args2;

		CR_FindCIs(final String[] aArgs2) {
			args2 = aArgs2;
		}

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			find_cis(args2, st.ca.getCompilation(), st.ca.getCompilation().getErrSink(), st.ca.getCompilation().getIO(), cb);
		}

		protected void find_cis(final @NotNull String @NotNull [] args2,
		                        final @NotNull Compilation c,
		                        final @NotNull ErrSink errSink,
		                        final @NotNull IO io,
		                        final ICompilationBus cb) {
			CompilerInstructions ez_file;
			for (int i = 0; i < args2.length; i++) {
				final String  file_name = args2[i];
				final File    f         = new File(file_name);
				final boolean matches2  = Pattern.matches(".+\\.ez$", file_name);
				if (matches2) {
					final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(f, c);
					cci.accept(new Maybe<>(ilci, null));

					cb.inst(ilci);
				} else {
					//errSink.reportError("9996 Not an .ez file "+file_name);
					if (f.isDirectory()) {
						final List<CompilerInstructions> ezs = searchEzFiles(f, errSink, io, c);

						switch (ezs.size()) {
						case 0:
							final Diagnostic d_toomany = new TooManyEz_ActuallyNone();
							final Maybe<ILazyCompilerInstructions> m = new Maybe<>(null, d_toomany);
							cci.accept(m);
							break;
						case 1:
							ez_file = ezs.get(0);
							final ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(ez_file);
							cci.accept(new Maybe<>(ilci, null));
							cb.inst(ilci);
							break;
						default:
							//final Diagnostic d_toomany = new TooManyEz_UseFirst();
							//add_ci(ezs.get(0));

							// more than 1 (negative is not possible)
							final Diagnostic d_toomany2 = new TooManyEz_BeSpecific();
							final Maybe<ILazyCompilerInstructions> m2 = new Maybe<>(null, d_toomany2);
							cci.accept(m2);
							break;
						}
					} else
						errSink.reportError("9995 Not a directory " + f.getAbsolutePath());
				}
			}
		}
	}

	private class CR_FindStdlibAction implements CR_Action {

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			final Operation<CompilerInstructions> x = findStdLib(Compilation.CompilationAlways.defaultPrelude(), compilation);
			if (x.mode() == Mode.FAILURE) {
				compilation.getErrSink().exception(x.failure());
				return;
			}
			logProgress(130, "GEN_LANG: " + x.success().genLang());
		}
	}

	private class CR_ProcessInitialAction implements CR_Action {
		private final CompilerInstructions ci;
		private final boolean              do_out;

		public CR_ProcessInitialAction(final CompilerInstructions aCi, final boolean aDo_out) {
			ci     = aCi;
			do_out = aDo_out;
		}

		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			try {
				compilation.use(ci, do_out);
			} catch (final Exception aE) {
				throw new RuntimeException(aE); // FIXME
			}
		}
	}

	private class CR_RunBetterAction implements CR_Action {
		@Override
		public void attach(final CompilationRunner cr) {

		}

		@Override
		public void execute(final CR_State st) {
			st.ca = new DefaultCompilationAccess(compilation);
			st.pr = new ProcessRecord(st.ca);
			st.rt = StageToRuntime.get(st.ca.getStage(), st.ca, st.pr);

			try {
				st.rt.run_better();
			} catch (final Exception aE) {
				throw new RuntimeException(aE); // FIXME
			}
		}
	}
}
