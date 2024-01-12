package tripleo.elijah.nextgen.inputtree;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.*;
import tripleo.elijah.util.*;
import tripleo.elijah.util.Operation;

public class EIT_InputTree {
	// TODO 09/20 where is this used?
	public record _Node(Operation<?> operation) {
	}

	public void addNode(CompilerInput i) {
		int y = 2;
	}

	public void setNodeOperation(final CompilerInput input, final Operation<?> operation) {
		@Nullable
		Object o = input.getExt(EIT_InputTree.class);
		if (o == null) {
			input.putExt(EIT_InputTree.class, new _Node(operation));
		} else {
			input.putExt(EIT_InputTree.class, new _Node(operation));
		}
	}
}
