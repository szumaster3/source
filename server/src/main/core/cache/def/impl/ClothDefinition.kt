package core.cache.def.impl

import core.ServerConstants
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.net.g1
import core.net.g2
import core.net.g2s
import java.nio.ByteBuffer

/**
 * Represents a cloth definition with model and color data.
 */
class ClothDefinition {
    var bodyPartId: Int = 0
    var bodyModelIds: IntArray? = null
    var notSelectable: Boolean = false
    var headModelIds: IntArray = intArrayOf(-1, -1, -1, -1, -1)
    var originalColors: IntArray? = null
    var modifiedColors: IntArray? = null
    var originalTextureColors: IntArray? = null
    var modifiedTextureColors: IntArray? = null

    companion object {
        /**
         * Loads the cloth definition for the id from cache.
         *
         * @param clothId The cloth id.
         * @return The loaded [ClothDefinition].
         */
        fun forId(clothId: Int): ClothDefinition {
            val def = ClothDefinition()
            val data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.IDK_TYPE, clothId)
            data?.let { def.load(ByteBuffer.wrap(it)) }
            return def
        }

        /**
         * Initializes the cache and loads all cloth definitions.
         *
         * @param args Command-line arguments (unused).
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                Cache.init(ServerConstants.CACHE_PATH)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            val length = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.IDK_TYPE)
            for (i in 0 until length) {
                forId(i)
            }
        }
    }

    /**
     * Loads cloth data from the buffer.
     *
     * @param buffer The data buffer.
     */
    fun load(buffer: ByteBuffer) {
        while (true) {
            when (val opcode = buffer.g1()) {
                0 -> return
                else -> decode(opcode, buffer)
            }
        }
    }

    /**
     * Parses an opcode and reads data.
     *
     * @param opcode The data opcode.
     * @param buffer The data buffer.
     */
    private fun decode(opcode: Int, buffer: ByteBuffer) {
        when (opcode) {
            1 -> bodyPartId = buffer.g1()
            2 -> {
                val length = buffer.g1()
                bodyModelIds = IntArray(length) { buffer.g2() }
            }
            3 -> notSelectable = true
            40 -> {
                val length = buffer.g1()
                originalColors = IntArray(length) { buffer.g2s() }
                modifiedColors = IntArray(length) { buffer.g2s() }
            }
            41 -> {
                val length = buffer.g1()
                originalTextureColors = IntArray(length) { buffer.g2s() }
                modifiedTextureColors = IntArray(length) { buffer.g2s() }
            }
            in 60..69 -> headModelIds[opcode - 60] = buffer.g2()
        }
    }
}
