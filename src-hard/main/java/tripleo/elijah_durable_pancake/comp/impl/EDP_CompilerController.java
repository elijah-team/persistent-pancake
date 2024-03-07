package tripleo.elijah_durable_pancake.comp.impl;

import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilerController;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah_durable_pancake.comp.ApacheOptionsProcessor;
import tripleo.elijah_durable_pancake.comp.CompilationRunner;
import tripleo.elijah_durable_pancake.comp.CompilerInstructionsObserver;
import tripleo.elijah_durable_pancake.comp.OptionsProcessor;
import tripleo.elijah_durable_pancake.comp.internal.EDP_CompilationBus;
import tripleo.elijah_pancake.feb24.comp.Startup;

import java.util.List;

public class EDP_CompilerController implements CompilerController {
	List<String>    args;
	String[]        args2;
	ICompilationBus cb;
	private Compilation c;

	@Override
	public void printUsage() {
		System.out.println("Usage: eljc [--showtree] [-sE|O] <directory or .ez file names>");
	}

	@Override
	public void processOptions() {
		final OptionsProcessor             op  = new ApacheOptionsProcessor();
		final CompilerInstructionsObserver cio = new CompilerInstructionsObserver(c, op, c.get_cis());
		cb = new EDP_CompilationBus(c);

		try {
			args2 = op.process(c, args, cb);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void runner() {
		try {
//			c.setRunner(new CompilationRunner(c, c.get_cis(), cb, startup));

			c.provide(CPP_InputList.class, (o) -> {
				return new CPP_InputList(args2, cb);
			}, new Class[]{});
			c.provide(CPP_Runner.class, (o) -> {
				var startup = new Startup(c);
				return new CPP_Runner(startup, new CompilationRunner(c, c.get_cis(), cb, startup));
			}, new Class[]{});

			c.trigger(CS_FindCIs.class);
		} catch (final Exception e) {
			c.getErrSink().exception(e);
			throw new RuntimeException(e);
		}
	}

	public void _set(final Compilation aCompilation, final List<String> aArgs) {
		c    = aCompilation;
		args = aArgs;

		c.onTrigger(CS_FindCIs.class, () -> c.waitProviders(
		  new Class[]{CPP_InputList.class, CPP_Runner.class} // be lazy/don't overspecify
		  , (o) -> {
			  c.getRunner().doFindCIs(args2, cb);
		  }));
		c.onTrigger(CPP_Runner.class, () -> c.waitProviders(
		  new Class[]{}
		  , (o) -> {
			  c.getRunner().doFindCIs(args2, cb);
		  }));
	}

	interface CS_FindCIs {
	}

	record CPP_InputList(String[] aArgs2, ICompilationBus aCb) {
	}

	record CPP_Runner(Startup st, CompilationRunner cr) {
	}
}
