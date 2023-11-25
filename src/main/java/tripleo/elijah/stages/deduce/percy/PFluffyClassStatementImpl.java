package tripleo.elijah.stages.deduce.percy;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;

import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.NormalTypeName;
import tripleo.elijah.lang.TypeName;
import tripleo.elijah.lang.TypeNameList;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

public class PFluffyClassStatementImpl implements PFluffyClassStatement {
	private final ClassStatement classStatement;

	public PFluffyClassStatementImpl(final ClassStatement aClassStatement) {
		classStatement = aClassStatement;
	}

	@Override
	public void extracted(@Nullable final String constructorName,
	                      @NotNull final NormalTypeName aTyn1,
	                      final PFInvocation.Setter s,
	                      final DeduceTypes2 aDeduceTypes2) {
		@NotNull final List<TypeName> gp     = classStatement.getGenericPart();
		ClassInvocation               clsinv = new ClassInvocation(classStatement, constructorName);

		if (gp.isEmpty()) {
			return;
		}

		final TypeNameList gp2 = aTyn1.getGenericPart();
		if (gp2 == null) {
			int y=2;
//			assert false;
			return;
		}

		final DeduceTypeResolve2 r = aDeduceTypes2.resolver();

		for (int i = 0; i < gp.size(); i++) {
			final TypeName                    typeName = gp2.get(i);
			final @NotNull Operation<GenType> top      = r.of(typeName);
			final @NotNull GenType            typeName2;

			if (top.mode() == Mode.SUCCESS) {
				typeName2 = top.success();

				clsinv.set(i, gp.get(i), typeName2.getResolved());

				r.registerClassInvocation(clsinv)
				             .andThen(clsinv2 -> {
							// NOTE 11/18 why doesn't idea do this for me (i bet it would with Kotlin...)
					             // -- before andThen
					             //  but it has intention below...
					             //noinspection DataFlowIssue
					             Preconditions.checkNotNull(clsinv2);

					             typeName2.setCi(clsinv2);

					             s.classInvocation(clsinv2);
				             });
			} else {
				final Throwable failure = top.failure();
				failure.printStackTrace();
				s.exception(failure);
			}
		}
	}
}
