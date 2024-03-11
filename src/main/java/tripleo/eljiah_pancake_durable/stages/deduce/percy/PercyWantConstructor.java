package tripleo.eljiah_pancake_durable.stages.deduce.percy;

import org.jdeferred2.DoneCallback;
import tripleo.elijah.Eventual;
import tripleo.eljiah_pancake_durable.lang.ConstructorDef;
import tripleo.eljiah_pancake_durable.stages.deduce.ClassInvocation;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenType;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedConstructor;

import java.util.function.Consumer;

public interface PercyWantConstructor {
	void onFinalSuccess(Consumer<Void> cb);
	void onFailure(Consumer<Void> cb);

	Eventual<ConstructorDef> getEventualConstructorDef();

	void provide(ClassInvocation aClassInvocation);

	void provide(ConstructorDef aConstructorDef);

	void provide(GeneratedConstructor aGeneratedConstructor);

	void setEnclosingGenType(GenType aResolved2);

	void provide(DeduceTypeResolve2 aResolver);

	GenType getEnclosingGenType();

	void onResolver(DoneCallback<DeduceTypeResolve2> cb);
}
