package tripleo.elijah.stages.deduce.percy;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.ResolveError;

import java.util.function.Consumer;

public interface PFInvocation {
	void setter(Consumer<Setter> cs);

	ClassStatement getClassStatement();

	public interface Setter {
		void classInvocationAndRegister(ClassInvocation aClsinv);

		void resolveError(ResolveError aResolveError);

		void classInvocation(ClassInvocation aClsinv);

		ClassInvocation getClassInvocation();

		void hookClassInvocation(DoneCallback<ClassInvocation> cb);

		void exception(Exception aFailure);
	}
}
