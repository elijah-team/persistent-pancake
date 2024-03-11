package tripleo.elijah_durable_pancake.comp.impl;

import tripleo.elijah_durable_pancake.comp.CompilerSignal;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.comp.CompilerController;
import tripleo.eljiah_pancake_durable.comp.ICompilationBus;
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
		  new Class[]{CPP_InputList.class, CPP_Runner.class, CompilationRunner.class} // be lazy/don't overspecify
		  , (o) -> {
//			  final CompilationRunner r = c.getRunner();
//			  final CompilationRunner r = (CompilationRunner) o.get(CompilationRunner.class);
			  final CPP_Runner        cpprunner = (CPP_Runner) o.get(CPP_Runner.class);
			  final CompilationRunner r         = cpprunner.cr();
			  r.doFindCIs(args2, cb);
		  }));
		c.onTrigger(CPP_Runner.class, () -> c.waitProviders(
		  new Class[]{CompilationRunner.class}
		  , (o) -> {
//			  var r = c.getRunner();
			  CompilationRunner r = ((CompilationRunner) o.get(CompilationRunner.class));

			  r.doFindCIs(args2, cb);
		  }));
	}

	interface CS_FindCIs extends CompilerSignal {
	}

	record CPP_InputList(String[] aArgs2, ICompilationBus aCb) implements CompilerSignal {
	}

	record CPP_Runner(Startup st, CompilationRunner cr) implements CompilerSignal {
	}
}
