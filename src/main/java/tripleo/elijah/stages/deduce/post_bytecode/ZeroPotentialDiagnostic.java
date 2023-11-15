package tripleo.elijah.stages.deduce.post_bytecode;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.diagnostic.Diagnostic;
import tripleo.elijah.diagnostic.Locatable;
import tripleo.elijah.util.NotImplementedException;

import java.io.PrintStream;
import java.util.List;

public class ZeroPotentialDiagnostic implements Diagnostic {
	@Override
	public String code() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public Severity severity() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public @NotNull Locatable primary() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public @NotNull List<Locatable> secondary() {
		NotImplementedException.raise();
		return null;
	}

	@Override
	public void report(final PrintStream stream) {
		NotImplementedException.raise();
		int y = 2;
	}
}
