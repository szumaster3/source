package core.cache.buffer.read

import core.cache.buffer.write.BufferWriter
import java.nio.ByteBuffer

interface BufferReader<T> {
    /**
     * Method to decode a ByteBuffer.
     */
    fun decode(buffer: ByteBuffer): T

    companion object {
        /**
         * Read an object from a buffer.
         */
        inline fun <reified T> getObject(buffer: ByteBuffer, reader: BufferReader<T>): T? {
            val length = buffer.int
            if (length <= 0) return null

            val slice = buffer.slice()
            slice.limit(length)

            buffer.position(buffer.position() + length)

            return reader.decode(slice)
        }

        @JvmStatic
        fun getMedium(buffer: ByteBuffer): Int =
            ((buffer.get().toInt() and 0xFF) shl 16) + ((buffer.get().toInt() and 0xFF) shl 8) + (buffer.get()
                .toInt() and 0xFF)

        @JvmStatic
        fun getSmart(buffer: ByteBuffer): Int {
            val peek = buffer.get().toInt() and 0xFF
            return if (peek <= Byte.MAX_VALUE.toInt()) {
                peek
            } else {
                ((peek shl 8) or (buffer.get().toInt() and 0xFF)) - 32768
            }
        }

        @JvmStatic
        fun getBigSmart(buffer: ByteBuffer): Int {
            var value = 0
            var current = getSmart(buffer)
            while (current == 32767) {
                current = getSmart(buffer)
                value += 32767
            }
            value += current
            return value
        }
    }
}