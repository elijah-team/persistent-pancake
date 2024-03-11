package tripleo.eljiah_pancake_durable.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputFile;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputTree;
import tripleo.eljiah_pancake_durable.stages.deduce.fluffy.i.FluffyComp;
import tripleo.eljiah_pancake_durable.stages.generate.ElSystem;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.world.i.LivingRepo;
import tripleo.elijah_durable_pancake.comp.Compilation0101;
import tripleo.eljiah_pancake_durable.comp.i.LCM_CompilerAccess;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;
import tripleo.elijah_pancake.feb24.comp.CompilationSignalTarget;
import tripleo.elijah_pancake.feb24.comp.Providing;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Compilation extends Compilation0101 {
	void feedCmdLine(@NotNull List<String> args);

	void pushItem(CompilerInstructions aci);

	int errorCount();

	IO getIO();

	ErrSink getErrSink();

	boolean getSilence();

	int compilationNumber();

	String getCompilationNumberString();

	@Deprecated
	int modules_size();

	@NotNull EOT_OutputTree getOutputTree();

	@NotNull FluffyComp getFluffy();

	void addCodeOutput(EOT_FileNameProvider aFileNameProvider,
	                   Supplier<EOT_OutputFile> aOutputFileSupplier,
	                   boolean addFlag);

	void provide(ElSystem aSystem);

	int getOutputTreeSize();

	Finally reports();

	void writeLogs();

	EDP_Compilation.CompilationConfig _cfg();

	ElLog.Verbosity testSilence();

	CentralController central();

	LivingRepo _repo();

	LCM_CompilerAccess getLCMAccess();

	<T> void provide(Class<T> aClass, Function<Providing, T> cb, Class<?>[] aClasses);

	void waitProviders(Class[] aClasses, Consumer<Providing> cb);

	void trigger(Class<?/* super CompilationSignal*/> aSignalClass);

	void onTrigger(Class<? /*super CompilationSignal*/> aSignalClass, CompilationSignalTarget aSignalTarget);

	String getProjectName();

	void _CS_RunBetter();
}
