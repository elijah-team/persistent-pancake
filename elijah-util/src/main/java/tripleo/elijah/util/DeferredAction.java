package tripleo.elijah.util;

import tripleo.elijah.*;

public interface DeferredAction<T> {
	String description();

	boolean completed();

	Eventual<T> promise();

	void calculate();
}
