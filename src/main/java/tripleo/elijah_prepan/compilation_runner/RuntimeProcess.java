package tripleo.elijah_prepan.compilation_runner;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Contract;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.Compilation0101;
import tripleo.elijah.comp.i.ICompilationAccess;
import tripleo.elijah_durable_pancake.comp.Pipeline;
import tripleo.elijah_durable_pancake.comp.internal.PipelinePlugin;
import tripleo.elijah_pancake.feb24.comp.ProcessRecord;
import tripleo.vendor.mal.stepA_mal;
import tripleo.vendor.mal.types;

public interface RuntimeProcess {
	void run();

	void postProcess();

	void prepare() throws Exception;

	public final class EmptyProcess implements RuntimeProcess {
		public EmptyProcess(final ICompilationAccess aCompilationAccess, final ProcessRecord aPr) {
		}

		@Override
		public void postProcess() {
		}

		@Override
		public void run() {
		}

		@Override
		public void prepare() {
		}
	}


	public class DStageProcess implements RuntimeProcess {
		private final ICompilationAccess ca;
		private final ProcessRecord      pr;

		@Contract(pure = true)
		public DStageProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			ca = aCa;
			pr = aPr;
		}

		@Override
		public void run() {
			final int y = 2;
		}

		@Override
		public void postProcess() {
		}

		@Override
		public void prepare() {
			assert ca.getStage() == Stages.D;
		}
	}

	public class OStageProcess implements RuntimeProcess {
		private final ProcessRecord      pr;
		private final ICompilationAccess ca;
		final         stepA_mal.MalEnv2  env;

		public OStageProcess(final ICompilationAccess aCa, final ProcessRecord aPr) {
			ca = aCa;
			pr = aPr;

			env = new stepA_mal.MalEnv2(null); // TODO what does null mean?

			Preconditions.checkNotNull(pr.getAccessBus());
			env.set(new types.MalSymbol("add-pipeline"), new _AddPipeline__MAL(pr.getAccessBus()));
		}

		@Override
		public void run() {
			final AccessBus ab = pr.getAccessBus();

			ab.subscribePipelineLogic((pl) -> {
				final Compilation comp = ca.getCompilation();
				final Pipeline    ps   = comp.getPipelines();

				try {
					ps.run();

					ab.writeLogs();
				} catch (final Exception ex) {
//				Logger.getLogger(OStageProcess.class.getName()).log(Level.SEVERE, "Error during Piplines#run from OStageProcess", ex);
					comp.getErrSink().exception(ex);
				}
			});
		}

		@Override
		public void postProcess() {
		}

		@Override
		public void prepare() throws Exception {
			Preconditions.checkNotNull(pr);
			Preconditions.checkNotNull(pr.getAccessBus().gr);

			final AccessBus ab = pr.getAccessBus();

//		env.re("(def! GeneratePipeline 'native)");
			env.re("(add-pipeline 'DeducePipeline)"); // FIXME note moved from ...

//		ab.add(GeneratePipeline::new);
//		ab.add(WritePipeline::new);
//		ab.add(WriteMesonPipeline::new);
			env.re("(add-pipeline 'GeneratePipeline)");
			env.re("(add-pipeline 'WritePipeline)");
			env.re("(add-pipeline 'WriteMesonPipeline)");

			ab.subscribePipelineLogic(pl -> {
				final Compilation0101 comp = ca.getCompilation();

				comp.modulesStream().forEach(pl::addModule);
			});
		}

		private static class _AddPipeline__MAL extends types.MalFunction {
			private final AccessBus ab;

			public _AddPipeline__MAL(final AccessBus aAb) {
				ab = aAb;
			}

			public types.MalVal apply(final types.MalList args) throws types.MalThrowable {
				final types.MalVal a0 = args.nth(0);

				if (a0 instanceof final types.MalSymbol pipelineSymbol) {
					// 0. accessors
					final String pipelineName = pipelineSymbol.getName();

					// 1. observe side effect
					final PipelinePlugin pipelinePlugin = ab.getPipelinePlugin(pipelineName);
					if (pipelinePlugin == null)
						return types.False;

					// 2. produce effect
					ab.add(pipelinePlugin::instance);
					return types.True;
				} else {
					// TODO exception? errSink??
					return types.False;
				}
			}
		}
	}
}
