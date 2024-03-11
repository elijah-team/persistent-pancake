package tripleo.eljiah_pancake_durable.comp;

import tripleo.elijah.util.io.CharSink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface IO {
	CharSink openWrite(Path aPath) throws IOException;

	InputStream readFile(File aFile) throws FileNotFoundException;

	boolean recordedRead(File aFile);

	List<File> recordedreads();
}
