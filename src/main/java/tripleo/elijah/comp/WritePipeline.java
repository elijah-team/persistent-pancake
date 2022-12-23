/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tripleo.elijah.nextgen.inputtree.EIT_Input;
import tripleo.elijah.nextgen.inputtree.EIT_ModuleInput;
import tripleo.elijah.nextgen.outputstatement.EG_CompoundStatement;
import tripleo.elijah.nextgen.outputtree.EOT_OutputFile;
import tripleo.elijah.nextgen.outputtree.EOT_OutputType;
import tripleo.elijah.stages.gen_generic.GenerateResult;
import tripleo.elijah.stages.gen_generic.GenerateResultItem;
import tripleo.elijah.stages.generate.ElSystem;
import tripleo.elijah.stages.generate.OutputStrategy;
import tripleo.elijah.util.Helpers;
import tripleo.util.buffer.Buffer;
import tripleo.util.buffer.DefaultBuffer;
import tripleo.util.buffer.TextBuffer;
import tripleo.util.io.CharSink;
import tripleo.util.io.FileCharSink;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;
import static tripleo.elijah.util.Helpers.List_of;

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
		final Multimap<String, Buffer> mb = ArrayListMultimap.create();

		for (final GenerateResultItem ab : gr.results()) {
			mb.put(ab.output, ab.buffer);
		}

		final List<EOT_OutputFile> leof = new ArrayList<>();
		for (final GenerateResultItem ab : gr.results()) {
			final List<EIT_Input> inputs = List_of(new EIT_ModuleInput(ab.node.module(), c));
			final EG_CompoundStatement seq = new EG_CompoundStatement();
			final EOT_OutputFile eof = new EOT_OutputFile(c, inputs, ab.output, EOT_OutputType.SOURCES, seq);
			leof.add(eof);
		}

		final File fn1 = choose_dir_name();

		__rest(mb, fn1); //file_prefix);
	}

	private void __rest(final Multimap<String, Buffer> mb, final File aFile_prefix) throws IOException {
		aFile_prefix.mkdirs();
		final String prefix = aFile_prefix.toString();

		// TODO flag?
		write_inputs(aFile_prefix);

		for (final Map.Entry<String, Collection<Buffer>> entry : mb.asMap().entrySet()) {
			final String key = entry.getKey();
			final Path path = FileSystems.getDefault().getPath(prefix, key);
//			BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

			path.getParent().toFile().mkdirs();

			// TODO functionality
			System.out.println("201 Writing path: "+path);
			final CharSink x = c.getIO().openWrite(path);
			for (final Buffer buffer : entry.getValue()) {
				x.accept(buffer.getText());
			}
			((FileCharSink)x).close();
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

		final List<File> recordedreads = c.getIO().recordedreads;
		final List<String> recordedread_filenames = recordedreads.stream()
				.map(file -> file.toString())
				.collect(Collectors.toList());

		for (final File file : recordedreads) {
			final String fn = file.toString();

			append_hash(buf, fn, c.getErrSink());
		}

		final File fn1 = new File(file_prefix, "inputs.txt");
		final String s = buf.getText();
		try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn1, true)))) {
			w.write(s);
		}
	}

	private File choose_dir_name() {
		final List<File> recordedreads = c.getIO().recordedreads;
		final List<String> recordedread_filenames = recordedreads.stream()
				.map(File::toString)
				.collect(Collectors.toList());

//		for (final File file : recordedreads) {
//			final String fn = file.toString();
//
//			append_hash(buf, fn, c.getErrSink());
//		}

		// TODO can't use stream because of exception
//		recordedread_filenames
//				.forEach(fn -> append_hash(buf, fn, c.getErrSink()));

		final DigestUtils digestUtils = new DigestUtils(SHA_256);

		final StringBuilder sb1 = new StringBuilder();

//		Map<String, Integer> unSortedMap = getUnSortedMap();
//		// LinkedHashMap preserves the ordering of elements in which they are inserted
//
//		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
//		unSortedMap.entrySet()
//				.stream()
//				.sorted(Map.Entry.comparingByKey())
//				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		recordedread_filenames
				.stream()
				.sorted().map(digestUtils::digestAsHex)
				.forEach(sha256 -> sb1.append(sha256));

//		final byte[] c_name0 = digestUtils.digest(sb1.toString());
//		final String c_name = Base36.toBase36(c_name0);
		final String c_name = digestUtils.digestAsHex(sb1.toString());

		//

		final LocalDateTime instance = LocalDateTime.now();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh.mm.ss");

		final String date = formatter.format(instance); //15-02-2022 12:43

		final File fn00 = new File("COMP", c_name);
		final File fn0 = new File(fn00, date);
		fn0.mkdirs();

		final String fn1 = new File(fn0, "inputs.txt").toString();
//		final String fn1 = new File(file_prefix, "inputs.txt").toString();
		return fn0;
	}

	private void append_hash(final TextBuffer aBuf, final String aFilename, final ErrSink errSink) throws IOException {
		@Nullable final String hh = Helpers.getHashForFilename(aFilename, errSink);
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
}

//
//
//
