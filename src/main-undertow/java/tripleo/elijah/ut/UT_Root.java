package tripleo.elijah.ut;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static tripleo.elijah.util.Helpers.List_of;

class UT_Root {
	List<Path> paths = List_of();

	public UT_Root() {
		final Path path = Paths.get("test");

		try {
			paths = CompilationsListHandler.listFiles(path);
		} catch (final IOException aE) {
//			throw new RuntimeException(aE);
			System.err.println(aE);
			return;
		}

		paths.forEach(x -> System.out.println(x));
	}
}
