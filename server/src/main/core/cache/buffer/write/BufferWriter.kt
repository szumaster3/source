package core.cache.buffer.write

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

interface BufferWriter {

    fun encode(buffer: ByteBuffer)

    fun ByteBuffer.putString(s: String) {
        put(s.toByteArray(StandardCharsets.UTF_8))
        put(0.toByte())
    }

    companion object {
        @JvmStatic
        fun putObject(buffer: ByteBuffer, obj: BufferWriter?) {
            if (obj == null) {
                buffer.putInt(0)
                return
            }
            val tempBuffer = ByteBuffer.allocate(1024)
            obj.encode(tempBuffer)
            tempBuffer.flip()
            buffer.putInt(tempBuffer.remaining())
            buffer.put(tempBuffer)
        }
    }

}