package tripleo.eljiah_pancake_durable.stages.gen_generic;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.stages.gen_c.GenerateC;

import java.util.Objects;

public final class OutputFileFactory {
	private OutputFileFactory() {
	}

	@Contract("_, _ -> new")
	public static @NotNull GenerateFiles create(final @NotNull String lang,
	                                            final @NotNull OutputFileFactoryParams params) {
		if (Objects.equals(lang, "c")) {
			return new GenerateC(params);
		} else {
//			throw new NotImplementedException();
			return new GenerateC(params);
		}
	}
}
