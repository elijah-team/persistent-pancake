package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.ResolveError;

import java.util.function.Consumer;

public interface PFInvocation {
	void setter(Consumer<Setter> cs);

	public interface Setter {
		void classInvocationAndRegister(ClassInvocation aClsinv);

		void resolveError(ResolveError aResolveError);

		void classInvocation(ClassInvocation aClsinv);

		ClassInvocation getClassInvocation();
	}
}
