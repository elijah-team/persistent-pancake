package tripleo.elijah_pancake.crib;

import tripleo.elijah.comp.USE;
import tripleo.elijah_pancake.cribbonacci.JavaParser;
import tripleo.elijah_pancake.cribbonacci.ast.visitor.VoidVisitorAdapter;
import tripleo.elijah_pancake.crib.me.tomassetti.support.DirExplorer;
import tripleo.elijah_pancake.cribbonacci.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

public class ListClassesExample {
	private static boolean flag;
	private static       boolean            quiet = false;
	static final List<List<String>> ts    = new ArrayList<>();

	public static void listClasses(final File projectDir) {
		new DirExplorer(
		  (level, path, file) -> {
			  if (path.startsWith("/AA/")) {return false;}
			  if (path.startsWith("/java_demos/")) {return false;}
			  if (USE.accept_source_files.accept(null, path)) {
				  return true;
			  }
			  return false;
		  },
		  (level, path, file) -> {
			_l(path);
			_l(Strings.repeat("=", path.length()));
			try {
				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
//						super.visit(n, arg);

						assert n != null;

						final String nn = n.getName().asString();
						final String pn  = n.getPackageName().asString();
						_l(String.format("** class `%s' in `%s'%n", nn, pn));

						_ll(List_of(path, nn, pn));

						flag = true;
					}
				}.visit(JavaParser.parse(file), null);
				_l(""); // empty line
//			} catch (final ParseException e) {
//				throw new RuntimeException(e);
//				System.err.println("** IGNORING "+path);
			} catch (final IOException e) {
//				throw new RuntimeException(e);
				System.err.println("** IGNORING "+path);
			}
		  }).explore(projectDir);
	}

	private static void _ll(final List<String> t) {
		ts.add(t);
	}

	private static void _l(final String string) {
		if (!quiet) {
			System.out.println(string);
		}
	}

	public static void setQuiet() {
		quiet = true;
	}

	public boolean isFlag() {
		return flag;
	}
}
