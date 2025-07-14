package core.cache.misc.buffer

import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * Buffer input stream.
 */
class BufferInputStream(val buffer: ByteBuffer) : InputStream() {
    @Throws(IOException::class)
    override fun read(): Int = buffer.get().toInt()
}
