package tripleo.elijah.persistent_pancake.javaparser.ast.visitor;

import tripleo.elijah.persistent_pancake.javaparser.ast.body.ClassOrInterfaceDeclaration;

public abstract class VoidVisitorAdapter<T> {
	public abstract void visit(ClassOrInterfaceDeclaration n, Object arg);
}
