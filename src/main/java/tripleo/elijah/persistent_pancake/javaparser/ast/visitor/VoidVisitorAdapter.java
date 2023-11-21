package tripleo.elijah.persistent_pancake.javaparser.ast.visitor;

import tripleo.elijah.persistent_pancake.javaparser.ParseResult;
import tripleo.elijah.persistent_pancake.javaparser.ast.CompilationUnit;
import tripleo.elijah.persistent_pancake.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Optional;

public abstract class VoidVisitorAdapter<A> {
	public abstract void visit(ClassOrInterfaceDeclaration n, A arg);

//	@Override
	public void visit(final CompilationUnit n, final A arg) {
//		n.getImports().forEach(p -> p.accept(this, arg));
//		n.getModule().ifPresent(l -> l.accept(this, arg));
//		n.getPackageDeclaration().ifPresent(l -> l.accept(this, arg));
		n.getTypes().forEach(p -> p.accept(this, arg));
//		n.getComment().ifPresent(l -> l.accept(this, arg));
	}

	public void visit(final ParseResult<CompilationUnit> aParse, final Object aO) {
		final Optional<CompilationUnit> ocu = aParse.getResult();
		if (ocu.isPresent()) {
			visit(ocu.get(), (A) aO);
		}
	}
}
