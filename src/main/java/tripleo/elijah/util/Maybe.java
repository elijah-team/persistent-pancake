package tripleo.elijah.util;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.diagnostic.*;
import tripleo.elijah.diagnostic.*;

public class Maybe<T> {
	public static <T> Maybe<T> of(@NotNull T t) {
		return new Maybe<>(t, null);
	}

	public static <T> Maybe<T> of_exc(@NotNull Diagnostic d) {
		return new Maybe<>(null, d);
	}

	public static <T> Maybe<T> of_exc(@NotNull Exception e) {
		return new Maybe<>(null, new ExceptionDiagnostic(e));
	}

	public final @Nullable Diagnostic exc;

	public final @Nullable T o;

	public Maybe(final @Nullable T o, final Diagnostic exc) {
		if (o == null) {
			if (exc == null) {
				throw new IllegalStateException("Both o and exc are null!");
			}
		} else {
			if (exc != null) {
				throw new IllegalStateException("Both o and exc are null (2)!");
			}
		}

		this.o = o;
		this.exc = exc;
	}

	public boolean isException() {
		return exc != null;
	}
}
