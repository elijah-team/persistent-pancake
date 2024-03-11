/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.eljiah_pancake_durable.stages.gen_fn;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.elijah.work.WorkManager;
import tripleo.elijah_durable_pancake.comp.AccessBus;
import tripleo.elijah_durable_pancake.comp.PipelineLogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created 5/16/21 12:35 AM
 */
public class GeneratePhase {
	public final WorkManager wm = new WorkManager();

	final         Map<OS_Module, GenerateFunctions> generateFunctions = new HashMap<OS_Module, GenerateFunctions>();
	private final ElLog.Verbosity                   verbosity;
	private final PipelineLogic                     pipelineLogic;

	public GeneratePhase(final AccessBus aAccessBus, final PipelineLogic aPipelineLogic) {
		verbosity     = aAccessBus.getCompilation().testSilence(); // ca.testSilence

		pipelineLogic = aPipelineLogic;

		Compilation compilation = aAccessBus.getCompilation();
	}

	@NotNull
	public GenerateFunctions getGenerateFunctions(@NotNull final OS_Module mod) {
		final GenerateFunctions Result;
		if (generateFunctions.containsKey(mod)) {
			Result = generateFunctions.get(mod);
		} else {
			Result = new GenerateFunctions(this, mod, pipelineLogic);
			generateFunctions.put(mod, Result);
		}
		return Result;
	}

	public ElLog.Verbosity getVerbosity() {
		return verbosity;
	}
}

//
//
//
