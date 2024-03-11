package tripleo.elijah_durable_pancake.comp;

import tripleo.eljiah_pancake_durable.ci.CompilerInstructions;
import tripleo.elijah.util.Maybe;
import tripleo.elijah.util.Operation2;
import tripleo.eljiah_pancake_durable.util._Extensionable;

import java.io.File;
import java.util.List;

public interface CompilerInput extends _Extensionable {
	File makeFile();

	void accept_ci(Maybe<ILazyCompilerInstructions> compilerInstructionsMaybe);

	void accept_hash(String hash);

	Maybe<ILazyCompilerInstructions> acceptance_ci();

	void certifyRoot();

	File getDirectory();

	boolean isElijjahFile();

	boolean isEzFile();

	boolean isNull();

	boolean isSourceRoot();

	void setArg();

	void setDirectory(File f);

	void setDirectoryResults(List<Operation2<CompilerInstructions>> aLoci);

	void setMaster(CompilerInputMaster master);

	void setSourceRoot();

	@Override
	String toString();

	Ty ty();

	public enum CompilerInputField {
		TY, HASH, ACCEPT_CI, DIRECTORY_RESULTS
	}

	public enum Ty {
		NULL, /* as is from command line/ */
		SOURCE_ROOT,
		ROOT, /* the base of the compilation */
		ARG, /* represents a compiler change (CC) */
		STDLIB
	}
}
