package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.ResolveError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PFluffyEvaFunctionImpl implements PFluffyEvaFunction {
	private List<PFInvocation> invocations = new ArrayList<>();

	@Override
	public PFInvocation addInvocation(final ClassStatement aClassStatement, final ClassInvocation aClsinv, final ResolveError aRe) {
		final PFInvocationImpl invocation = new PFInvocationImpl(aClassStatement, aClsinv, aRe);
		invocations.add(invocation);
		return invocation;
	}

	@Override
	public PFInvocation makeInvocation(final ClassStatement aClassStatement) {
		final Optional<PFInvocation> o = invocations.stream()
//		  .filter(LookupResultList.distinctByKey())
                                                    .filter(i -> i.getClassStatement() == aClassStatement)
                                                    .findAny();

		if (o.isPresent()) {
			return o.get();
		}

		final PFInvocationImpl invocation = new PFInvocationImpl(aClassStatement, null, null);
		invocations.add(invocation);
		return invocation;
	}
}
