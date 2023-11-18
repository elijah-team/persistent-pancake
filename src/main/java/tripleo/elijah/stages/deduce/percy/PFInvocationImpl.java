package tripleo.elijah.stages.deduce.percy;

import org.apache.commons.lang3.tuple.Triple;
import org.jdeferred2.DoneCallback;
import tripleo.elijah.Eventual;
import tripleo.elijah.UnintendedUseException;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.ResolveError;

import java.util.function.Consumer;

public class PFInvocationImpl implements PFInvocation {
	private final ClassStatement classStatement;
	private final ClassInvocation clsinv;
	private final ResolveError re;

	public PFInvocationImpl(final ClassStatement aClassStatement, final ClassInvocation aClsinv, final ResolveError aRe) {
		classStatement = aClassStatement;
		clsinv         = aClsinv;
		re             = aRe;
	}

	@Override
	public void setter(final Consumer<Setter> cs) {
		cs.accept(new Setter() {
			private Eventual<ClassInvocation> _p_CI = new Eventual<>(); // sigh
			private ResolveError re;
			private ClassInvocation ci;

			@Override
			public void classInvocationAndRegister(final ClassInvocation aClsinv) {
				throw new UnintendedUseException();
			}

			@Override
			public void resolveError(final ResolveError aResolveError) {
				this.re = aResolveError;
			}

			@Override
			public void classInvocation(final ClassInvocation aClsinv) {
				ci = aClsinv;
				_p_CI.resolve(aClsinv);
			}

			@Override
			public ClassInvocation getClassInvocation() {
//				throw new UnintendedUseException();
				return ci;
			}

			@Override
			public void hookClassInvocation(final DoneCallback<ClassInvocation> cb) {
				_p_CI.then(cb);
			}
		});
	}

	@Override
	public ClassStatement getClassStatement() {
		return classStatement;
	}
}
