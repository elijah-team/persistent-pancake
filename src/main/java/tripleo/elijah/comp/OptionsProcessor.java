package tripleo.elijah.comp;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.diagnostic.TooManyEz_ActuallyNone;
import tripleo.elijah.comp.diagnostic.TooManyEz_BeSpecific;
import tripleo.elijah.comp.queries.QueryEzFileToModule;
import tripleo.elijah.comp.queries.QueryEzFileToModuleParams;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static tripleo.elijah.nextgen.query.Mode.FAILURE;
import static tripleo.elijah.nextgen.query.Mode.SUCCESS;

public interface OptionsProcessor {
	void run(Compilation c, final List<String> args, final IO io, final Consumer<Boolean> aInstructionCompleter) throws Exception;

//	String[] resultantArray();

	abstract class DefaultOptionsProcessor implements OptionsProcessor {
		protected void doFindCIs(final Compilation c, final String[] args2, final ErrSink aErrSink1, final IO io, final Consumer<Boolean> aInstructionCompleter) throws Exception {
			find_cis(args2, mcci -> {
				if (mcci.isException()) return;

				final ILazyCompilerInstructions cci = mcci.o;
				final CompilerInstructions      ci  = cci.get();

				System.err.println("*** "+ci.getName());

				c.pushItem(ci);
				//add_ci(ci);
			}, c, aErrSink1, io);

			aInstructionCompleter.accept(true);
		}

		private void find_cis(final @NotNull String[] args2,
							  final @NotNull Consumer<Maybe<ILazyCompilerInstructions>> cci,
							  final @NotNull Compilation c,
							  final @NotNull ErrSink errSink, final IO io) {
			CompilerInstructions ez_file;
			for (int i = 0; i < args2.length; i++) {
				final String  file_name = args2[i];
				final File    f         = new File(file_name);
				final boolean matches2  = Pattern.matches(".+\\.ez$", file_name);
				if (matches2) {
					ILazyCompilerInstructions ilci = ILazyCompilerInstructions.of(f, c);
					cci.accept(new Maybe<>(ilci, null));
					//add_ci(parseEzFile(f, file_name, errSink));
				} else {
//						eee.reportError("9996 Not an .ez file "+file_name);
					if (f.isDirectory()) {
						final List<CompilerInstructions> ezs = searchEzFiles(f, errSink, io, c);
						if (ezs.size() > 1) {
/*
						final Diagnostic d_toomany = new TooManyEz_UseFirst();
						add_ci(ezs.get(0));
*/
							final Diagnostic                       d_toomany = new TooManyEz_BeSpecific();
							final Maybe<ILazyCompilerInstructions> m         = new Maybe<>(null, d_toomany);
							cci.accept(m);
						} else if (ezs.size() == 0) {
							final Diagnostic                       d_toomany = new TooManyEz_ActuallyNone();
							final Maybe<ILazyCompilerInstructions> m         = new Maybe<>(null, d_toomany);
							cci.accept(m);
						} else {
							ez_file = ezs.get(0);
							cci.accept(new Maybe<>(ILazyCompilerInstructions.of(ez_file), null));
						}
					} else
						errSink.reportError("9995 Not a directory " + f.getAbsolutePath());
				}
			}
		}

		private @NotNull List<CompilerInstructions> searchEzFiles(final @NotNull File directory, final ErrSink errSink, final IO io, final Compilation c) {
			final List<CompilerInstructions> R = new ArrayList<CompilerInstructions>();
			final FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(final File file, final String s) {
					final boolean matches2 = Pattern.matches(".+\\.ez$", s);
					return matches2;
				}
			};
			final String[] list = directory.list(filter);
			if (list != null) {
				for (final String file_name : list) {
					try {
						final File                 file   = new File(directory, file_name);
						final CompilerInstructions ezFile = parseEzFile(file, file.toString(), errSink, io, c);
						if (ezFile != null)
							R.add(ezFile);
						else
							errSink.reportError("9995 ezFile is null " + file);
					} catch (final Exception e) {
						errSink.exception(e);
					}
				}
			}
			return R;
		}

		public @NotNull Operation<CompilerInstructions> parseEzFile1(final @NotNull File f,
																	 final String file_name,
																	 final ErrSink errSink,
																	 final IO io,
																	 final Compilation c) {
			System.out.printf("   %s%n", f.getAbsolutePath());
			if (!f.exists()) {
				errSink.reportError(
						"File doesn't exist " + f.getAbsolutePath());
				return null;
			} else {
				final Operation<CompilerInstructions> om = realParseEzFile(file_name/*, io.readFile(f), f*/, io, c);
				return om;
			}
		}

		@Nullable CompilerInstructions parseEzFile(final @NotNull File f, final String file_name, final ErrSink errSink, final IO io, final Compilation c) throws Exception {
			final Operation<CompilerInstructions> om = parseEzFile1(f, file_name, errSink, io, c);

			final CompilerInstructions             m;

			if (om.mode() == SUCCESS) {
				m = om.success();

/*
			final String prelude;
			final String xprelude = m.genLang();
			System.err.println("230 " + prelude);
			if (xprelude == null)
				prelude = CompilationAlways.defaultPrelude(); // TODO should be java for eljc
			else
				prelude = null;
*/
			} else {
				m = null;
			}

			return m;
		}

		private Operation<CompilerInstructions> realParseEzFile(final String f, final @NotNull IO io, final Compilation c) {
			final File                            file = new File(f);

			try {
				final Operation<CompilerInstructions> os;
				os = realParseEzFile(f, io.readFile(file), file, c);
				return os;
			} catch (FileNotFoundException aE) {
				final Operation<CompilerInstructions> ofl;
				ofl = new Operation<CompilerInstructions>(null, aE, FAILURE);
				return ofl;
			}
		}

		public Operation<CompilerInstructions> realParseEzFile(final String f,
															   final InputStream s,
															   final @NotNull File file,
															   final Compilation c) {
			final String absolutePath;
			try {
				absolutePath = file.getCanonicalFile().toString();
			} catch (IOException aE) {
				//throw new RuntimeException(aE);
				return Operation.failure(aE);
			}

			if (c.fn2ci.containsKey(absolutePath)) { // don't parse twice
				return Operation.success(c.fn2ci.get(absolutePath));
			}

			try {
				try {
					final Operation<CompilerInstructions> cio = parseEzFile_(f, s);

					if (cio.mode() != SUCCESS) {
						final Exception e = cio.failure();
						assert e != null;

						System.err.println(("parser exception: " + e));
						e.printStackTrace(System.err);
						//s.close();
						return cio;
					}

					final CompilerInstructions R = cio.success();
					R.setFilename(file.toString());
					c.fn2ci.put(absolutePath, R);
					return cio;
				} catch (final ANTLRException e) {
					System.err.println(("parser exception: " + e));
					e.printStackTrace(System.err);
					return Operation.failure(e);
				}
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException aE) {
						// TODO return inside finally: is this ok??
						return new Operation<>(null, aE, FAILURE);
					}
				}
			}
		}

		public boolean findStdLib(final String prelude_name, final Compilation c, final ErrSink errSink, final IO io) {
			final File local_stdlib = new File("lib_elijjah/lib-" + prelude_name + "/stdlib.ez");
			if (local_stdlib.exists()) {
				try {
					final Operation<CompilerInstructions> oci = realParseEzFile(local_stdlib.getName(), io.readFile(local_stdlib), local_stdlib, c);
					if (oci.mode() == SUCCESS) {
						c.pushItem(oci.success());
						return true;
					}
				} catch (final Exception e) {
					errSink.exception(e);
				}
			}
			return false;
		}

		private Operation<CompilerInstructions> parseEzFile_(final String f, final InputStream s) throws RecognitionException, TokenStreamException {
			final QueryEzFileToModuleParams qp = new QueryEzFileToModuleParams(f, s);
			return new QueryEzFileToModule(qp).calculate();
		}

	}
}
