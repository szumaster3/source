package core.cache.def.impl

import core.ServerConstants
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import java.nio.ByteBuffer

/**
 * Represents a cloth definition.
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
         * Retrieves a [ClothDefinition] for the given cloth id by loading its data from the cache.
         *
         * @param clothId The id of the cloth.
         * @return The [ClothDefinition] containing the data for the given id.
         */
        @JvmStatic
        fun forId(clothId: Int): ClothDefinition {
            val def = ClothDefinition()
            val data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.PLAYER_KIT, clothId)
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
            val length = Cache.getArchiveCapacity(CacheIndex.CONFIGURATION, CacheArchive.PLAYER_KIT)
            for (i in 0 until length) {
                forId(i)
            }
        }
    }

    /**
     * Loads the cloth definition data from the provided [ByteBuffer].
     *
     * @param buffer The [ByteBuffer] containing the cloth data.
     */
    fun load(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) break
            parse(opcode, buffer)
        }
    }

    /**
     * Parses an opcode and reads the corresponding data from the buffer.
     *
     * @param opcode The opcode indicating the type of data to read.
     * @param buffer The [ByteBuffer] containing the data.
     */
    private fun parse(
        opcode: Int,
        buffer: ByteBuffer,
    ) {
        when (opcode) {
            1 -> bodyPartId = buffer.get().toInt() and 0xFF
            2 -> {
                val length = buffer.get().toInt() and 0xFF
                bodyModelIds = IntArray(length) { buffer.short.toInt() and 0xFFFF }
            }

            3 -> notSelectable = true
            40 -> {
                val length = buffer.get().toInt() and 0xFF
                originalColors = IntArray(length) { buffer.short.toInt() }
                modifiedColors = IntArray(length) { buffer.short.toInt() }
            }

            41 -> {
                val length = buffer.get().toInt() and 0xFF
                originalTextureColors = IntArray(length) { buffer.short.toInt() }
                modifiedTextureColors = IntArray(length) { buffer.short.toInt() }
            }

            in 60..69 -> headModelIds[opcode - 60] = buffer.short.toInt() and 0xFFFF
        }
    }
}
