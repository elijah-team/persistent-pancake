/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 */
package tripleo.elijah.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.comp.functionality.f203.F203;
import tripleo.elijah.lang.OS_Module;
import tripleo.elijah.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.elijah.nextgen.outputstatement.EG_SingleStatement;
import tripleo.elijah.nextgen.outputstatement.EG_Statement;
import tripleo.elijah.nextgen.outputstatement.EX_Explanation;
import tripleo.elijah.nextgen.outputstatement.GE_BuffersStatement;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.nextgen.outputtree.EOT_OutputTree;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.io.CharSink;
import tripleo.elijah.util.io.FileCharSink;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.DefaultBuffer;
import tripleo.util.buffer.TextBuffer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created 8/21/21 10:19 PM
 */
public class WritePipeline implements PipelineMember, AccessBus.AB_GenerateResultListener {
	private final Compilation c;
	private GenerateResult gr;

	final OutputStrategy os;
	final ElSystem sys;

	private final File file_prefix;

	public WritePipeline(@NotNull final AccessBus ab) {
		c = ab.getCompilation();

		file_prefix = new File("COMP", c.getCompilationNumberString());

		os = new OutputStrategy();
		os.per(OutputStrategy.Per.PER_CLASS); // TODO this needs to be configured per lsp

		sys = new ElSystem();
		sys.verbose = false; // TODO flag? ie CompilationOptions
		sys.setCompilation(c);
		sys.setOutputStrategy(os);

		ab.subscribe_GenerateResult(this);
	}

	@Override
	public void run() throws Exception {
		sys.generateOutputs(gr);

		write_files();
		// TODO flag?
		write_buffers();
	}

	public void write_files() throws IOException {
		final MB mb2 = new MB();

		for (final GenerateResultItem ab : gr.results()) {
			final String fileName = ab.output;

			mb2.put(fileName, ab.buffer);
			mb2.modmap_put(fileName, ab.node.module());
		}

		final List<EOT_OutputFile> leof = new ArrayList<>();

		for (final MBB entry : mb2.bz()) {
			final String             fileName = entry.getFileName();
			final Collection<Buffer> vs       = entry.getBuffers();

			final EOT_OutputFile eof = EOT_OutputFile.bufferSetToOutputFile(fileName, vs, c, entry.getModule());
			leof.add(eof);

			c.reports().addCodeOutput(()-> remove_initial_slash(fileName), eof);
		}

		c.getOutputTree().set(leof);

		final File fn1 = choose_dir_name();

		__rest(mb2.entries(), fn1, leof);
	}

	@NotNull
	private static String remove_initial_slash(final String s) {
		if (s.length() > 1 && s.charAt(0) == '/') {
			return s.substring(1);
		}
		return s;
	}

	private @NotNull File choose_dir_name() {
		final File fn00 = new F203(c.getErrSink(), c).chooseDirectory();
		final File fn01 = new File(fn00, "code");

		return fn01;
	}

	private void __rest(final @NotNull Iterable<Map.Entry<String, Collection<Buffer>>> mb,
	                    final @NotNull File aFile_prefix,
	                    final List<EOT_OutputFile> leof) throws IOException {
		aFile_prefix.mkdirs();
		final String prefix = aFile_prefix.toString();

		// TODO flag?
		write_inputs(aFile_prefix);

		for (final Map.Entry<String, Collection<Buffer>> entry : mb) {
			final String key  = entry.getKey();
			final Path   path = FileSystems.getDefault().getPath(prefix, key);
//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

			path.getParent().toFile().mkdirs();

			// TODO functionality
			System.out.println("201 Writing path: " + path);
			final CharSink x = c.getIO().openWrite(path);

			final EG_SingleStatement beginning = new EG_SingleStatement("", new EX_Explanation() {
			});
			final EG_Statement middle = new GE_BuffersStatement(entry);
			final EG_SingleStatement ending = new EG_SingleStatement("", new EX_Explanation() {
			});
			final EX_Explanation explanation = new EX_Explanation() {
				final String message = "write output file";
			};

			final EG_CompoundStatement seq = new EG_CompoundStatement(beginning, ending, middle, false, explanation);

//			for (final @NotNull Buffer buffer : entry.getValue()) {
//				x.accept(buffer.getText());
//			}
			x.accept(seq.getText());
			((FileCharSink) x).close();

			final @NotNull EOT_OutputTree cot = c.getOutputTree();
			cot._putSeq(key, path, seq);
		}
	}

	private void write_inputs(final File file_prefix) throws IOException {
		final DefaultBuffer buf = new DefaultBuffer("");
//			FileBackedBuffer buf = new FileBackedBuffer(fn1);
//			for (OS_Module module : modules) {
//				final String fn = module.getFileName();
//
//				append_hash(buf, fn);
//			}
//
//			for (CompilerInstructions ci : cis) {
//				final String fn = ci.getFilename();
//
//				append_hash(buf, fn);
//			}

		final List<File> recordedreads = c.getIO().recordedreads();
		final List<String> recordedread_filenames = recordedreads.stream()
				.map(File::toString)
				.collect(Collectors.toList());

		for (final @NotNull File file : recordedreads) {
			final String fn = file.toString();

			append_hash(buf, fn, c.getErrSink());
		}

		final File fn1 = new File(file_prefix, "inputs.txt");
		final String s = buf.getText();
		try (final Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn1, true)))) {
			w.write(s);
		}
	}

	private void append_hash(final TextBuffer aBuf, final String aFilename, final ErrSink errSink) throws IOException {
		@Nullable final String hh = Helpers.getHashForFilename(aFilename).success();
		if (hh != null) {
			aBuf.append(hh);
			aBuf.append(" ");
			aBuf.append_ln(aFilename);
		}
	}

	public void write_buffers() throws FileNotFoundException {
		file_prefix.mkdirs();

		final PrintStream db_stream = new PrintStream(new File(file_prefix, "buffers.txt"));
		PipelineLogic.debug_buffers(gr, db_stream);
	}

	@Override
	public void gr_slot(final GenerateResult gr) {
		this.gr = gr;
	}

	public Consumer<Supplier<GenerateResult>> consumer() {
		return new Consumer<Supplier<GenerateResult>>() {
			@Override
			public void accept(final Supplier<GenerateResult> aGenerateResultSupplier) {
//				if (grs != null) {
//					tripleo.elijah.util.Stupidity.println_err2("234 grs not null "+grs.getClass().getName());
//					return;
//				}
//
//				assert false;
//				grs = aGenerateResultSupplier;
//				//final GenerateResult gr = aGenerateResultSupplier.get();
				final int y = 2;
			}
		};
	}

	static class MBB {
		private final String fileName;
		private final Buffer buffer;
		private MB _up;

		public MBB(final String aFileName, final Buffer aBuffer) {
			fileName = aFileName;
			buffer   = aBuffer;
		}

		public String getFileName() {
			return fileName;
		}

		public Buffer getBuffer() {
			return buffer;
		}

		public Collection<Buffer> getBuffers() {
			return _up.getBuffers(this);
		}

		public void set_up(final MB a_up) {
			_up = a_up;
		}

		public OS_Module getModule() {
			return _up.getModule(this);
		}
	}

	static class MB {
		final Multimap<String, Buffer> mb = ArrayListMultimap.create();
		final Map<String, OS_Module> modmap = new HashMap<String, OS_Module>();
		private List<MBB> bz = new ArrayList<>();

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
	}

}

//
//
//
