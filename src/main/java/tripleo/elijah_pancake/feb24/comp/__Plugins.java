package tripleo.elijah_pancake.feb24.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.AccessBus;
import tripleo.elijah.comp.DeducePipeline;
import tripleo.elijah.comp.GeneratePipeline;
import tripleo.elijah.comp.PipelineMember;
import tripleo.elijah.comp.WriteMesonPipeline;
import tripleo.elijah.comp.WritePipeline;
import tripleo.elijah.comp.internal.PipelinePlugin;

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
