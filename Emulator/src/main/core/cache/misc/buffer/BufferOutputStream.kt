package core.cache.misc.buffer

import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer

/**
 * Buffer output stream.
 */
class BufferOutputStream(val buffer: ByteBuffer, ) : OutputStream() {
    @Throws(IOException::class)
    override fun write(b: Int) {
        buffer.put(b.toByte())
    }

    override fun flush() {
    }

    override fun close() {
    }
}
