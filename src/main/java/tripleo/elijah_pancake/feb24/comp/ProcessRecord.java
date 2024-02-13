package tripleo.elijah_pancake.feb24.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

public class ProcessRecord {
	public final AccessBus ab;

	public ProcessRecord(final @NotNull ICompilationAccess ca0) {
		ab = new AccessBus(ca0);

		ab.addPipelinePlugin(new __Plugins.GeneratePipelinePlugin());
		ab.addPipelinePlugin(new __Plugins.DeducePipelinePlugin());
		ab.addPipelinePlugin(new __Plugins.WritePipelinePlugin());
		ab.addPipelinePlugin(new __Plugins.WriteMesonPipelinePlugin());

		ab.addPipelineLogic(PipelineLogic::new);
//		ab.add(DeducePipeline::new);
	}

	public void writeLogs(final @NotNull ICompilationAccess ca) {
		//ab.writeLogs();
		ca.getStage().writeLogs(ca);
	}
}
