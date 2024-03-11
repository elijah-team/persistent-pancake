package tripleo.eljiah_pancake_durable.stages.gen_c;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EX_Explanation;
import tripleo.eljiah_pancake_durable.stages.deduce.post_bytecode.DeduceElement3_ProcTableEntry;

public class EX_ProcTableEntryExplanation implements EX_Explanation {
	private final DeduceElement3_ProcTableEntry pte;

	public EX_ProcTableEntryExplanation(final @NotNull DeduceElement3_ProcTableEntry aPte) {
		pte = aPte;
	}
}
