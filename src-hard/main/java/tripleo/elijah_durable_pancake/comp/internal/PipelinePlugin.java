package tripleo.elijah_durable_pancake.comp.internal;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.PipelineMember;

public interface PipelinePlugin {
	String name();

	PipelineMember instance(final @NotNull AccessBus ab0);
}
