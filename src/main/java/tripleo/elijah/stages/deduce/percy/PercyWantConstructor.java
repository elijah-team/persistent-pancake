package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.Eventual;
import tripleo.elijah.lang.ConstructorDef;
import tripleo.elijah.stages.deduce.ClassInvocation;
import tripleo.elijah.stages.gen_fn.GenType;
import tripleo.elijah.stages.gen_fn.GeneratedConstructor;

import java.util.function.Consumer;

public interface PercyWantConstructor {
	void onFinalSuccess(Consumer<Void> cb);
	void onFailure(Consumer<Void> cb);

	Eventual<ConstructorDef> getEventualConstructorDef();

	void provide(ClassInvocation aClassInvocation);

	void provide(ConstructorDef aEl2);

	void provide(GeneratedConstructor aGeneratedConstructor);

	void genType(GenType aResolved2);
}
