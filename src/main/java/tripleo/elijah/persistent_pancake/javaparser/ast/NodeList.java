package tripleo.elijah.persistent_pancake.javaparser.ast;

import tripleo.elijah.persistent_pancake.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NodeList<N /*extends ___Node*/> {
//	@InternalProperty
	private final List</*N*/___Node> innerList = new ArrayList<>(0);

	public void forEach(final Consumer</*? super N*/___Node> action) {
		innerList.forEach(action);
//		throw new UnintendedUseException();
	}

	public void ___add(final ClassOrInterfaceDeclaration aDeclaration) {
		innerList.add(aDeclaration);
	}
}
