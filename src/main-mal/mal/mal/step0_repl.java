package mal;

import java.io.IOException;

public class step0_repl {
	public static void main(final String[] args) {
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
			System.out.println(PRINT(RE(null, line)));
		}
	}

	// print
	public static String PRINT(final String exp) {
		return exp;
	}

	// repl
	public static String RE(final String env, final String str) {
		return EVAL(READ(str), env);
	}

	// eval
	public static String EVAL(final String ast, final String env) {
		return ast;
	}

	// read
	public static String READ(final String str) {
		return str;
	}
}
