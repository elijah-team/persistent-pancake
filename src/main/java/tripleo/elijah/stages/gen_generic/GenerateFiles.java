package tripleo.elijah.stages.gen_generic;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.nextgen.model.SM_ClassDeclaration;
import tripleo.elijah.nextgen.model.SM_Node;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.work.WorkManager;

import java.util.Collection;

public interface GenerateFiles extends CodeGenerator {

	GenerateResult generateCode(final @NotNull Collection<GeneratedNode> aNodeCollection, final @NotNull WorkManager aWorkManager);

	void forNode(final SM_ClassDeclaration aNode);

	void forNode(SM_Node aNode);
}
