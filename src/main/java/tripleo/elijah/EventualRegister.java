package tripleo.elijah;

public interface EventualRegister {
	void checkFinishEventuals(); // TODO signature

	<P> void register(Eventual<P> e);
}
