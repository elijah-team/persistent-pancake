package tripleo.elijah_prepan.compilation_runner;

public interface CK_Monitor {
	int PLACEHOLDER_CODE   = 9999;
	int PLACEHOLDER_CODE_2 = 9998;

	void reportFailure(int code, String message);
}
