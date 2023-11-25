package tripleo.elijah.util;

public interface DeferredAction<T> {
	String description();

	boolean completed();

	Eventual<T> promise();

	void calculate();
}
