package tripleo.eljiah_pancake_durable.nextgen.outputtree;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.jetbrains.annotations.*;
import tripleo.elijah.nextgen.outputstatement.*;
import tripleo.eljiah_pancake_durable.stages.gen_generic.GenerateResultItem;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Naming;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_SequenceStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_Statement;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author tripleo
 */
public class EOT_OutputTree {
	private final List<EOT_OutputFile> list = new ArrayList<>();

	public void add(final @NotNull EOT_OutputFile aOutputFile) {
		// 05/18
		// 11/21: re
//		System.err.printf("-- [EOT_OutputTree::add] %s %s%n", aOutputFile.getFilename(), aOutputFile.getStatementSequence().getText());

		list.add(aOutputFile);
	}

	public @NotNull List<EOT_OutputFile> getList() {
		return list;
	}

	public void recompute() {
		// TODO big wtf
		final Multimap<String, EOT_OutputFile> mmfn = ArrayListMultimap.create();
		for (final EOT_OutputFile outputFile : list) {
			mmfn.put(outputFile.getFilename(), outputFile);
		}

		for (final Map.Entry<String, Collection<EOT_OutputFile>> sto : mmfn.asMap().entrySet()) {
			final var tt = sto.getValue();
			if (tt.size() > 1) {
				list.removeAll(tt);

				final var model = tt.iterator().next();

				final var type   = model.getType();
				final var inputs = model.getInputs(); // FIXME can possibly contain others
				final var filename = sto.getKey();

				final List<EG_Statement> list2 = _EOT_OutputTree__Utils._extractStatementSequenceFromAllOutputFiles(tt);

				final var seq = new EG_SequenceStatement(new EG_Naming("redone"), list2);
				final var ofn = new EOT_OutputFile(inputs, filename, type, seq);

				list.add(ofn);
			}
		}
	}

	public void set(final @NotNull List<EOT_OutputFile> aLeof) {
		list.addAll(aLeof);
	}

	public void _putSeq(final String aKey, final Path aPath, final EG_CompoundStatement aSeq) {
//		_putSeq(aKey, aPath, aSeq);
	}

	public void addGenerateResultItem(final GenerateResultItem ab, final Supplier<EOT_FileNameProvider> aSupplier) {
		final EOT_FileNameProvider fn       = aSupplier.get();
		final String               filename = fn.getFilename();

		Preconditions.checkNotNull(filename);

		ab.output = filename;
	}
}
