package tripleo.eljiah_pancake_durable.nextgen.outputtree;

import org.jetbrains.annotations.NotNull;
import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.eljiah_pancake_durable.lang.OS_Module;
import tripleo.eljiah_pancake_durable.nextgen.inputtree.EIT_Input;
import tripleo.eljiah_pancake_durable.nextgen.inputtree.EIT_Input_HashSourceFile_Triple;
import tripleo.eljiah_pancake_durable.nextgen.inputtree.EIT_ModuleInput;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Naming;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_SequenceStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_SingleStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Statement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EX_Explanation;
import tripleo.util.buffer.Buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static tripleo.eljiah_pancake_durable.util.Helpers.List_of;

public class EOT_OutputFile {
	public static class DefaultFileNameProvider implements EOT_FileNameProvider {
		private final String r;

		public DefaultFileNameProvider(final String aR) {
			r = aR;
		}

		@Override
		public String getFilename() {
			return r;
		}
	}

	private final @NotNull EOT_FileNameProvider                  _filename;
	private final          List<EIT_Input>                       _inputs = new ArrayList<>();
	private final @NotNull EOT_OutputType                        _type;
	private final @NotNull EG_Statement                          _sequence; // TODO List<?> ??
	public                 List<EIT_Input_HashSourceFile_Triple> x;

	public EOT_OutputFile(final @NotNull List<EIT_Input> inputs, final @NotNull EOT_FileNameProvider filename,
			final @NotNull EOT_OutputType type, final @NotNull EG_Statement sequence) {
		_filename = filename;
		_type = type;
		_sequence = sequence;
		_inputs.addAll(inputs);
	}

	public EOT_OutputFile(final @NotNull List<EIT_Input> inputs,
	                      final @NotNull String filename,
	                      final @NotNull EOT_OutputType type,
	                      final @NotNull EG_Statement sequence) {
		this(inputs, new DefaultFileNameProvider(filename), type, sequence);
	}

	public String getFilename() {
		final String filename = _filename.getFilename();
		return filename;
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
