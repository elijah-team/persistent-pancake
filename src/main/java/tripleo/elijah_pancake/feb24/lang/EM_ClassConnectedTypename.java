package tripleo.elijah_pancake.feb24.lang;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.TypeName;

public record EM_ClassConnectedTypename(EM_Typename em,
                                        ClassStatement classStatement) implements EM_Typename {
	@Override
	public TypeName asTypeNameElement() {
		return em.asTypeNameElement();
	}
}
