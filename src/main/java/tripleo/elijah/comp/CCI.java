package tripleo.elijah.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.stages.deduce.post_bytecode.Maybe;

class CCI {
	//private final @NotNull Compilation compilation;
	private final Compilation.CIS _cis;

	@Contract(pure = true)
	CCI(final @NotNull Compilation aCompilation, final Compilation.CIS aCis) {
		//compilation = aCompilation;
		_cis = aCis;
	}

	public void accept(final @NotNull Maybe<ILazyCompilerInstructions> mcci) {
		if (mcci.isException()) return;

		final ILazyCompilerInstructions cci = mcci.o;
		final CompilerInstructions      ci  = cci.get();

		System.err.println("*** " + ci.getName());

		_cis.onNext(ci);
		//compilation.pushItem(ci);
	}
}
