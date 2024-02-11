package tripleo.elijah_pancake.crib.javaparser.ast.visitor;

import tripleo.elijah_pancake.crib.javaparser.ast.CompilationUnit;
import tripleo.elijah_pancake.crib.javaparser.ParseResult;
import tripleo.elijah_pancake.crib.javaparser.ast.___Node;
import tripleo.elijah_pancake.crib.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Optional;

public abstract class VoidVisitorAdapter<A> {
	public abstract void visit(ClassOrInterfaceDeclaration n, A arg);

//	@Override
	public void visit(final CompilationUnit n, final A arg) {
//		n.getImports().forEach(p -> p.accept(this, arg));
//		n.getModule().ifPresent(l -> l.accept(this, arg));
//		n.getPackageDeclaration().ifPresent(l -> l.accept(this, arg));
		n.getTypes().forEach((___Node p) -> p.accept(this, arg));
//		n.getComment().ifPresent(l -> l.accept(this, arg));
	}

	public void visit(final ParseResult<CompilationUnit> aParse, final Object aO) {
		final Optional<CompilationUnit> ocu = aParse.getResult();
		ocu.ifPresent(aCompilationUnit -> visit(aCompilationUnit, (A) aO));
	}
}
