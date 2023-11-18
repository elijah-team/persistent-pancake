/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.comp;

import org.jetbrains.annotations.*;
import tripleo.elijah.nextgen.inputtree.*;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.Operation;
import tripleo.elijah.util.io.CharSource;
import tripleo.elijah.util.io.DisposableCharSink;
import tripleo.elijah.util.io.FileCharSink;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class IO {

	// exists, delete, isType ....

	public final  List<File>         recordedwrites = new ArrayList<>();
	private final List<_IO_ReadFile> recordedreads  = new ArrayList<>();

	public @Nullable CharSource openRead(final @NotNull Path p) {
		record(FileOption.READ, p);
		return null;
	}

	public @NotNull DisposableCharSink openWrite(final @NotNull Path p) throws IOException {
		record(FileOption.WRITE, p);
		return new FileCharSink(Files.newOutputStream(p));
	}

	public @NotNull InputStream readFile(final @NotNull File f) throws FileNotFoundException {
		record(FileOption.READ, f);
		return new FileInputStream(f);
	}

	public _IO_ReadFile readFile2(final @NotNull File f) throws FileNotFoundException {
		final _IO_ReadFile readFile = new _IO_ReadFile(f);

		record(readFile);

		FileInputStream inputStream = new FileInputStream(f);
		readFile.setInputStream(inputStream);

		return readFile;
	}

	private void record(@NotNull final FileOption read, @NotNull final File file) {
		switch (read) {
		case WRITE:
			recordedwrites.add(file);
			break;
		case READ:
			record(new _IO_ReadFile(file));
			break;
		default:
			throw new IllegalStateException("Cant be here");
		}
	}

	private void record(@NotNull final _IO_ReadFile aReadFile) {
		recordedreads.add((aReadFile));
	}

	private void record(final @NotNull FileOption read, @NotNull final Path p) {
		record(read, p.toFile());
	}

	public boolean recordedRead(final File file) {
		return recordedreads.stream().anyMatch(x-> Objects.equals(x.getFile().getAbsolutePath(), file.getAbsolutePath()));
	}

	public boolean recordedWrite(final File file) {
		return recordedwrites.contains(file);
	}

	public List<_IO_ReadFile> recordedreads_io() {
		return recordedreads;
	}

	public List<File> recordedreads() {
		return recordedreads.stream()
		  .map(r -> r.file)
		  .collect(Collectors.toList());
	}

	public static class _IO_ReadFile {

		private final File file;
		private FileInputStream inputStream;

		public _IO_ReadFile(File aFile) {
			file = aFile;
		}

		@Override
		public String toString() {
			return "_IO_ReadFile{" +
					"file=" + file +
					'}';
		}

		public File getFile() {
			return file;
		}

		public String getFileName() {
			return file.toString();
		}

		public Operation<String> hash() {
			final @NotNull Operation<String> hh2 = Helpers.getHashForFilename(getFileName());
			return hh2;
		}

		public EIT_SourceOrigin getSourceOrigin() {
			final String           fn = getFileName();
			final EIT_SourceOrigin x;

			if (fn.equals("lib_elijjah/lib-c/Prelude.elijjah")) {
				x = EIT_SourceOrigin.PREL;
			} else if (fn.startsWith("lib_elijjah/")) {
				x = EIT_SourceOrigin.LIB;
			} else if (fn.startsWith("test/")) {
				x = EIT_SourceOrigin.SRC;
			} else {
				throw new IllegalStateException("Error"); // Operation??
			}

			return x;
		}

		public void setInputStream(FileInputStream aInputStream) {

			inputStream = aInputStream;
		}

		public FileInputStream getInputStream() {
			return inputStream;
		}

		public String getLongPath1() throws IOException {
			return getFile().getCanonicalPath();
		}
	}
}

//
//
//
