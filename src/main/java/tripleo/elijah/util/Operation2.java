package tripleo.elijah.util;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.diagnostic.*;
import tripleo.elijah.diagnostic.*;

/**
 * An emulation of Rust's Result type
 *
 * @param <T> the success type
 */
public class Operation2<T> {
	public static <T> Operation2<T> convert(final @NotNull Operation<T> op) {
		final Operation2<T> op2;

		switch (op.mode()) {
		case FAILURE -> {
			op2 = Operation2.failure(new ExceptionDiagnostic(op.failure()));
		}
		case NOTHING -> {
			throw new NotImplementedException(); // UnintendedUseException ??
		}
		case SUCCESS -> {
			op2 = Operation2.success(op.success());
		}
		default -> throw new IllegalStateException("Unexpected value: " + op.mode());
		}

		return op2;
	}

	public static <T> @NotNull Operation2<T> failure(final Diagnostic aException) {
		final Operation2<T> op = new Operation2<>(null, aException, Mode.FAILURE);
		return op;
	}

	public static <T> @NotNull Operation2<T> success(final T aSuccess) {
		final Operation2<T> op = new Operation2<>(aSuccess, null, Mode.SUCCESS);
		return op;
	}

	private final Mode mode;

	private final T succ;

	private final Diagnostic exc;

	public Operation2(final T aSuccess, final Diagnostic aException, final Mode aMode) {
		succ = aSuccess;
		exc = aException;
		mode = aMode;

		if (succ == exc)
			throw new AssertionError();
	}

	public Diagnostic failure() {
		return exc;
	}

	public Mode mode() {
		return mode;
	}

	public T success() {
		return succ;
	}
}
