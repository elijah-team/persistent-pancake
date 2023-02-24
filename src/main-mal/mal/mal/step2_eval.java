package mal;

import mal.types.ILambda;
import mal.types.MalContinue;
import mal.types.MalError;
import mal.types.MalFunction;
import mal.types.MalHashMap;
import mal.types.MalInteger;
import mal.types.MalList;
import mal.types.MalSymbol;
import mal.types.MalThrowable;
import mal.types.MalVal;
import mal.types.MalVector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class step2_eval {
	static MalFunction add      = new MalFunction() {
		public MalVal apply(final MalList a) throws MalThrowable {
			return ((MalInteger) a.nth(0)).add((MalInteger) a.nth(1));
		}
	};
	static MalFunction subtract = new MalFunction() {
		public MalVal apply(final MalList a) throws MalThrowable {
			return ((MalInteger) a.nth(0)).subtract((MalInteger) a.nth(1));
		}
	};
	static MalFunction multiply = new MalFunction() {
		public MalVal apply(final MalList a) throws MalThrowable {
			return ((MalInteger) a.nth(0)).multiply((MalInteger) a.nth(1));
		}
	};
	static MalFunction divide   = new MalFunction() {
		public MalVal apply(final MalList a) throws MalThrowable {
			return ((MalInteger) a.nth(0)).divide((MalInteger) a.nth(1));
		}
	};

	// read
	public static MalVal READ(final String str) throws MalThrowable {
		return reader.read_str(str);
	}

	// eval
	public static MalVal eval_ast(final MalVal ast, final HashMap env) throws MalThrowable {
		if (ast instanceof MalSymbol) {
			final MalSymbol sym = (MalSymbol) ast;
			return (MalVal) env.get(sym.getName());
		} else if (ast instanceof MalList) {
			final MalList old_lst = (MalList) ast;
			final MalList new_lst = ast.list_Q() ? new MalList()
			  : new MalVector();
			for (final MalVal mv : (List<MalVal>) old_lst.value) {
				new_lst.conj_BANG(EVAL(mv, env));
			}
			return new_lst;
		} else if (ast instanceof MalHashMap) {
			final MalHashMap new_hm = new MalHashMap();
			final Iterator   it     = ((MalHashMap) ast).value.entrySet().iterator();
			while (it.hasNext()) {
				final Map.Entry entry = (Map.Entry) it.next();
				new_hm.value.put(entry.getKey(), EVAL((MalVal) entry.getValue(), env));
			}
			return new_hm;
		} else {
			return ast;
		}
	}

	public static MalVal EVAL(final MalVal orig_ast, final HashMap env) throws MalThrowable {
		final MalVal a0;
		//System.out.println("EVAL: " + printer._pr_str(orig_ast, true));
		if (!orig_ast.list_Q()) {
			return eval_ast(orig_ast, env);
		}

		// apply list
		final MalList ast = (MalList) orig_ast;
		if (ast.size() == 0) {
			return ast;
		}
		a0 = ast.nth(0);
		if (!(a0 instanceof MalSymbol)) {
			throw new MalError("attempt to apply on non-symbol '"
			  + printer._pr_str(a0, true) + "'");
		}
		final MalVal    args = eval_ast(ast.rest(), env);
		final MalSymbol fsym = (MalSymbol) a0;
		final ILambda   f    = (ILambda) env.get(fsym.getName());
		if (f == null) {
			throw new MalError("'" + fsym.getName() + "' not found");
		}
		return f.apply((MalList) args);
	}

	// print
	public static String PRINT(final MalVal exp) {
		return printer._pr_str(exp, true);
	}

	// repl
	public static MalVal RE(final HashMap env, final String str) throws MalThrowable {
		return EVAL(READ(str), env);
	}

	public static void main(final String[] args) throws MalThrowable {
		final String prompt = "user> ";

		final HashMap repl_env = new HashMap();
		repl_env.put("+", add);
		repl_env.put("-", subtract);
		repl_env.put("*", multiply);
		repl_env.put("/", divide);

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
				System.out.println(PRINT(RE(repl_env, line)));
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
}
