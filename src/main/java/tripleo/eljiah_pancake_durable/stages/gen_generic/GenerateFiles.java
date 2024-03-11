package tripleo.eljiah_pancake_durable.stages.gen_generic;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.nextgen.model.SM_Node;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedNode;
import tripleo.elijah.work.WorkManager;

import java.util.Collection;
import java.util.List;

public interface GenerateFiles extends CodeGenerator {

	GenerateResult generateCode(final @NotNull Collection<GeneratedNode> aNodeCollection, final @NotNull WorkManager aWorkManager);

	void forNode(final SM_Node aNode);

	GenerateResult resultsFromNodes(List<GeneratedNode> aNodes, WorkManager aWm);
}
