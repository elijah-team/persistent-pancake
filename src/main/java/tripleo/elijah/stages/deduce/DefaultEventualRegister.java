package tripleo.elijah.stages.deduce;

//import jogamp.opengl.openal.av.ALAudioSink;
import tripleo.elijah.Eventual;
import tripleo.elijah.EventualRegister;
//import tripleo.elijah.ci.CiExpressionList;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultEventualRegister implements EventualRegister {
	private List<Eventual<?>> registered = new ArrayList<>();

	@Override
	public void checkFinishEventuals() {
		for (Eventual<?> o : registered) {
			if (o.isPending()) System.err.println("isPending @" +o);
		}
	}

	@Override
	public <P> void register(final Eventual<P> e) {
		registered.add(e);
	}
}
