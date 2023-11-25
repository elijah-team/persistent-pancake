package tripleo.elijah.comp;

import org.jetbrains.annotations.Nullable;
import tripleo.elijah.ci.LibraryStatementPart;
import tripleo.elijah.util.Operation2;
import tripleo.elijah.world.i.WorldModule;

import java.io.File;

public final class InputRequest {
	private final File                    _file;
	private final LibraryStatementPart    lsp;
	private       Operation2<WorldModule> op;

	public InputRequest(final File aFile, final @Nullable LibraryStatementPart aLsp) {
		_file   = aFile;
		lsp     = aLsp;
	}

	public File file() {
		return _file;
	}

	public LibraryStatementPart lsp() {
		return lsp;
	}

	public void setOp(final Operation2<WorldModule> aOwm) {
		op = aOwm;
	}
}
