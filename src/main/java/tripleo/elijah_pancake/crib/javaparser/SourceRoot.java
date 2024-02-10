package tripleo.elijah_pancake.crib.javaparser;

import tripleo.elijah.UnintendedUseException;
import tripleo.elijah_pancake.crib.javaparser.utils.ParserConfiguration;

import java.nio.file.Path;

public class SourceRoot {
	private Path path;

	public SourceRoot(final Path aPath) {
		path = aPath;
	}

	public SourceRoot setParserConfiguration(final ParserConfiguration aParserConfiguration) {
		throw new UnintendedUseException();
	}
}
