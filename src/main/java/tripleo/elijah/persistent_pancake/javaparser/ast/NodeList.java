package tripleo.elijah.persistent_pancake.javaparser.ast;

import tripleo.elijah.UnintendedUseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NodeList<N> {
//	@InternalProperty
	private final List<N> innerList = new ArrayList<>(0);

	public void forEach(final Consumer<? super N> action) {
		innerList.forEach(action);
//		throw new UnintendedUseException();
	}
}
