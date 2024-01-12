package tripleo.vendor.batoull22;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author tripleo
 */
class EK_Factarray {

	private final String st;
	private final EK_Fact[] ch;

	public EK_Factarray(String ast, EK_Fact[] ach) {
		this.st = ast;
		this.ch = ach;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		EK_Factarray that = (EK_Factarray) obj;
		return Objects.equals(this.st, that.st) && Objects.equals(this.ch, that.ch);
	}

	@Override
	public @NotNull String toString() {
		return "EK_Factarray[" + "st=" + st + ", " + "ch=" + ch + ']';
	}

	public String st()    { return this.st; }
	public EK_Fact[] ch() { return this.ch; }

}
