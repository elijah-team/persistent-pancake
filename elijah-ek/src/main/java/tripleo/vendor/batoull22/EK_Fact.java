package tripleo.vendor.batoull22;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author tripleo
 */
public class EK_Fact {
	private final char ch;

	public EK_Fact(char aCh) {
		ch = aCh;
	}

	public char ch() {
		return ch;
	}

	@Override
	public int hashCode() {
		return /*Objects.hash*/(ch); // ??
	}

	@Override
	public boolean equals(final Object aO) {
		if (this == aO) return true;
		if (aO == null || getClass() != aO.getClass()) return false;
		final EK_Fact ekFact = (EK_Fact) aO;
		return ch == ekFact.ch;
	}

	@Contract(pure = true)
	@Override
	public @NotNull String toString() {
		return "<FACT %c>".formatted(ch); // ??!! you don't actually know java, do you?
	}
}
