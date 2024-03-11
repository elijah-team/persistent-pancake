package tripleo.eljiah_pancake_durable.comp.nextgen.pw;

public interface PW_PushWork {
	void handle(PW_Controller pwc, PW_PushWork otherInstance);

	void execute(PW_Controller aController);
}
