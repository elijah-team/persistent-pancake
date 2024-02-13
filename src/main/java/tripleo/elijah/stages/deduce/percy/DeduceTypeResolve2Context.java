package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.stages.gen_fn.GenType;

import java.util.Optional;

public interface DeduceTypeResolve2Context {
	<T> void set(String aKey, T aValue);
	<T> Optional<T> get(String aKey);

	GenType createGenType();

	LookupResultList getLrl();
}
