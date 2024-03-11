package tripleo.elijah_durable_pancake.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.bus.CB_Action;
import tripleo.eljiah_pancake_durable.comp.bus.CB_OutputString;
import tripleo.eljiah_pancake_durable.comp.bus.CB_Process;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.comp.IO;
import tripleo.eljiah_pancake_durable.comp.ICompilationBus;
import tripleo.eljiah_pancake_durable.nextgen.inputtree.EIT_InputType;
import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.Maybe;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;
import tripleo.elijah.util.Operation2;
import tripleo.elijah.util.SimplePrintLoggerToRemoveSoon;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;
import tripleo.elijah_durable_pancake.comp.internal.EDP_ProgressSink;
import tripleo.elijah_durable_pancake.comp.queries.QueryEzFileToModule;
import tripleo.elijah_durable_pancake.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah_pancake.feb24.comp.CR_State;
import tripleo.elijah_pancake.feb24.comp.Startup;
import tripleo.elijah_prepan.compilation_runner.CR_Action;
import tripleo.elijah_prepan.compilation_runner.CR_AlmostComplete;
import tripleo.elijah_prepan.compilation_runner.CR_FindCIs;
import tripleo.elijah_prepan.compilation_runner.CompilationRunnerProcess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

/**
 * NOTE 24/01/12 I like how all CR_ stuff is here
 * - Fix @ 58 next
 */
public class CompilationRunner {
	final         Map<String, CompilerInstructions> fn2ci = new HashMap<>();
	private final Compilation         compilation;
	private final EDP_Compilation.CIS cis;
	private final CCI                 cci;
	private final ICompilationBus                   cb;

	private final CR_State st1;

	@Contract(pure = true)
	public CompilationRunner(final Compilation aCompilation, final EDP_Compilation.CIS a_cis, final ICompilationBus aCb, final Startup startup) {
		compilation = aCompilation;
		cis         = a_cis;
		final EDP_ProgressSink ps1 = new EDP_ProgressSink();
		cci = new CCI(compilation, a_cis, ps1);
		cb  = aCb;

		st1 = new CR_State(this, startup);
		startup.completeInitial(this);
	}

	public void start(final CompilerInstructions ci) throws Exception {
		cb.add(new CompilationRunnerProcess(this, st1, ci));
	}

	public void logProgress(final int number, final String text) {
		if (number == 130) return;

		System.err.println(MessageFormat.format("{0} {1}", number, text));
	}

	public void doFindCIs(final String[] args2, final ICompilationBus cb) {
		// TODO map + "extract"
//		final CR_State st1 = new CR_State();
		cb.add(new CA_doFindCIs(args2));
	}

	/*
	 * Design question:
	 *   - why push and return?
	 *     we only want to check error
	 *     utilize exceptions --> only one usage
	 *     or inline (esp use of Compilation)
	 */
	@NotNull
	public Operation<CompilerInstructions> findStdLib(final String prelude_name, final @NotNull Compilation c) {
		final ErrSink errSink = c.getErrSink();
		final IO      io      = c.getIO();

		// TODO stdlib path here
		final File local_stdlib = new File("lib_elijjah/lib-" + prelude_name + "/stdlib.ez");
		if (local_stdlib.exists()) {
			try {
				final Operation<CompilerInstructions> oci = realParseEzFile(local_stdlib.toString(), io.readFile(local_stdlib), local_stdlib, c);
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

	public Operation<CompilerInstructions> realParseEzFile(final String f,
	                                                       final InputStream s,
	                                                       final @NotNull File file,
	                                                       final Compilation c) {
		c.reports().addInput(() -> f, EIT_InputType.EZ_FILE);
		return __realParseEzFile2(f, s, file, c);
	}

	Operation<CompilerInstructions> __realParseEzFile2(final String f,
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
			final QueryEzFileToModuleParams       qp  = new QueryEzFileToModuleParams(f, s);
			final Operation<CompilerInstructions> cio = new QueryEzFileToModule(qp).calculate();

			if (cio.mode() == Mode.FAILURE) {
				final Throwable e = cio.failure();
				assert e != null;

				SimplePrintLoggerToRemoveSoon.println_err2(("parser exception: " + e));
				e.printStackTrace(System.err);
				//s.close();
				return cio;
			}

			final CompilerInstructions R = cio.success();
			R.setFilename(file.toString());
			fn2ci.put(absolutePath, R);
			return cio;
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

	@NotNull
	public List<CompilerInstructions> searchEzFiles(final @NotNull File directory, final ErrSink errSink, final IO io, final Compilation c) {
		final QuerySearchEzFiles                     q    = new QuerySearchEzFiles(c, errSink, io, this);
		final Operation2<List<CompilerInstructions>> olci = q.process(directory);

		if (olci.mode() == Mode.SUCCESS) {
			return olci.success();
		}

		errSink.reportDiagnostic(olci.failure());
		return List_of();
	}

	public Compilation _accessCompilation() {
		return compilation;
	}

	public void _almostComplete() {
		cis.almostComplete();
	}

	public ICompilationBus _access_cb() {
		return cb;
	}

	public void cci_accept(final Maybe<ILazyCompilerInstructions> aM2) {
		cci.accept(aM2);
	}

	private class CA_AlmostComplete implements CB_Action {
		private final CR_Action a = new CR_AlmostComplete(CompilationRunner.this);

		@Override
		public String name() {
			return a.name();
		}

		@Override
		public void execute() {
			a.execute(st1);
		}

		@Override
		public CB_OutputString[] outputStrings() {
			return new CB_OutputString[0];
		}
	}

	private class CA_FindCIs implements CB_Action {
		private final CR_Action a;

		public CA_FindCIs(final String[] aArgs2) {
			a = new CR_FindCIs(CompilationRunner.this, aArgs2);
		}

		@Override
		public String name() {
			return a.name();
		}

		@Override
		public void execute() {
			st1.cur = this;
			a.execute(st1);
			st1.cur = null;
		}

		@Override
		public CB_OutputString[] outputStrings() {
			return new CB_OutputString[0];
		}
	}

	private class CA_doFindCIs implements CB_Process {
		final CB_Action a;
		final CB_Action b;

		public CA_doFindCIs(final String[] aStrings) {
			a = new CA_FindCIs(aStrings);
			b = new CA_AlmostComplete();
		}

		@Override
		public List<CB_Action> steps() {
			return List_of(a, b);
		}
	}
}