package tripleo.elijah_durable_pancake.comp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.UnintendedUseException;
import tripleo.elijah.comp.CompilationAlways;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah_durable_pancake.compilation_runner.CC_SetShowTree;
import tripleo.elijah_durable_pancake.compilation_runner.CC_SetSilent;
import tripleo.elijah_durable_pancake.compilation_runner.CC_SetStage;

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
	public String[] process(final @NotNull Compilation0101 c,
	                        final @NotNull List<String> args,
	                        final @NotNull ICompilationBus cb) throws Exception {
		final CommandLine cmd;

		cmd = clp.parse(options, args.toArray(new String[args.size()]));

		if (cmd.hasOption("s")) {
			cb.option(new CC_SetStage(cmd.getOptionValue('s')));
		}
		if (cmd.hasOption("showtree")) {
			cb.option(new CC_SetShowTree(true));
		}
		if (cmd.hasOption("out")) {
			throw new UnintendedUseException();
		}

		if (CompilationAlways.isGitlab_ci() || cmd.hasOption("silent")) {
			cb.option(new CC_SetSilent(true));
		}

		return cmd.getArgs();
	}
}
