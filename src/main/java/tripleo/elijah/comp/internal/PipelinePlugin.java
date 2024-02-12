package tripleo.elijah.comp.internal;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.AccessBus;
import tripleo.elijah.comp.PipelineMember;

public interface PipelinePlugin {
	String name();

	PipelineMember instance(final @NotNull AccessBus ab0);
}
