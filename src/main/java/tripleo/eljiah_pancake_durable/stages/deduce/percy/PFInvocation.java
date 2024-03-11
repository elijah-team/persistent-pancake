package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import org.jdeferred2.DoneCallback;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;

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

		void exception(Throwable aFailure);
	}
}
