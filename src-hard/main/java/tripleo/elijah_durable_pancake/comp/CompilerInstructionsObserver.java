package tripleo.elijah_durable_pancake.comp;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.util.ArrayList;
import java.util.List;

public class CompilerInstructionsObserver implements Observer<CompilerInstructions> {
	private final List<CompilerInstructions> l = new ArrayList<>();
	private final Compilation                compilation;

	public CompilerInstructionsObserver(final Compilation aCompilation, final OptionsProcessor ignoredAOp) {
		compilation = aCompilation;
	}

	public CompilerInstructionsObserver(final Compilation aCompilation, final OptionsProcessor ignoredAOp, final EDP_Compilation.CIS cis) {
		compilation = aCompilation;
		cis.set_cio(this);

		cis.subscribe(this);
	}

	@Override
	public void onSubscribe(@NonNull final Disposable d) {

	}

	@Override
	public void onNext(@NonNull final CompilerInstructions aCompilerInstructions) {
		l.add(aCompilerInstructions);
		//NotImplementedException.raise();
	}

	@Override
	public void onError(@NonNull final Throwable e) {
		NotImplementedException.raise();
	}

	@Override
	public void onComplete() {
		throw new RuntimeException();
	}

	public void almostComplete() {
		try {
			compilation.hasInstructions(l);
		} catch (final Exception aE) {
			compilation.getErrSink().exception(aE);
//			NotImplementedException.raise();
			throw new RuntimeException(aE);
		}
	}
}
