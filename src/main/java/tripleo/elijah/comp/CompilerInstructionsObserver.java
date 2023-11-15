package tripleo.elijah.comp;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

class CompilerInstructionsObserver implements Observer<CompilerInstructions> {
	private final Compilation                compilation;
	final         List<CompilerInstructions> l = new ArrayList<>();
	private final OptionsProcessor op;

	public CompilerInstructionsObserver(final Compilation aCompilation, final OptionsProcessor aOp) {
		compilation = aCompilation;
		op = aOp;
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
		try {
			compilation.hasInstructions(l, compilation.do_out, op);
		} catch (Exception aE) {
			throw new RuntimeException(aE);
		}
	}
}
