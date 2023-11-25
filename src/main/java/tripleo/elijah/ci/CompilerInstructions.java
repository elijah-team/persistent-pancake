package tripleo.elijah.ci;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.ci_impl.LibraryStatementPartImpl;
import tripleo.elijah.comp.CompilerInput;
import tripleo.elijah.compiler_model.CM_Filename;
import tripleo.elijah.xlang.LocatableString;

//import java.io.File;
import java.util.List;

public interface CompilerInstructions {

//	File makeFile();

	CiIndexingStatement indexingStatement();

	void add(GenerateStatement generateStatement);

	void add(LibraryStatementPartImpl libraryStatementPart);

	CM_Filename getFilename();

	void setFilename(String filename);

	void setFilename(CM_Filename filename);

	void add(@NotNull LibraryStatementPart libraryStatementPart);

	String genLang();

	String getName();

	void setName(String name);

	void setName(LocatableString name);

	List<LibraryStatementPart> getLibraryStatementParts();

	void advise(CompilerInput aCompilerInput);
}
