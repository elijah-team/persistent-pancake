package tripleo.elijah_pancake.cribbonacci.ast;

import tripleo.elijah_pancake.cribbonacci.ast.visitor.VoidVisitorAdapter;

public interface TypeDeclaration<T> extends ___Node {
	<A> void accept(VoidVisitorAdapter aAVoidVisitorAdapter, A aArg);
}
