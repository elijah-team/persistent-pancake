package tripleo.elijah.factory.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah_durable_pancake.comp.impl.IO_;
import tripleo.elijah_durable_pancake.comp.impl.StdErrSink;
import tripleo.elijah_durable_pancake.comp.internal.CompilationImpl;

import java.util.List;

public class CompilationFactory {

	public static Compilation mkCompilation2(final List<IFunctionMapHook> aMapHooks) {
		final StdErrSink errSink = new StdErrSink();
		final IO         io      = new IO_();

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
		var io1  = new IO_();

		return mkCompilation(eee1, io1);
	}
}
