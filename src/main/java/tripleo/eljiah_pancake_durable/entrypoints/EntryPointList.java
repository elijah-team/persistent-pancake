package tripleo.eljiah_pancake_durable.entrypoints;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GenerateFunctions;
import tripleo.elijah.work.WorkList;
import tripleo.elijah.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntryPointList {

	final @NotNull List<EntryPoint> eps;

	@Contract(pure = true)
	public EntryPointList() {
		eps = new ArrayList<>();
	}

	private void generateFromEntryPoints(final DeducePhase deducePhase,
										 final GenerateFunctions aGenerateFunctions,
										 final WorkManager wm) {
		if (eps.size() == 0) return; // short circuit


		final WorkList wl = new WorkList();

		for (final EntryPoint entryPoint : eps) {
			final EntryPointProcessor epp = EntryPointProcessor.dispatch(entryPoint, deducePhase, wl, aGenerateFunctions);
			epp.process();
		}

		wm.addJobs(wl);
		wm.drain();
	}

	public void generate(@NotNull final GenerateFunctions aGenerateFunctions, final DeducePhase aDeducePhase, @NotNull final Supplier<WorkManager> wm) {
		generateFromEntryPoints(aDeducePhase, aGenerateFunctions, wm.get());
	}

	public void add(final EntryPoint aEntryPoint) {
		eps.add(aEntryPoint);
	}

	public List<EntryPoint> _getMods() {
		return eps;
	}

	public int size() {
		return eps.size();
	}
}
