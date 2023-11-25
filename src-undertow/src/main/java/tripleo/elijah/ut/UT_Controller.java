package tripleo.elijah.ut;

/*
import tripleo.elijah.comp.impl.ApacheOptionsProcessor;
import tripleo.elijah.comp.i.Compilation;
import tripleo.elijah.comp.impl2.CompilationRunner;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah.comp.impl2.CompilerInstructionsObserver;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah.comp.i.OptionsProcessor;
import tripleo.elijah.comp.i.IProgressSink;
import tripleo.elijah.comp.i.ProgressSinkComponent;
*/

import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.UnintendedUseException;
import tripleo.elijah.comp.impl.ApacheOptionsProcessor;
import tripleo.elijah.comp.i.Compilation;
import tripleo.elijah.comp.i.CompilerController;
import tripleo.elijah.comp.i.ICompilationBus;
import tripleo.elijah.comp.i.OptionsProcessor;

import java.util.List;

public class UT_Controller implements CompilerController {
	List<String>    args;
	String[]        args2;
	ICompilationBus cb;
	private final UT_Root      utr;
	private       Compilation c;

	public UT_Controller(final UT_Root aUtr) {
		utr = aUtr;
	}

	@Override
	public void printUsage() {
		System.out.println("Usage: eljc [--showtree] [-sE|O] <directory or .ez file names>");
	}

	@Override
	public void processOptions() {
		final OptionsProcessor             op  = new ApacheOptionsProcessor();
//		final CompilerInstructionsObserver cio = new CompilerInstructionsObserver(c, op, c._cis);
		cb = new UT_CompilationBus(c, this);

		try {
			List inputs = null;//string
			args2 = op.process(c, (List<CompilerInput>) inputs, cb);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void runner() {
		try {
			// FIXME 11/21 not now
			throw new UnintendedUseException();
//			c.__cr = new CompilationRunner(c, c._cis, cb, new IProgressSink() {
//				@Override
//				public void note(final int code, final ProgressSinkComponent component, final int type, final Object[] params) {
//					if (component.isPrintErr(code, type)) {
//						final String s = component.printErr(code, type, params);
//						System.err.println(s);
//					}
//				}
//			});
//			c.__cr.doFindCIs(args2, cb);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void _setInputs(final List<CompilerInput> aCompilerInputs) {
		throw new NotImplementedException();
	}

	public void _set(final Compilation aCompilation, final List<String> aArgs) {
		c    = aCompilation;
		args = aArgs;
	}

	public List<ICompilationBus.CB_Action> actions() {
		return ((UT_CompilationBus) cb).actions;
	}
}
