package tripleo.eljiah_pancake_durable.test_help;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.contexts.ModuleContext;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.FunctionDef;
import tripleo.eljiah_pancake_durable.lang.OS_Element;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.util.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BoilerplateClassBuilder {
	final private List<BCB_Child> children = new ArrayList<>();
	private       OS_Element      parent;
	private       String          classNameString;

	public BoilerplateClassBuilder() {
	}

	public void name(final String aClassNameString) {
		classNameString = aClassNameString;
	}

	public void addFunction(final @NotNull Consumer<BoilerplateFunctionBuilder> aFunctionBuilderConsumer) {
		final BoilerplateFunctionBuilder bfb = new BoilerplateFunctionBuilder();
//		bfb.parent(parent);

		aFunctionBuilderConsumer.accept(bfb);

		children.add(new BFCH_Function(bfb));
	}

	public void module(final OS_Module aMod) {
		parent = aMod;
	}

	public ClassStatement build() {
		final OS_Module      mod  = (OS_Module) parent;
		final ModuleContext  mctx = (ModuleContext) mod.getContext();
		final ClassStatement cs   = new ClassStatement(mod, mctx);

		cs.setName(Helpers.string_to_ident(classNameString));

		final BCB_State s = new BCB_State(cs);

		for (final BCB_Child child : children) {
			child.process(s);
		}

		return cs;
	}

	interface IBCB_State {
		ClassStatement getKlass();
	}

	interface BCB_Child {
		void process(IBCB_State s);
	}

	class BCB_State implements IBCB_State {
		private final ClassStatement classStatement;

		public BCB_State(final ClassStatement aClassStatement) {
			classStatement = aClassStatement;
		}

		@Override
		public ClassStatement getKlass() {
			return classStatement;
		}
	}

	class BFCH_Function implements BCB_Child {
		private final BoilerplateFunctionBuilder b;

		public BFCH_Function(final BoilerplateFunctionBuilder bb) {
			b = bb;
		}

		@Override
		public void process(final @NotNull IBCB_State s) {
			b.parent(s.getKlass());

			final FunctionDef fd = b.build();
			fd.postConstruct();
		}
	}

}
