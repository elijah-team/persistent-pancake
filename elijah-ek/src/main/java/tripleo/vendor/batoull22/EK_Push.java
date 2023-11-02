package tripleo.vendor.batoull22;

import java.util.Objects;

/**
 * @author tripleo
 */
final class EK_Push {
	private final EK_Fact predicating;
	private final EK_Fact resultant;

	EK_Push(EK_Fact predicating, EK_Fact resultant) {
		this.predicating = predicating;
		this.resultant   = resultant;
	}

	public EK_Fact predicating() {
		return predicating;
	}

	public EK_Fact resultant() {
		return resultant;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		EK_Push that = (EK_Push) obj;
		return Objects.equals(this.predicating, that.predicating) &&
				Objects.equals(this.resultant, that.resultant);
	}

	@Override
	public int hashCode() {
		return Objects.hash(predicating, resultant);
	}

	@Override
	public String toString() {
		return "EK_Push[" +
				"predicating=" + predicating + ", " +
				"resultant=" + resultant + ']';
	}

}
