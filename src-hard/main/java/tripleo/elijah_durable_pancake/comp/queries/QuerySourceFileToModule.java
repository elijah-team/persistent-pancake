package tripleo.elijah_durable_pancake.comp.queries;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import tripleo.eljiah_pancake_durable.Out;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.nextgen.query.QueryDatabase;
import tripleo.elijah.util.NotImplementedException;
import tripleo.elijah.util.Operation;
import tripleo.elijjah.ElijjahLexer;
import tripleo.elijjah.ElijjahParser;

import java.io.InputStream;

public class QuerySourceFileToModule {
	private final QuerySourceFileToModuleParams params;
	private final Compilation                                               compilation;

	public QuerySourceFileToModule(final QuerySourceFileToModuleParams aParams, final Compilation aCompilation) {
		params      = aParams;
		compilation = aCompilation;
	}

	public OS_Module load(final QueryDatabase qb) {
		throw new NotImplementedException();
	}

	public Operation<OS_Module> calculate() {
		final String      f      = params.sourceFilename();
		final InputStream s      = params.inputStream();
		final boolean     do_out = false;

		final ElijjahLexer lexer = new ElijjahLexer(s);
		lexer.setFilename(f);
		final ElijjahParser parser = new ElijjahParser(lexer);
		parser.out = new Out(f, compilation, do_out);
		parser.setFilename(f);

		try {
			parser.program();
		} catch (final AssertionError | TokenStreamException | RecognitionException aE) {
			return Operation.failure(aE);
		}

		final OS_Module module = parser.out.module();
		parser.out = null;
		return Operation.success(module);
	}


}
