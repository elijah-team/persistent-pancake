package tripleo.elijah_pancake.pipelines.write;

import org.jdeferred2.DoneCallback;
import org.jetbrains.annotations.NotNull;

import tripleo.eljiah_pancake_durable.comp.Compilation;
import tripleo.elijah.util.Ok;
import tripleo.elijah.util.Operation;
import tripleo.elijah.util.io.CharSink;
import tripleo.elijah.util.io.FileCharSink;

import tripleo.eljiah_pancake_durable.comp.WritePipeline;

import tripleo.eljiah_pancake_durable.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.eljiah_pancake_durable.nextgen.outputtree.EOT_OutputTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public record Googly(WritePipeline wp, Compilation c, File aChosen) {
	public void googly_mkdirs(final File aChosenDirectoryName) {
		assert aChosenDirectoryName == aChosen;
		aChosenDirectoryName.mkdirs();
	}

	public @NotNull Operation<Ok> write_inputs(final File aChosenDirectoryName) {
		assert aChosenDirectoryName == aChosen;
		try {
			wp.write_inputs(aChosenDirectoryName);
			return Operation.success(Ok.instance());
		} catch (IOException aE) {
			return Operation.failure(aE);
		}
	}

	public @NotNull Operation<Ok> googly_write(final Path aPath, final EG_CompoundStatement aSeq) {
		System.out.println("201 Writing path: " + aPath);

		try {
			final CharSink x = c.getIO().openWrite(aPath);
			x.accept(aSeq.getText());
			((FileCharSink) x).close();
			return Operation.success(Ok.instance());
		} catch (IOException aE) {
			return Operation.failure(aE);
		}
	}

	public void googly_putSeq(final String aKey, final Path aPath, final EG_CompoundStatement aSeq) {
		final @NotNull EOT_OutputTree cot = c.getOutputTree();
		cot._putSeq(aKey, aPath, aSeq);
	}

	public void onChosen(final DoneCallback<File> cb) { // WP_Path
		wp._p_chosen.then(cb);
	}


}
