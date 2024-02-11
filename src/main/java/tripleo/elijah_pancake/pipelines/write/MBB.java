package tripleo.elijah_pancake.pipelines.write;

import tripleo.elijah.lang.OS_Module;
import tripleo.util.buffer.Buffer;

import java.util.Collection;

public class MBB {
	private final String fileName;
	private final Buffer buffer;
	private       MB     _up;

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
