package tripleo.elijah.nextgen.outputtree;

import org.jetbrains.annotations.*;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.inputtree.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.util.buffer.Buffer;

import java.util.*;
import java.util.stream.Collectors;

import static tripleo.elijah.util.Helpers.List_of;

public class EOT_OutputFile {
	public static class DefaultFileNameProvider implements FileNameProvider {
		private final String r;

		public DefaultFileNameProvider(final String aR) {
			r = aR;
		}

		@Override
		public String getFilename() {
			return r;
		}
	}

	@FunctionalInterface
	public interface FileNameProvider {
		String getFilename();
	}

	private final @NotNull FileNameProvider _filename;
	private final List<EIT_Input> _inputs = new ArrayList<>();
	private final @NotNull EOT_OutputType _type;
	private final @NotNull EG_Statement _sequence; // TODO List<?> ??
	public List<EIT_Input_HashSourceFile_Triple> x;

	public EOT_OutputFile(final @NotNull List<EIT_Input> inputs, final @NotNull FileNameProvider filename,
			final @NotNull EOT_OutputType type, final @NotNull EG_Statement sequence) {
		_filename = filename;
		_type = type;
		_sequence = sequence;
		_inputs.addAll(inputs);
	}

	public EOT_OutputFile(final @NotNull List<EIT_Input> inputs, final @NotNull String filename,
			final @NotNull EOT_OutputType type, final @NotNull EG_Statement sequence) {
		this(inputs, new DefaultFileNameProvider(filename), type, sequence);
	}

	public String getFilename() {
		return _filename.getFilename();
	}

	public @NotNull List<EIT_Input> getInputs() {
		return _inputs;
	}

	public EG_Statement getStatementSequence() {
		return _sequence;
	}

	public EOT_OutputType getType() {
		return _type;
	}

	@Override
	public String toString() {
		return "(%s) '%s'".formatted(_type, _filename);
	}

	public static @NotNull EOT_OutputFile bufferSetToOutputFile(final String aFilename,
	                                                            final @NotNull Collection<Buffer> aBuffers,
	                                                            final Compilation comp,
	                                                            final OS_Module aModule) {
		final List<EIT_Input> inputs = List_of(new EIT_ModuleInput(aModule, comp));

		final List<EG_Statement> statementStream = aBuffers.stream()
		                                                   .map(buffer ->
		                                                     new EG_SingleStatement(buffer.getText(), new EX_Explanation() {
		                                                     })
		                                                   ).collect(Collectors.toList());
		final EG_SequenceStatement seq = new EG_SequenceStatement(new EG_Naming("yyy"), statementStream);

		final EOT_OutputFile eof = new EOT_OutputFile(inputs, aFilename, EOT_OutputType.SOURCES, seq);
		return eof;
	}
}
