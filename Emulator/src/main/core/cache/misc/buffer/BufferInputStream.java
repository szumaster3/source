package core.cache.misc.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * The type Buffer input stream.
 */
public final class BufferInputStream extends InputStream {

	private final ByteBuffer buffer;

    /**
     * Instantiates a new Buffer input stream.
     *
     * @param buffer the buffer
     * @throws IOException the io exception
     */
    public BufferInputStream(ByteBuffer buffer) throws IOException {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		return buffer.get();
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
