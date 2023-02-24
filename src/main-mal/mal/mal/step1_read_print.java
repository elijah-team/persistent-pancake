package mal;

import mal.types.MalContinue;
import mal.types.MalThrowable;
import mal.types.MalVal;

import java.io.IOException;

public class step1_read_print {
	public static void main(final String[] args) throws MalThrowable {
		final String prompt = "user> ";

		if (args.length > 0 && args[0].equals("--raw")) {
			readline.mode = readline.Mode.JAVA;
		}
		while (true) {
			final String line;
			try {
				line = readline.readline(prompt);
				if (line == null) {
					continue;
				}
			} catch (final readline.EOFException e) {
				break;
			} catch (final IOException e) {
				System.out.println("IOException: " + e.getMessage());
				break;
			}
			try {
				System.out.println(PRINT(RE(null, line)));
			} catch (final MalContinue e) {
				continue;
			} catch (final MalThrowable t) {
				System.out.println("Error: " + t.getMessage());
				continue;
			} catch (final Throwable t) {
				System.out.println("Uncaught " + t + ": " + t.getMessage());
				continue;
			}
		}
	}

	// print
	public static String PRINT(final MalVal exp) {
		return printer._pr_str(exp, true);
	}

	// repl
	public static MalVal RE(final String env, final String str) throws MalThrowable {
		return EVAL(READ(str), env);
	}

	// eval
	public static MalVal EVAL(final MalVal ast, final String env) {
		return ast;
	}

	// read
	public static MalVal READ(final String str) throws MalThrowable {
		return reader.read_str(str);
	}
}
