package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.lang.Context;
import tripleo.eljiah_pancake_durable.lang.LookupResultList;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.TypeName;
import tripleo.eljiah_pancake_durable.lang.types.OS_UserType;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.deduce.DeduceTypes2;
import tripleo.eljiah_pancake_durable.stages.deduce.ResolveError;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.Operation;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DeduceTypeResolve2 {
	private final DeduceTypes2 deduceTypes2;

	public DeduceTypeResolve2(final DeduceTypes2 aDeduceTypes2) {
		deduceTypes2 = aDeduceTypes2;
	}

	public void makeGenType(final @NotNull PercyWantType aPercyWantType, final PercyMakeType aMt) {
		aPercyWantType.provide(aMt);
	}

	public ClassInvocation getClassInvocation(final GenType aGenType, final PercyWantConstructor aPercyWantConstructor) {
		NotImplementedException.raise_stop();
		return null;
	}

	public @NotNull Operation<GenType> of(TypeName typeName) {
		@NotNull
		GenType result;
		try {
			result = deduceTypes2.newPFluffyType().resolve_type(new OS_UserType(typeName), typeName.getContext());
		} catch (ResolveError e) {
//			e.printStackTrace();
			return Operation.failure(e);
		}
		return Operation.success(result);
	}

	public DeducePhase.RCI2 registerClassInvocation(ClassInvocation clsinv) {
		return deduceTypes2._phase().registerClassInvocation2(clsinv);
	}

	public void resolveIdentImmediately(final String aText, final Context aCtx, final BiConsumer<LookupResultList, DeduceTypeResolve2Context> cons) {
		resolveIdentImmediatelyReturningContext(aText, aCtx, cons);
	}

	public DeduceTypeResolve2Context resolveIdentImmediatelyReturningContext(final String aText, final Context aCtx, final BiConsumer<LookupResultList, DeduceTypeResolve2Context> cons) {
		//new GenType(resolver)
		final LookupResultList lrl = aCtx.lookup(aText);
		final var ctx = new DeduceTypeResolve2Context() {
			private final Map<String, Object> m = new HashMap<>();

			@Override
			public <T> void set(final String aKey, final T aValue) {
				assert !m.containsKey(aKey);
				m.put(aKey, aValue);
			}

			@Override
			public <T> Optional<T> get(final String aKey) {
				if (!m.containsKey(aKey))
					return Optional.empty();

				//noinspection unchecked
				return Optional.of((T) m.get(aKey));
			}

			@Override
			public GenType createGenType() {
				// TODO dt2._inj() ??
				return new GenType(DeduceTypeResolve2.this);
			}

			@Override
			public LookupResultList getLrl() {
				return lrl;
			}
		};
		cons.accept(lrl, ctx);
		return ctx;
	}

	public void resolveIdentImmediatelyBest(final String aText, final Context aCtx, final List<Predicate<OS_Element>> l, final BiConsumer<OS_Element, DeduceTypeResolve2Context> cons) {
		//new GenType(resolver)
		final LookupResultList lrl = aCtx.lookup(aText);
		final var ctx = new DeduceTypeResolve2Context() {
			private final Map<String, Object> m = new HashMap<>();

			@Override
			public <T> void set(final String aKey, final T aValue) {
				assert !m.containsKey(aKey);
				m.put(aKey, aValue);
			}

			@Override
			public <T> Optional<T> get(final String aKey) {
				if (!m.containsKey(aKey))
					return Optional.empty();

				//noinspection unchecked
				return Optional.of((T) m.get(aKey));
			}

			@Override
			public GenType createGenType() {
				// TODO dt2._inj() ??
				return new GenType(DeduceTypeResolve2.this);
			}

			@Override
			public LookupResultList getLrl() {
				return lrl;
			}
		};
		var best = lrl.chooseBest(l);
		cons.accept(best, ctx);
	}
}
