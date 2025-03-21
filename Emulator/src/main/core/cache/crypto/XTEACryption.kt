package core.cache.crypto

import java.nio.ByteBuffer

/**
 * The object `XTEACryption` provides encryption and decryption methods using the XTEA algorithm.
 * XTEA (extended Tiny Encryption Algorithm) is a block cipher that operates on 64-bit blocks and uses a 128-bit key.
 */
object XTEACryption {
    private const val DELTA: Int = -1640531527
    private const val SUM: Int = -957401312
    private const val NUM_ROUNDS: Int = 32
    private const val GOLDEN_RATIO = -0x61c88647

    /**
     * Deciphers the provided byte array using the XTEA decryption algorithm.
     *
     * @param buffer The byte array to be decrypted.
     * @param key The key used for decryption (should contain exactly 4 integers).
     * @param start The starting index in the buffer where the decryption should begin (default is 0).
     *
     * @throws IllegalArgumentException If the key array does not contain exactly 4 integers.
     */
    @JvmStatic
    fun decipher(
        buffer: ByteArray,
        key: IntArray,
        start: Int = 0,
    ) {
        if (key.size != 4) {
            throw IllegalArgumentException("Key must contain exactly 4 integers.")
        }

        val numQuads = buffer.size / 8
        for (i in 0 until numQuads) {
            var sum = GOLDEN_RATIO * NUM_ROUNDS
            var v0 = getInt(buffer, start + i * 8)
            var v1 = getInt(buffer, start + i * 8 + 4)
            for (j in 0 until NUM_ROUNDS) {
                v1 -= (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum.ushr(11) and 3]
                sum -= GOLDEN_RATIO
                v0 -= (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
            }
            putInt(buffer, start + i * 8, v0)
            putInt(buffer, start + i * 8 + 4, v1)
        }
    }

    /**
     * Encrypts the provided byte array using the XTEA encryption algorithm.
     *
     * @param buffer The byte array to be encrypted.
     * @param start The starting index in the buffer where the encryption should begin.
     * @param end The ending index (exclusive) where the encryption should stop.
     * @param key The key used for encryption (should contain exactly 4 integers).
     *
     * @throws IllegalArgumentException If the key array does not contain exactly 4 integers.
     */
    @JvmStatic
    fun encipher(
        buffer: ByteArray,
        start: Int,
        end: Int,
        key: IntArray,
    ) {
        if (key.size != 4) {
            throw IllegalArgumentException("Key must contain exactly 4 integers.")
        }

        val numQuads = (end - start) / 8
        for (i in 0 until numQuads) {
            var sum = 0
            var v0 = getInt(buffer, start + i * 8)
            var v1 = getInt(buffer, start + i * 8 + 4)
            for (j in 0 until NUM_ROUNDS) {
                v0 += (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
                sum += GOLDEN_RATIO
                v1 += (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum.ushr(11) and 3]
            }
            putInt(buffer, start + i * 8, v0)
            putInt(buffer, start + i * 8 + 4, v1)
        }
    }

    /**
     * Retrieves a 4-byte integer from the byte array at the specified index.
     *
     * @param buffer The byte array.
     * @param index The index in the byte array from where the integer is to be retrieved.
     *
     * @return The integer value from the byte array.
     */
    private fun getInt(
        buffer: ByteArray,
        index: Int,
    ) = (buffer[index].toInt() and 0xff shl 24) or (buffer[index + 1].toInt() and 0xff shl 16) or
            (buffer[index + 2].toInt() and 0xff shl 8) or
            (buffer[index + 3].toInt() and 0xff)

    /**
     * Puts a 4-byte integer into the byte array at the specified index.
     *
     * @param buffer The byte array.
     * @param index The index in the byte array where the integer should be placed.
     * @param value The integer value to be placed in the byte array.
     */
    private fun putInt(
        buffer: ByteArray,
        index: Int,
        value: Int,
    ) {
        buffer[index] = (value shr 24).toByte()
        buffer[index + 1] = (value shr 16).toByte()
        buffer[index + 2] = (value shr 8).toByte()
        buffer[index + 3] = value.toByte()
    }

    /**
     * Decrypts a ByteBuffer using the XTEA decryption algorithm.
     *
     * @param keys The decryption keys (should contain exactly 4 integers).
     * @param buffer The ByteBuffer to be decrypted.
     *
     * @return The decrypted ByteBuffer.
     */
    @JvmStatic
    fun decrypt(
        keys: IntArray,
        buffer: ByteBuffer,
    ): ByteBuffer = decrypt(keys, buffer, buffer.position(), buffer.limit())

    /**
     * Decrypts a ByteBuffer using the XTEA decryption algorithm.
     *
     * @param keys The decryption keys (should contain exactly 4 integers).
     * @param buffer The ByteBuffer to be decrypted.
     * @param offset The starting index where decryption should begin.
     * @param length The length of data to be decrypted.
     *
     * @return The decrypted ByteBuffer.
     */
    @JvmStatic
    fun decrypt(
        keys: IntArray,
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
    ): ByteBuffer {
        val numBlocks = (length - offset) / 8
        val block = IntArray(2)
        for (i in 0 until numBlocks) {
            val index = i * 8 + offset
            block[0] = buffer.getInt(index)
            block[1] = buffer.getInt(index + 4)
            decipher(keys, block)
            buffer.putInt(index, block[0])
            buffer.putInt(index + 4, block[1])
        }
        return buffer
    }

    /**
     * Deciphers a block of data using the XTEA decryption algorithm.
     *
     * @param keys The decryption keys (should contain exactly 4 integers).
     * @param block The block of data to be decrypted.
     */
    private fun decipher(
        keys: IntArray,
        block: IntArray,
    ) {
        var sum: Long = SUM.toLong()
        for (i in 0 until NUM_ROUNDS) {
            block[1] -=
                (
                        keys[((sum and 0x1933L) ushr 11).toInt()] + sum xor
                                (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()
                        ).toInt()
            sum -= DELTA.toLong()
            block[0] -=
                (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
        }
    }

    /**
     * Encrypts a ByteBuffer using the XTEA encryption algorithm.
     *
     * @param keys The encryption keys (should contain exactly 4 integers).
     * @param buffer The ByteBuffer to be encrypted.
     */
    @JvmStatic
    fun encrypt(
        keys: IntArray,
        buffer: ByteBuffer,
    ) {
        encrypt(keys, buffer, buffer.position(), buffer.limit())
    }

    /**
     * Encrypts a ByteBuffer using the XTEA encryption algorithm.
     *
     * @param keys The encryption keys (should contain exactly 4 integers).
     * @param buffer The ByteBuffer to be encrypted.
     * @param offset The starting index where encryption should begin.
     * @param length The length of data to be encrypted.
     */
    @JvmStatic
    fun encrypt(
        keys: IntArray,
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
    ) {
        val numBlocks = (length - offset) / 8
        val block = IntArray(2)
        for (i in 0 until numBlocks) {
            val index = i * 8 + offset
            block[0] = buffer.getInt(index)
            block[1] = buffer.getInt(index + 4)
            encipher(keys, block)
            buffer.putInt(index, block[0])
            buffer.putInt(index + 4, block[1])
        }
    }

    /**
     * Encrypts a block of data using the XTEA encryption algorithm.
     *
     * @param keys The encryption keys (should contain exactly 4 integers).
     * @param block The block of data to be encrypted.
     */
    private fun encipher(
        keys: IntArray,
        block: IntArray,
    ) {
        var sum: Long = 0
        for (i in 0 until NUM_ROUNDS) {
            block[0] +=
                (((block[1] shl 4 xor (block[1] ushr 5)) + block[1]).toLong() xor keys[(sum and 0x3L).toInt()] + sum).toInt()
            sum += DELTA.toLong()
            block[1] +=
                (
                        keys[((sum and 0x1933L) ushr 11).toInt()] + sum xor
                                (block[0] + (block[0] shl 4 xor (block[0] ushr 5))).toLong()
                        ).toInt()
        }
    }
}
