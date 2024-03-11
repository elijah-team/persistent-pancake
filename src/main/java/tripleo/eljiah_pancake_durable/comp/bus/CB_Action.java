package tripleo.eljiah_pancake_durable.comp.bus;

public interface CB_Action {
	String name();

	void execute();

	CB_OutputString[] outputStrings();

}
