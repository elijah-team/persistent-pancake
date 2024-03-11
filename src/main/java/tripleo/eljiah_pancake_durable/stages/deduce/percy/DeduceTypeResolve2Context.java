package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;

import java.util.Optional;

public interface DeduceTypeResolve2Context {
	<T> void set(String aKey, T aValue);
	<T> Optional<T> get(String aKey);

	GenType createGenType();

	LookupResultList getLrl();
}
