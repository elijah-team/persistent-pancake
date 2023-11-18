package tripleo.elijah.stages.deduce.percy;

import org.apache.commons.lang3.tuple.Triple;
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
		throw new UnintendedUseException();
	}
}
