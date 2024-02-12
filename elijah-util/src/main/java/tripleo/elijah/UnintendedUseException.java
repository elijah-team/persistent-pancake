package tripleo.elijah;

public class UnintendedUseException extends RuntimeException {
	public UnintendedUseException(final String s) {
		super(s);
	}
	public UnintendedUseException() {
		super();
	}
}
