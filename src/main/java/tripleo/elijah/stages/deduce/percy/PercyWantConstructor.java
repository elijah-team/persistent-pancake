package tripleo.elijah.stages.deduce.percy;

import tripleo.elijah.Eventual;
import tripleo.elijah.lang.ConstructorDef;

import java.util.function.Consumer;

public interface PercyWantConstructor {
	void onFinalSuccess(Consumer<Void> cb);
	void onFailure(Consumer<Void> cb);

	Eventual<ConstructorDef> getEventualConstructorDef();
}
