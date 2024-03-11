package tripleo.eljiah_pancake_durable.ci;

import antlr.Token;
import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.ci_impl.LibraryStatementPartImpl;
import tripleo.elijah_durable_pancake.comp.CompilerInput;

import java.io.File;
import java.util.List;

public interface CompilerInstructions {

	File makeFile();

	CiIndexingStatement indexingStatement();

	void add(GenerateStatement generateStatement);

	void add(LibraryStatementPartImpl libraryStatementPart);

	String getFilename();

	void setFilename(String filename);

	void add(@NotNull LibraryStatementPart libraryStatementPart);

	String genLang();

	String getName();

	void setName(String name);

	void setName(Token name);

	List<LibraryStatementPart> getLibraryStatementParts();

	void advise(CompilerInput aCompilerInput);

	List<LibraryStatementPart> lsps();
}