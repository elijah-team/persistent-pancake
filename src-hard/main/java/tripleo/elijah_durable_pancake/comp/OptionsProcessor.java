package tripleo.elijah_durable_pancake.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.ICompilationBus;

import java.util.List;

//@FunctionalInterface
public interface OptionsProcessor {
	//String[] process(final Compilation c, final List<String> args) throws Exception;

	String[] process(@NotNull Compilation0101 c,
	                 @NotNull List<String> args,
	                 @NotNull ICompilationBus cb) throws Exception;
}
