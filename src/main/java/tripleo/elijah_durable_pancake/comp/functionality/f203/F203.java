package tripleo.elijah_durable_pancake.comp.functionality.f203;

import org.jetbrains.annotations.Contract;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.ErrSink;

import java.io.File;
import java.time.LocalDateTime;

public class F203 {
	final         ChooseDirectoryNameBehavior cdn;
	private final ErrSink                     errSink;
	final         LocalDateTime               localDateTime = LocalDateTime.now();

	@Contract(pure = true)
	public F203(final ErrSink aErrSink, final Compilation c) {
		errSink = aErrSink;
//		cdn = new ChooseCompilationNameBehavior(c);
		cdn = new ChooseHashDirectoryNameBehavior(c, localDateTime);
	}

	public File chooseDirectory() {
		return cdn.chooseDirectory();
	}
}
