package tripleo.elijah.ut;

import com.google.common.base.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.comp.CompilerController;
import tripleo.elijah.comp.ICompilationBus;
import tripleo.elijah.comp.IO;
import tripleo.elijah.comp.StdErrSink;
import tripleo.elijah.factory.comp.CompilationFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tripleo.elijah.util.Helpers.List_of;

public class CompilationsStartHandler implements HttpHandler {

	//	private final Map<Integer, Compilation> cs = new HashMap<>();


	private final UT_Root            utr;
	private       CompilerController utc = null;

	public CompilationsStartHandler(final UT_Root aUtr) {
		utr = aUtr;
	}

	public static List<Path> listFiles(final Path path) throws IOException {
		final List<Path> result;
		try (final Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files::isRegularFile)
			             .filter(x -> x.toFile().getName().endsWith(".ez"))
			             .collect(Collectors.toList());
		}
		return result;
	}

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");

		final StringBuilder sb    = new StringBuilder();
		final int           num   = Integer.valueOf(exchange.getRequestPath().substring(7));
		final List<Path>    paths = utr.paths;
		final Path          p     = paths.get(num);
		final Compilation   c     = CompilationFactory.mkCompilation(new StdErrSink(), new IO());

		if (utc == null) {
			Preconditions.checkNotNull(utr);

			utc = new UT_Controller(utr);
		}

		c.feedCmdLine(List_of(p.toString()), utc);

		sb.append("<html><body>\n");

		final List<ICompilationBus.CB_Action> actions = ((UT_Controller) utc).actions();

		for (int i = 0; i < paths.size(); i++) {
			final Path x = paths.get(i);
			sb.append("<a href=\"/start/" + i + "\">" + x + "</a><br>\n");
		}

		sb.append("</body></html>\n");

		exchange.getResponseSender().send(sb.toString());
	}
}
