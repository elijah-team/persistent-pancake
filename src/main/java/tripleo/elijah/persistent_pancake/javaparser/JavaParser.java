package tripleo.elijah.persistent_pancake.javaparser;

import org.jetbrains.annotations.Contract;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.queries.QuerySourceFileToModule;
import tripleo.elijah.comp.queries.QuerySourceFileToModuleParams;
import tripleo.elijah.factory.comp.CompilationFactory;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.persistent_pancake.javaparser.ast.CompilationUnit;
import tripleo.elijah.persistent_pancake.javaparser.ast.body.ClassOrInterfaceDeclaration;
import tripleo.elijah.util.Mode;
import tripleo.elijah.util.Operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

public class JavaParser {
	@Contract("_ -> new")
	public static CompilationUnit ___parse(final File aFile) throws ParseException, IOException {
//		ElijahSpec elijahSpec = new ES
		final Compilation                   c  = CompilationFactory.mkCompilationDefault();
		final QuerySourceFileToModuleParams p  = new QuerySourceFileToModuleParams(new FileInputStream(aFile), aFile.getAbsolutePath(), false);
		final QuerySourceFileToModule       q  = new QuerySourceFileToModule(p, c);
		final Operation<OS_Module>          om = q.calculate();
		if (om.mode() == Mode.SUCCESS) {
			final CompilationUnit cu = CompilationUnit.of(om.success());
			return cu;
		} else {
			throw new ParseException("operation failure", om);
		}
	}

	/**
	 * Parses the Java code contained in a {@link File} and returns a
	 * {@link CompilationUnit} that represents it.<br>
	 *
	 * @param file {@link File} containing Java source code. It will be closed after parsing.
	 * @return CompilationUnit representing the Java source code
	 * @throws ParseProblemException if the source code has parser errors
	 * @throws FileNotFoundException the file was not found
	 */
	public static ParseResult<CompilationUnit> parse(final File file) throws FileNotFoundException {
//		ParseResult<CompilationUnit> result = parse(COMPILATION_UNIT, provider(file, configuration.getCharacterEncoding()));
//		result.getResult().ifPresent(cu -> cu.setStorage(file.toPath(), configuration.getCharacterEncoding()));

		CompilationUnit cu;
		try {
			cu = ___parse(file);
		} catch (ParseException aE) {
//			throw new RuntimeException(aE);
			cu = null;
		} catch (IOException aE) {
			throw new RuntimeException(aE);
		}
		ParseResult<CompilationUnit> result = new ParseResult<>(cu
		  , null/*Collections.EMPTY_LIST*/
		  , null/*Collections.EMPTY_LIST*/
		);

		return result;
	}

	final int COMPILATION_UNIT = -1;
}
