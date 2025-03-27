package core.cache.misc.buffer

import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer

/**
 * The type Buffer output stream.
 */
class BufferOutputStream
/**
 * Instantiates a new Buffer output stream.
 *
 * @param buffer the buffer
 * @throws IOException       the io exception
 * @throws SecurityException the security exception
 */
(
    /**
     * Gets buffer.
     *
     * @return the buffer
     */
    val buffer: ByteBuffer,
) : OutputStream() {
    @Throws(IOException::class)
    override fun write(b: Int) {
        buffer.put(b.toByte())
    }

    override fun flush() {
    }

    override fun close() {
    }
}
