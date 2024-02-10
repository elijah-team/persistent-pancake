package tripleo.elijah_pancake.feb24.lang;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.TypeName;

public record EM_ClassConnectedTypename(EM_Typename em,
                                        ClassStatement classStatement) implements EM_Typename {
	@Override
	public TypeName asTypeNameElement() {
		return em.asTypeNameElement();
	}
}
