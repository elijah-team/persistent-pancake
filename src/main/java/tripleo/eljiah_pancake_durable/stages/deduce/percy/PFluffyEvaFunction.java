package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;

public interface PFluffyEvaFunction extends PFluffyItem {
	PFInvocation addInvocation(ClassStatement aClassStatement, ClassInvocation aClsinv, ResolveError aRe);

	PFInvocation makeInvocation(ClassStatement aClassStatement);
}
