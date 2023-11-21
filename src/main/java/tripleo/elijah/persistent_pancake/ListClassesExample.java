package tripleo.elijah.persistent_pancake;

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
		new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
			System.out.println(path);
			System.out.println(Strings.repeat("=", path.length()));
			try {
				new VoidVisitorAdapter<Object>() {
					@Override
					public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
//						super.visit(n, arg);
						System.out.println(" * " + n.getName());
					}
				}.visit(JavaParser.parse(file), null);
				System.out.println(); // empty line
			} catch (final ParseException | IOException e) {
				throw new RuntimeException(e);
			}
		}).explore(projectDir);
	}

	public static void main(final String[] args) {
		final File projectDir = new File("source_to_parse/junit-master");
		listClasses(projectDir);
	}
}
