package tripleo.elijah.comp;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci.CompilerInstructions;
import tripleo.elijah.comp.internal.CompilationImpl;
import tripleo.elijah.comp.i.LCM_CompilerAccess;
import tripleo.elijah.lang.ClassStatement;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.lang.OS_Package;
import tripleo.elijah.lang.Qualident;
import tripleo.elijah.nextgen.outputtree.EOT_FileNameProvider;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.util.Operation2;
import tripleo.elijah.stages.deduce.DeducePhase;
import tripleo.elijah.stages.deduce.fluffy.i.FluffyComp;
import tripleo.elijah.stages.gen_fn.GeneratedNode;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah.testing.comp.IFunctionMapHook;
import tripleo.elijah.util.Operation;
import tripleo.elijah.world.i.LivingRepo;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Compilation {

	void hasInstructions(@NotNull List<CompilerInstructions> cis) throws Exception;

	void feedCmdLine(@NotNull List<String> args);

	String getProjectName();

	OS_Module realParseElijjahFile(String f, @NotNull File file, boolean do_out) throws Exception;

	Operation<CompilerInstructions> parseEzFile(@NotNull File aFile);

	void pushItem(CompilerInstructions aci);

	List<ClassStatement> findClass(String string);

	void use(@NotNull CompilerInstructions compilerInstructions, boolean do_out) throws Exception;

	int errorCount();

	void writeLogs(boolean aSilent, @NotNull List<ElLog> aLogs);

	IO getIO();

	void addModule(OS_Module module, String fn);

	OS_Module fileNameToModule(String fileName);

	ErrSink getErrSink();

	boolean getSilence();

	Operation2<OS_Module> findPrelude(String prelude_name);

	void addFunctionMapHook(IFunctionMapHook aFunctionMapHook);

	@NotNull DeducePhase getDeducePhase();

	int nextClassCode();

	int nextFunctionCode();

	OS_Package getPackage(Qualident pkg_name);

	OS_Package makePackage(Qualident pkg_name);

	int compilationNumber();

	String getCompilationNumberString();

	@Deprecated
	int modules_size();

	@NotNull EOT_OutputTree getOutputTree();

	@NotNull FluffyComp getFluffy();

	@NotNull List<GeneratedNode> getLGC();

	boolean isPackage(String aPackageName);

	void feedCmdLine(List<String> args, CompilerController ctl);

	void addCodeOutput(EOT_FileNameProvider aFileNameProvider,
	                   Supplier<EOT_OutputFile> aOutputFileSupplier,
	                   boolean addFlag);

	void provide(ElSystem aSystem);

	int getOutputTreeSize();

	Pipeline getPipelines();

	ModuleBuilder moduleBuilder();

	Finally reports();

	void writeLogs();

	List<ElLog> _elLogs();

	CompilationImpl.CompilationConfig _cfg();

	ElLog.Verbosity testSilence();

	Stream<OS_Module> modulesStream();

	void acceptElLog(ElLog aLog);

	CompilationImpl.CIS get_cis();

	void setRunner(CompilationRunner aCompilationRunner);

	CompilationRunner getRunner();

	LivingRepo _repo();

	LCM_CompilerAccess getLCMAccess();

	class CompilationAlways {

		@NotNull
		public static String defaultPrelude() {
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
}
