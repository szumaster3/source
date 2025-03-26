package core.cache.misc.buffer

import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * The type Buffer input stream.
 */
class BufferInputStream
/**
 * Instantiates a new Buffer input stream.
 *
 * @param buffer the buffer
 * @throws IOException the io exception
 */(
    /**
     * Gets buffer.
     *
     * @return the buffer
     */
    val buffer: ByteBuffer
) : InputStream() {
    @Throws(IOException::class)
    override fun read(): Int {
        return buffer.get().toInt()
    }
}
