package tripleo.elijah.ut;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompilationsListHandler implements HttpHandler {

	//	private final Map<Integer, Compilation> cs = new HashMap<>();


	private final UT_Root utr;

	public CompilationsListHandler(final UT_Root aUtr) {
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

		final StringBuilder sb = new StringBuilder();

		sb.append("<html><body>\n");

		final List<Path> paths = utr.paths;

		for (int i = 0; i < paths.size(); i++) {
			final Path x = paths.get(i);
			sb.append("<a href=\"/start/" + i + "\">" + x + "</a><br>\n");
		}

		sb.append("</body></html>\n");

		exchange.getResponseSender().send(sb.toString());
	}
}
