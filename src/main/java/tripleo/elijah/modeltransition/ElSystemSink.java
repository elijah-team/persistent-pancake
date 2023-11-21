package tripleo.elijah.modeltransition;

import tripleo.elijah.stages.generate.ElSystem;

public interface ElSystemSink {
	void provide(ElSystem aSystem);
}
