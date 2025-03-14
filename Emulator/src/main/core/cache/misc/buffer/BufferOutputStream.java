package core.cache.misc.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * The type Buffer output stream.
 */
public final class BufferOutputStream extends OutputStream {

	private final ByteBuffer buffer;

    /**
     * Instantiates a new Buffer output stream.
     *
     * @param buffer the buffer
     * @throws IOException       the io exception
     * @throws SecurityException the security exception
     */
    public BufferOutputStream(ByteBuffer buffer) throws IOException, SecurityException {
		super();
		this.buffer = buffer;
	}

	@Override
	public void write(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void flush() {

	}

	@Override
	public void close() {

	}

    /**
     * Gets buffer.
     *
     * @return the buffer
     */
    public ByteBuffer getBuffer() {
		return buffer;
	}

}