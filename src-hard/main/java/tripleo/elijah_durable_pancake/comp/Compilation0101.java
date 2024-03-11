package tripleo.elijah_durable_pancake.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.eljiah_pancake_durable.comp.CompilerController;
import tripleo.eljiah_pancake_durable.lang.ClassStatement;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.lang.OS_Package;
import tripleo.eljiah_pancake_durable.lang.Qualident;
import tripleo.eljiah_pancake_durable.stages.deduce.DeducePhase;
import tripleo.eljiah_pancake_durable.stages.gen_fn.GeneratedNode;
import tripleo.eljiah_pancake_durable.stages.logging.ElLog;
import tripleo.eljiah_pancake_durable.testing.comp.IFunctionMapHook;
import tripleo.elijah.util.Operation;
import tripleo.elijah.util.Operation2;
import tripleo.elijah_durable_pancake.comp.internal.EDP_Compilation;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public interface Compilation0101 {
	void hasInstructions(@NotNull List<CompilerInstructions> cis) throws Exception;

	OS_Module realParseElijjahFile(String f, @NotNull File file, boolean do_out) throws Exception;

	Operation<CompilerInstructions> parseEzFile(@NotNull File aFile);

	List<ClassStatement> findClass(String string);

	void use(@NotNull CompilerInstructions compilerInstructions, boolean do_out) throws Exception;

	void writeLogs(boolean aSilent, @NotNull List<ElLog> aLogs);

	void addModule(OS_Module module, String fn);

	OS_Module fileNameToModule(String fileName);

	Operation2<OS_Module> findPrelude(String prelude_name);

	void addFunctionMapHook(IFunctionMapHook aFunctionMapHook);

	@NotNull DeducePhase getDeducePhase();

	int nextClassCode();

	int nextFunctionCode();

	OS_Package getPackage(Qualident pkg_name);

	OS_Package makePackage(Qualident pkg_name);

	@NotNull List<GeneratedNode> getLGC();

	boolean isPackage(String aPackageName);

	void feedCmdLine(List<String> args, CompilerController ctl);

	Pipeline getPipelines();

	ModuleBuilder moduleBuilder();

	List<ElLog> _elLogs();

	Stream<OS_Module> modulesStream();

	void acceptElLog(ElLog aLog);

	EDP_Compilation.CIS get_cis();

	void setRunner(CompilationRunner aCompilationRunner);

	CompilationRunner getRunner();
}
