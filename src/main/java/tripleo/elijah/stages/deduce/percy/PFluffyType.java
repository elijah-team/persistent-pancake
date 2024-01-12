package tripleo.elijah.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.OS_Type;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.ResolveError;
import tripleo.elijah.stages.gen_fn.GenType;

public class PFluffyType implements PFluffyItem {
	private DeduceTypes2 deduceTypes2;
	private OS_Type      osType;
	private Context      context;
	private GenType      genType;

	public void provide(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
	}

	public GenType resolve_type(final OS_Type aOSType, final Context aContext) throws ResolveError {
		// TODO 11/18 promises on exception/return !!

		provide(aOSType);
		provide(aContext);

		if (deduceTypes2 == null) throw new AssertionError();

		@NotNull final GenType genType = deduceTypes2.resolve_type(osType, context);
		provide(genType);
		return genType;
	}

	private void provide(final OS_Type aOSType) {
		osType = aOSType;
	}

	private void provide(final Context aContext) {
		context = aContext;
	}

	private void provide(final GenType aGenType) {
		genType = aGenType;
	}
}
