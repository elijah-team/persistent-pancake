package tripleo.elijah_pancake.feb24.lang;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.TypeNameList;
import tripleo.elijah_pancake.sep1011.lang.ENU_Understanding;

public record EN_HasGenericPart(TypeNameList genericPart, ClassStatement classStatement) implements ENU_Understanding {
	@Override
	public boolean equals(final Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}
}
