package tripleo.elijah.comp;

import tripleo.elijah.comp.impl.ApacheOptionsProcessor;
import tripleo.elijah.comp.internal.CompilationBus;

import java.util.List;

public class DefaultCompilerController implements CompilerController {
	List<String>   args;
	String[]       args2;
	CompilationBus cb;
	private Compilation c;
	List<CompilerInput> inputs;

	@Override
	public void printUsage() {
		System.out.println("Usage: eljc [--showtree] [-sE|O] <directory or .ez file names>");
	}

	@Override
	public void processOptions() {
		final OptionsProcessor             op  = new ApacheOptionsProcessor();
		final CompilerInstructionsObserver cio = new CompilerInstructionsObserver(c, op, c.get_cis());
		cb = new CompilationBus(c);

		try {
//			args2 = op.process(c, args, cb);
			args2 = op.process(c, inputs, cb);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void runner() {
		try {
			c.setRunner(new CompilationRunner(c, c.get_cis(), cb));
			c.getRunner().doFindCIs(args2, cb);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	public void _set(final Compilation aCompilation, final List<String> aArgs) {
		c    = aCompilation;
		args = aArgs;
	}
}
