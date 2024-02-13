package tripleo.elijah_durable_pancake.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.stages.logging.ElLog;
import tripleo.elijah_durable_pancake.comp.functionality.f202.F202;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CA_writeLogs {
	public static void apply(final boolean aSilent, final List<ElLog> aLogs, final Compilation compilation) {
		final Multimap<String, ElLog> logMap = ArrayListMultimap.create();
		if (true) {
			for (final ElLog deduceLog : aLogs) {
				logMap.put(deduceLog.getFileName(), deduceLog);
			}
			for (final Map.Entry<String, Collection<ElLog>> stringCollectionEntry : logMap.asMap().entrySet()) {
				final F202 f202 = new F202(compilation.getErrSink(), compilation);
				f202.processLogs(stringCollectionEntry.getValue());
			}
		}
	}
}
