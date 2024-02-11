package tripleo.elijah_pancake.pipelines.write;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.Eventual;
import tripleo.elijah.comp.Compilation;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.elijah.nextgen.outputstatement.EG_SingleStatement;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;
import tripleo.elijah.nextgen.outputstatement.EX_Explanation;
import tripleo.elijah.nextgen.outputstatement.GE_BuffersStatement;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.util.Mode;
import tripleo.util.buffer.Buffer;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MB {
	final         Multimap<String, Buffer> mb                         = ArrayListMultimap.create();
	final         Map<String, OS_Module>   modmap                     = new HashMap<String, OS_Module>();
	private final List<MBB>                bz                         = new ArrayList<>();
	private final Eventual<String>         chosenDirectoryNamePromise = new Eventual<>();


	public void add(final MBB aMBB) {
		bz.add(aMBB);
		mb.put(aMBB.getFileName(), aMBB.getBuffer());
	}

	public void put(final String aFileName, final Buffer aBuffer) {
		final MBB mbb = new MBB(aFileName, aBuffer);
		mbb.set_up(this);
		add(mbb);
	}

	public void modmap_put(final String aFileName, final OS_Module aModule) {
		modmap.put(aFileName, aModule);
	}

	public @NotNull Iterable<Map.Entry<String, Collection<Buffer>>> entries() {
		return mb.asMap().entrySet();
	}

	public Iterable<? extends MBB> bz() {
		return this.bz;
	}

	public Collection<Buffer> getBuffers(final MBB aMBB) {
		return mb.asMap().get(aMBB.getFileName());
	}

	public OS_Module getModule(final MBB aMBB) {
		return modmap.get(aMBB.getFileName());
	}

	public void bzTrigger(final Compilation aC) {
		for (final MBB entry : this.bz()) {
			final String fileName = entry.getFileName();

			aC.addCodeOutput(() -> remove_initial_slash(fileName), () -> {
				final Collection<Buffer> vs = entry.getBuffers();

				final EOT_OutputFile eof = EOT_OutputFile.bufferSetToOutputFile(fileName, vs, aC, entry.getModule());
				return eof;
			}, true);
		}
	}

	public void ingest(final List<GenerateResultItem> aResults) {
		for (final GenerateResultItem ab : aResults) {
			final String fileName = ab.output;

			put(fileName, ab.buffer);
			modmap_put(fileName, ab.node.module());
		}
	}

	public void resolve_chosen(final String prefix) {
		chosenDirectoryNamePromise.resolve(prefix);
	}

	public record S0(String key, Collection<Buffer> cb) {

	}

	interface WP_Path {
		// String (specified, resolved)
		// CP_Path
		// ET/EX whatever
		// File/Path

	}

	public void restTrigger(final Googly aWritePipeline) {
		final File aChosenDirectoryName = aWritePipeline.aChosen();

		aWritePipeline.onChosen(chosen -> {
			final @NotNull Iterable<Map.Entry<String, Collection<Buffer>>> mb1 = entries();

			aWritePipeline.googly_mkdirs(aChosenDirectoryName);

			// TODO flag?
			var wio = aWritePipeline.write_inputs(aChosenDirectoryName);
			if (wio.mode() != Mode.SUCCESS) assert false; // go pattern

			mb1.forEach((Map.Entry<String, Collection<Buffer>> entry) -> {
				final String             key   = entry.getKey();
				final Collection<Buffer> value = entry.getValue();
				final S0                 s0    = new S0(key, value);

				chosenDirectoryNamePromise.then(cdn -> {
					final Path path = FileSystems.getDefault().getPath(cdn, key);

					//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

					aWritePipeline.googly_mkdirs(path.getParent().toFile());

					// TODO functionality
					//System.out.println("201-fake Writing path: " + path);

					final EG_CompoundStatement seq = getSeq(s0);

					// s1 = s0 + seq + cdn

					aWritePipeline.googly_write(path, seq);
					aWritePipeline.googly_putSeq(key, path, seq);
				});
			});
		});
	}

	@NotNull
	private static EG_CompoundStatement getSeq(final S0 s0) {
		//noinspection ExtractMethodRecommender
		final EG_SingleStatement beginning   = new EG_SingleStatement("", EX_Explanation.withMessage("201 beginning find from other branch"));
		final EG_Statement       middle      = new GE_BuffersStatement(s0);
		final EG_SingleStatement ending      = new EG_SingleStatement("", EX_Explanation.withMessage("201 ending find from other branch"));
		final EX_Explanation     explanation = EX_Explanation.withMessage("write output file");

		final EG_CompoundStatement seq = new EG_CompoundStatement(beginning, ending, middle, false, explanation);
		return seq;
	}

	@NotNull
	private static String remove_initial_slash(final String s) {
		if (s.length() > 1 && s.charAt(0) == '/') {
			return s.substring(1);
		}
		return s;
	}
}
