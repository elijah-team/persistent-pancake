package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.ResolveError;

public interface PFluffyEvaFunction extends PFluffyItem {
	PFInvocation addInvocation(ClassStatement aClassStatement, ClassInvocation aClsinv, ResolveError aRe);

	PFInvocation makeInvocation(ClassStatement aClassStatement);
}
