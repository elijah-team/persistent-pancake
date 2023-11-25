package tripleo.elijah.comp.i;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.util.Operation;

import java.io.File;
import java.util.Objects;

public interface ILazyCompilerInstructions {
	@Contract(value = "_ -> new", pure = true)
	static @NotNull ILazyCompilerInstructions of(final CompilerInstructions aCompilerInstructions) {
		return new ILazyCompilerInstructions() {
			@Override
			public CompilerInstructions get() {
				return aCompilerInstructions;
			}
		};
	}

	@Contract(value = "_, _ -> new", pure = true)
	static @NotNull ILazyCompilerInstructions of(final CompilerInput aCompilerInput, final CompilationClosure c) {
		return new ILazyCompilerInstructions() {
			@Override
			public CompilerInstructions get() {
				try {
					final Operation<CompilerInstructions> parsed = c.getCompilation().parseEzFile(new File(aCompilerInput.getInp()));
					return Objects.requireNonNull(parsed).success();
				} catch (final Exception aE) {
					throw new RuntimeException(aE); // TODO ugh
				}
			}
		};
	}

	CompilerInstructions get();
}
