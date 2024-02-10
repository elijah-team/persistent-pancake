package tripleo.elijah.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tripleo.elijah.lang.NormalTypeName;

import tripleo.elijah.stages.deduce.DeduceTypes2;

public interface PFluffyClassStatement {
	void resolveClassToXXX(@Nullable String constructorName,
	                       @NotNull NormalTypeName aTyn1,
	                       PFInvocation.Setter s,
	                       DeduceTypes2 aDeduceTypes2);
}
