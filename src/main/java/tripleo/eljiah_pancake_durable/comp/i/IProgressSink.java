package tripleo.eljiah_pancake_durable.comp.i;

public interface IProgressSink {
	void note(int aCode, ProgressSinkComponent aCci, int aType, Object[] aParams);
}
