package tripleo.elijah_pancake.crib.javaparser.ast.body;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah_pancake.crib.javaparser.ast.___Node;
import tripleo.elijah_pancake.crib.javaparser.ast.expr.SimpleName;
import tripleo.elijah_pancake.crib.javaparser.ast.visitor.VoidVisitorAdapter;

public interface ClassOrInterfaceDeclaration extends ___Node {
	static ClassOrInterfaceDeclaration of(ClassStatement aClassStatement) {
		return new _ClassOrInterfaceDeclaration(aClassStatement);
	}

	SimpleName getName();

	SimpleName getPackageName();

	class _ClassOrInterfaceDeclaration implements ClassOrInterfaceDeclaration, ___Node {
		private final ClassStatement classStatement;

		public _ClassOrInterfaceDeclaration(final ClassStatement aClassStatement) {
			classStatement = aClassStatement;
		}

		@Override
		public SimpleName getName() {
			return new SimpleName(classStatement.getName());
		}

		@Override
		public SimpleName getPackageName() {
			return new SimpleName(classStatement.getPackageName().getName());
		}

		@Override
		public <A> void accept(final VoidVisitorAdapter aAVoidVisitorAdapter, final A aArg) {
			aAVoidVisitorAdapter.visit(this, aArg);
		}
	}
}
