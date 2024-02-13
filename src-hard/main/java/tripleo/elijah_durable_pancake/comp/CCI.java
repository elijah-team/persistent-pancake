package tripleo.elijah_durable_pancake.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.i.ProgressSinkComponent;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;
import tripleo.elijah.comp.i.IProgressSink;
import tripleo.elijah_durable_pancake.comp.internal.CompilationImpl;
import tripleo.elijah_durable_pancake.comp.internal.DefaultProgressSink;

class CCI {
	//private final @NotNull Compilation compilation;
	private final CompilationImpl.CIS _cis;
	private final IProgressSink       _ps;

	@Contract(pure = true)
	CCI(final @NotNull Compilation0101 aCompilation, final CompilationImpl.CIS aCis) {
		this(aCompilation, aCis, new DefaultProgressSink());
	}

	@Contract(pure = true)
	CCI(final @NotNull Compilation0101 aCompilation, final CompilationImpl.CIS aCis, final IProgressSink aProgressSink) {
		//compilation = aCompilation;
		_cis = aCis;
		_ps  = aProgressSink;
	}

	public void accept(final @NotNull Maybe<ILazyCompilerInstructions> mcci) {
		if (mcci.isException()) return;

		final ILazyCompilerInstructions cci = mcci.o;
		final CompilerInstructions      ci  = cci.get();

		_ps.note(131, ProgressSinkComponent.CCI, -1, new Object[]{ci.getName()});

		_cis.onNext(ci);
		//compilation.pushItem(ci);
	}
}
