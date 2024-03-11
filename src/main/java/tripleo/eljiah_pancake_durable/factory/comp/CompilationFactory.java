package tripleo.eljiah_pancake_durable.factory.comp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.ErrSink;
import tripleo.eljiah_pancake_durable.comp.IO;
import tripleo.eljiah_pancake_durable.testing.comp.IFunctionMapHook;
import tripleo.elijah_durable_pancake.comp.impl.EDP_IO;
import tripleo.elijah_durable_pancake.comp.impl.EDP_ErrSink;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.util.List;

public class CompilationFactory {

	public static Compilation mkCompilation2(final List<IFunctionMapHook> aMapHooks) {
		final EDP_ErrSink errSink = new EDP_ErrSink();
		final IO          io      = new EDP_IO();

		final @NotNull EDP_Compilation c = mkCompilation(errSink, io);

		c.testMapHooks(aMapHooks);

		return c;
	}

	@Contract("_, _ -> new")
	public static @NotNull EDP_Compilation mkCompilation(final ErrSink eee, final IO io) {
		return new EDP_Compilation(eee, io);
	}

	public static @NotNull EDP_Compilation mkCompilationDefault() {
		var eee1 = new EDP_ErrSink();
		var io1  = new EDP_IO();

		return mkCompilation(eee1, io1);
	}
}
