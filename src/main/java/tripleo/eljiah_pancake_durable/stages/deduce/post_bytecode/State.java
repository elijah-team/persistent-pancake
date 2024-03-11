package tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode;

public interface State {
	void apply(DefaultStateful element);

	void setIdentity(int aId);

	boolean checkState(DefaultStateful aElement3);
}
