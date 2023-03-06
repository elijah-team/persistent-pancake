package tripleo.elijah.ut;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilationChange;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah.comp.ILazyCompilerInstructions;

import java.util.ArrayList;
import java.util.List;

public class UT_CompilationBus implements ICompilationBus {
	private final Compilation c;
	List<CB_Action> actions = new ArrayList<>();


	public UT_CompilationBus(final Compilation aC) {
		c = (aC);
	}

	@Override
	public void option(final @NotNull CompilationChange aChange) {
		aChange.apply(c);
	}

	@Override
	public void inst(final @NotNull ILazyCompilerInstructions aLazyCompilerInstructions) {
		System.out.println("** [ci] " + aLazyCompilerInstructions.get());
	}

	public void add(final CB_Action action) {
//		action.execute();
		actions.add(action);
	}

}
