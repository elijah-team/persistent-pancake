package tripleo.elijah.comp.queries;

import java.io.InputStream;

public class QuerySourceFileToModuleParams {
	public final InputStream inputStream;
	public final String      sourceFilename;
	public QuerySourceFileToModuleParams(final InputStream aInputStream, final String aSourceFilename) {
		inputStream    = aInputStream;
		sourceFilename = aSourceFilename;
	}
}
