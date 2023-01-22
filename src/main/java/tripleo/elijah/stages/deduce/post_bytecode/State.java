package tripleo.elijah.stages.deduce.post_bytecode;

public interface State {
	void apply(Stateful element);

	void setIdentity(int aId);

	boolean checkState(Stateful aElement3);
}
