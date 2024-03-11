package tripleo.eljiah_pancake_durable.test_help;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Module;

import java.util.function.Consumer;

public class BoilerplateModuleBuilder {
	private final OS_Module mod;

	public BoilerplateModuleBuilder(final OS_Module aMod) {
		mod = aMod;
	}

//	public BoilerplateClassBuilder addClass(final String aClassNameString) {
//		return new BoilerplateClassBuilder(mod, aClassNameString);
//	}

	public void addClass(final @NotNull Consumer<BoilerplateClassBuilder> aClassBuilderConsumer) {
		final BoilerplateClassBuilder bcb = new BoilerplateClassBuilder();
		bcb.module(mod);

		aClassBuilderConsumer.accept(bcb);

		final ClassStatement cs = bcb.build();
		cs.postConstruct();
	}

}
