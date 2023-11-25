package tripleo.elijah.factory.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.comp.internal.CompilationImpl;
import tripleo.elijah.testing.comp.IFunctionMapHook;

import java.util.List;

public class CompilationFactory {

	public static CompilationImpl mkCompilation2(final List<IFunctionMapHook> aMapHooks) {
		final StdErrSink errSink = new StdErrSink();
		final IO         io      = new IO();

		final @NotNull CompilationImpl c = mkCompilation(errSink, io);

		c.testMapHooks(aMapHooks);

		return c;
	}

	@Contract("_, _ -> new")
	public static @NotNull CompilationImpl mkCompilation(final ErrSink eee, final IO io) {
		return new CompilationImpl(eee, io);
	}

	public static @NotNull CompilationImpl mkCompilationDefault() {
		var eee1 = new StdErrSink();
		var io1  = new IO();

		return mkCompilation(eee1, io1);
	}
}
