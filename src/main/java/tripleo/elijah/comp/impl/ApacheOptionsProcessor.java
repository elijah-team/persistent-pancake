package tripleo.elijah.comp.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.comp.i.Compilation;
import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.comp.i.OptionsProcessor;
import tripleo.elijah.comp.internal.CompilationBus;
import tripleo.vendor.org.apache.commons.cli.CommandLine;
import tripleo.vendor.org.apache.commons.cli.CommandLineParser;
import tripleo.vendor.org.apache.commons.cli.DefaultParser;
import tripleo.vendor.org.apache.commons.cli.Options;

import java.util.List;

public class ApacheOptionsProcessor implements OptionsProcessor {
	final Options           options = new Options();
	final CommandLineParser clp     = new DefaultParser();

	@Contract(pure = true)
	public ApacheOptionsProcessor() {
		options.addOption("s", true, "stage: E: parse; O: output");
		options.addOption("showtree", false, "show tree");
		options.addOption("out", false, "make debug files");
		options.addOption("silent", false, "suppress DeduceType output to console");
	}

	@Override
	public String[] process(final @NotNull Compilation c, final @NotNull List<CompilerInput> aInputs, final CompilationBus aCb) throws Exception {
		final CommandLine cmd;

		cmd = clp.parse(options, aInputs);

		if (cmd.hasOption("s")) {
			new CC_SetStage(cmd.getOptionValue('s')).apply(c);
		}
		if (cmd.hasOption("showtree")) {
			new CC_SetShowTree(true).apply(c);
		}

		if (Compilation.CompilationAlways.isGitlab_ci() || cmd.hasOption("silent")) {
			new CC_SetSilent(true).apply(c);
		}

		return cmd.getArgs();
	}
}
