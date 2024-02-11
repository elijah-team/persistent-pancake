package tripleo.elijah_pancake.sep1011.modeltransition;

import tripleo.elijah.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.stages.generate.OutputStrategyC;

import java.util.function.Supplier;

public interface ElSystemSink {
	void provide(ElSystem aSystem);

	OutputStrategyC strategy(OutputStrategy aOutputStrategy);

	void addGenerateResultItem(GenerateResultItem aGenerateResultItem, Supplier<EOT_FileNameProvider> aFileNameProviderSupplier);
}
