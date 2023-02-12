package tripleo.elijah.comp;

public interface ICompilationBus {
	void option(CompilationChange aChange);

	void inst(ILazyCompilerInstructions aLazyCompilerInstructions);
}
