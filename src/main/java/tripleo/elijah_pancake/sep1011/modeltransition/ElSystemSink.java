package tripleo.elijah_pancake.sep1011.modeltransition;

import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.eljiah_pancake_durable.stages.gen_generic.GenerateResultItem;
import tripleo.eljiah_pancake_durable.stages.generate.ElSystem;
import tripleo.eljiah_pancake_durable.stages.generate.OutputStrategy;
import tripleo.eljiah_pancake_durable.stages.generate.OutputStrategyC;

import java.util.function.Supplier;

public interface ElSystemSink {
	void provide(ElSystem aSystem);

	OutputStrategyC strategy(OutputStrategy aOutputStrategy);

	void addGenerateResultItem(GenerateResultItem aGenerateResultItem, Supplier<EOT_FileNameProvider> aFileNameProviderSupplier);
}
