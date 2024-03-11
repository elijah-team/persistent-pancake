package tripleo.elijah_pancake.cribbonacci.ast.body;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.elijah_pancake.cribbonacci.ast.___Node;
import tripleo.elijah_pancake.cribbonacci.ast.expr.SimpleName;
import tripleo.elijah_pancake.cribbonacci.ast.visitor.VoidVisitorAdapter;

public interface ClassOrInterfaceDeclaration extends ___Node {
	static ClassOrInterfaceDeclaration of(final ClassStatement aClassStatement) {
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
		public <A> void accept(final VoidVisitorAdapter visitor, final A arg) {
			visitor.visit(this, arg);
		}
	}
}
