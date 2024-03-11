package tripleo.eljiah_pancake_durable.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;

public class CompilationAlways {
	public static @NotNull String defaultPrelude() {
		return "c";
	}

	public static ElLog.Verbosity gitlabCIVerbosity() {
		final boolean gitlab_ci = isGitlab_ci();
		return gitlab_ci ? ElLog.Verbosity.SILENT : ElLog.Verbosity.VERBOSE;
	}

	public static boolean isGitlab_ci() {
		return System.getenv("GITLAB_CI") != null;
	}
}
