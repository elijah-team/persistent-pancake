package tripleo.elijah.factory.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;
import tripleo.elijah.comp.IO;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.CompilationImpl;

import java.util.List;

public class CompilationFactory {

	public static Compilation mkCompilation2(final List<IFunctionMapHook> aMapHooks) {
		final EDP_ErrSink errSink = new EDP_ErrSink();
		final IO          io      = new EDP_IO();

		final @NotNull CompilationImpl c = mkCompilation(errSink, io);

		c.testMapHooks(aMapHooks);

		return c;
	}

	@Contract("_, _ -> new")
	public static @NotNull CompilationImpl mkCompilation(final ErrSink eee, final IO io) {
		return new CompilationImpl(eee, io);
	}

	public static @NotNull CompilationImpl mkCompilationDefault() {
		var eee1 = new EDP_ErrSink();
		var io1  = new EDP_IO();

		return mkCompilation(eee1, io1);
	}
}
