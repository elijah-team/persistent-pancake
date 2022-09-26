package tripleo.elijah.comp;

import org.jdeferred2.Promise;
import org.jdeferred2.impl.DeferredObject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.gen_generic.GenerateResult;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ProcessRecord {
	final        DeducePipeline     dpl;
	public final PipelineLogic                              pipelineLogic;
	final        Stages                                     stage;
	private      DeferredObject<GenerateResult, Void, Void> _pgr;

	public ProcessRecord(final @NotNull ICompilationAccess ca) {
		final Compilation compilation = ca.getCompilation();

		pipelineLogic = new PipelineLogic(ca);
		dpl           = new DeducePipeline(compilation);
		stage         = compilation.stage;
	}

	public Promise<GenerateResult, Void, Void> generateResultPromise() {
		if (_pgr == null) {
			_pgr = new DeferredObject<>();
		}
		return _pgr;
	}

	public void setGenerateResult(final GenerateResult gr) {
		_pgr.resolve(gr);
	}

	public void consumeGenerateResult(final @NotNull Consumer<Supplier<GenerateResult>> csgr) {
		csgr.accept(() -> {
			final GenerateResult[] xx = new GenerateResult[1];
			generateResultPromise().then((x) -> xx[0] = x);
			return xx[0];
		});
	}
}
