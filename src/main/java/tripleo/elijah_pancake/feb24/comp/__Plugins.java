package tripleo.elijah_pancake.feb24.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.DeducePipeline;
import tripleo.eljiah_pancake_durable.comp.GeneratePipeline;
import tripleo.eljiah_pancake_durable.comp.WriteMesonPipeline;
import tripleo.eljiah_pancake_durable.comp.WritePipeline;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.PipelineMember;
import tripleo.elijah_durable_pancake.comp.internal.PipelinePlugin;

public enum __Plugins {;
	static class GeneratePipelinePlugin implements PipelinePlugin {

		@Override
		public String name() {
			return "GeneratePipeline";
		}

		@Override
		public PipelineMember instance(final @NotNull AccessBus ab0) {
			return new GeneratePipeline(ab0);
		}
	}

	static class DeducePipelinePlugin implements PipelinePlugin {

		@Override
		public String name() {
			return "DeducePipeline";
		}

		@Override
		public PipelineMember instance(final @NotNull AccessBus ab0) {
			return new DeducePipeline(ab0);
		}
	}

	static class WritePipelinePlugin implements PipelinePlugin {
		@Override
		public String name() {
			return "WritePipeline";
		}

		@Override
		public PipelineMember instance(final @NotNull AccessBus ab0) {
			return new WritePipeline(ab0);
		}
	}

	static class WriteMesonPipelinePlugin implements PipelinePlugin {
		@Override
		public String name() {
			return "WriteMesonPipeline";
		}

		@Override
		public PipelineMember instance(final @NotNull AccessBus ab0) {
			return new WriteMesonPipeline(ab0);
		}
	}
}
