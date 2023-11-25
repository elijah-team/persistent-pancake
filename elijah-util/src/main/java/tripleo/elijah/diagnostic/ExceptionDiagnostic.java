package tripleo.elijah.diagnostic;

import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class ExceptionDiagnostic implements Diagnostic {
	private final Throwable e;

	public ExceptionDiagnostic(final Throwable aE) {
		e = aE;
	}

	@Override
	public @NotNull String code() {
		return "9003";
	}

	@Override
	public Object get() {
		return e;
	}

	@Override
	public @NotNull Locatable primary() {
		return null;
	}

	@Override
	public void report(final @NotNull PrintStream stream) {
		stream.println(code() + " Some exception " + e);
	}

	@Override
	public @NotNull List<Locatable> secondary() {
		return null;
	}

	@Override
	public @NotNull Severity severity() {
		return Severity.ERROR;
	}
}
