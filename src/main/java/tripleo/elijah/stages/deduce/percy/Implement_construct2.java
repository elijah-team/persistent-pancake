package tripleo.elijah.stages.deduce.percy;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.util.Eventual;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.modeltransition.EventualProvider;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.deduce.DeduceTypes2;
import tripleo.elijah.stages.deduce.Implement_construct;
import tripleo.elijah.stages.gen_fn.BaseGeneratedFunction;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;
import tripleo.elijah.stages.instructions.Instruction;
import tripleo.elijah.util.NotImplementedException;

import java.util.function.Consumer;

public class Implement_construct2 {
	private final Provided<PercyWantConstructor> p = new EventualProvider<>();
	private final DeduceTypes2                   deduceTypes2;
	private final BaseGeneratedFunction          generatedFunction;
	private final Instruction                    instruction;

	public Implement_construct2(final DeduceTypes2 aDeduceTypes2, final BaseGeneratedFunction aGeneratedFunction, final Instruction aInstruction) {
		deduceTypes2      = aDeduceTypes2;
		generatedFunction = aGeneratedFunction;
		instruction       = aInstruction;
	}

	public void action() {
		final @NotNull Implement_construct ic = deduceTypes2.newImplement_construct(generatedFunction, instruction);

		PercyWantConstructor pwc1 = new _IC2_PercyWantConstructor();

		p.provide(pwc1);

//		pwc1.

		ic.action(p);
	}

	private static class _IC2_PercyWantConstructor implements PercyWantConstructor {
		private final Eventual<DeduceTypeResolve2>   _p_resolver;
		private final Eventual<ConstructorDef>       _p_cd;
		private final Eventual<ClassInvocation>      _p_ci;
		private final Eventual<GeneratedConstructor> _p_ec;
		private       GenType                        genType;
		private       Eventual<PercyWantConstructor> pwcp = new Eventual<>();

		public _IC2_PercyWantConstructor() {
			_p_resolver = new Eventual<>();
			_p_cd       = new Eventual<>();
			_p_ci       = new Eventual<>();
			_p_ec       = new Eventual<>();
		}

		@Override
		public void onFinalSuccess(final Consumer<Void> cb) {
			throw new NotImplementedException();
		}

		@Override
		public void onFailure(final Consumer<Void> cb) {
			throw new NotImplementedException();
		}

		@Override
		public Eventual<ConstructorDef> getEventualConstructorDef() {
			return _p_cd;
		}

		@Override
		public void provide(final ClassInvocation aClassInvocation) {
			final Eventual<ClassInvocation> g = getEventualClassInvocation();
			if (g != null) {
				g.resolve(aClassInvocation);
			}
		}

		@Override
		public void provide(final ConstructorDef aConstructorDef) {
			final Eventual<ConstructorDef> g = getEventualConstructorDef();
			if (g != null) {
				g.resolve(aConstructorDef);
			}
		}

		@Override
		public void provide(final GeneratedConstructor aGeneratedConstructor) {
			final Eventual<GeneratedConstructor> g = getEventualGeneratedConstructor();
			if (g != null) {
				g.resolve(aGeneratedConstructor);
			}
		}

		@Override
		public void setEnclosingGenType(final GenType aResolved2) {
			this.genType = aResolved2;
			pwcp.resolve(this);

			_p_resolver.then(resolver -> pwcp.then(pwc1 -> {
				final ClassInvocation ci = resolver.getClassInvocation(genType, pwc1);
			}));
		}

		private Eventual<GeneratedConstructor> getEventualGeneratedConstructor() {
			return _p_ec;
		}

		private Eventual<ClassInvocation> getEventualClassInvocation() {
			return _p_ci;
		}

		@Override
		public void provide(final DeduceTypeResolve2 aResolver) {
			_p_resolver.resolve(aResolver);
		}

		@Override
		public GenType getEnclosingGenType() {
			return genType;
		}

		@Override
		public void onResolver(final DoneCallback<DeduceTypeResolve2> cb) {
			_p_resolver.then(cb);
		}
	}
}
