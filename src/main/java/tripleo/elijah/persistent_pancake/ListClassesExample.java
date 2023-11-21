package tripleo.elijah.persistent_pancake;

import tripleo.elijah.comp.USE;
import tripleo.elijah.persistent_pancake.javaparser.JavaParser;
import tripleo.elijah.persistent_pancake.javaparser.ParseException;
import tripleo.elijah.persistent_pancake.javaparser.ast.body.ClassOrInterfaceDeclaration;
import tripleo.elijah.persistent_pancake.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import tripleo.elijah.persistent_pancake.me.tomassetti.support.DirExplorer;

import java.io.File;
import java.io.IOException;

public class ListClassesExample {
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
			System.out.println(path);
			System.out.println(Strings.repeat("=", path.length()));
			try {
				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
//						super.visit(n, arg);

						assert n != null;

						System.out.println(" * " + n.getName());
					}
				}.visit(JavaParser.parse(file), null);
				System.out.println(); // empty line
//			} catch (final ParseException e) {
//				throw new RuntimeException(e);
//				System.err.println("** IGNORING "+path);
			} catch (final IOException e) {
//				throw new RuntimeException(e);
				System.err.println("** IGNORING "+path);
			}
		  }).explore(projectDir);
	}
}
