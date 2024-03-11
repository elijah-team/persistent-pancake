package tripleo.eljiah_pancake_durable.nextgen.outputstatement;

import tripleo.eljiah_pancake_durable.util.Helpers;
import tripleo.elijah_pancake.pipelines.write.MB;

import java.util.stream.Collectors;

public class GE_BuffersStatement implements EG_Statement {
	private final MB.S0 entry;

	public GE_BuffersStatement(final MB.S0 aEntry) {
		entry = aEntry;
	}

	@Override
	public String getText() {
		return Helpers.String_join("\n\n", entry.cb()
		                                        .stream()
		                                        .map(buffer -> buffer.getText())
		                                        .collect(Collectors.toList()));
	}

	@Override
	public EX_Explanation getExplanation() {
		return new GE_BuffersExplanation(this);
	}

	private static class GE_BuffersExplanation implements EX_Explanation {
		final         String              message = "buffers to statement";
		private final GE_BuffersStatement st;

		public GE_BuffersExplanation(final GE_BuffersStatement aGEBuffersStatement) {
			st = aGEBuffersStatement;
		}

		public String getText() {
			return message;
		}
	}
}
