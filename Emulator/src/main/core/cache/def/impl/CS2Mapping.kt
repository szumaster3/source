package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import core.cache.misc.buffer.ByteBufferUtils
import core.game.world.GameWorld
import java.io.BufferedWriter
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Represents a CS2 (Client Script 2) mapping for script id.
 *
 * @property scriptId The unique id of the script associated with this mapping.
 * @property unknown An unknown integer value associated with the mapping.
 * @property unknown1 Another unknown integer value associated with the mapping.
 * @property defaultString A default string value for the mapping.
 * @property defaultInt A default integer value for the mapping.
 * @property map A HashMap that associates an integer key with a value of type Any (either a String or an Integer).
 * @property array An array that holds the values in the same order as they appear in the map.
 */
class CS2Mapping private constructor(
    val scriptId: Int,
) {
    var unknown: Int = 0
    var unknown1: Int = 0
    var defaultString: String? = null
    var defaultInt: Int = 0
    var map: HashMap<Int, Any>? = null
    var array: Array<Any?>? = null

    companion object {
        private val maps = HashMap<Int, CS2Mapping>()

        /**
         * The main function that generates a report of all script mappings.
         *
         * @param args Command-line arguments (unused).
         */
        @JvmStatic
        fun main(args: Array<String>) {
            GameWorld.prompt(false)
            BufferedWriter(Files.newBufferedWriter(Paths.get("./cs2.txt"))).use { bw ->
                for (i in 0 until 10000) {
                    val mapping = forId(i) ?: continue
                    val mappingMap = mapping.map ?: continue
                    bw.append("ScriptAPI - $i [")
                    for ((index, value) in mappingMap) {
                        bw.append("$value: $index ")
                    }
                    bw.append("]")
                    bw.newLine()
                }
            }
        }

        /**
         * Retrieves the cs2 map for a given script id.
         *
         * @param scriptId The script id to retrieve the mapping for.
         * @return The corresponding [CS2Mapping], or `null` if the mapping could not be found or loaded.
         */
        @JvmStatic
        fun forId(scriptId: Int): CS2Mapping? {
            maps[scriptId]?.let { return it }
            val mapping = CS2Mapping(scriptId)
            val bs = Cache.getData(CacheIndex.CLIENT_SCRIPTS, scriptId ushr 8, scriptId and 0xFF) ?: return null
            mapping.load(ByteBuffer.wrap(bs))
            maps[scriptId] = mapping
            return mapping
        }
    }

    /**
     * Loads the mapping data from the provided byte buffer.
     *
     * @param buffer The ByteBuffer containing the mapping data to be loaded.
     */
    private fun load(buffer: ByteBuffer) {
        while (true) {
            when (val opcode = buffer.get().toInt() and 0xFF) {
                0 -> return
                1 -> unknown = buffer.get().toInt() and 0xFF
                2 -> unknown1 = buffer.get().toInt() and 0xFF
                3 -> defaultString = ByteBufferUtils.getString(buffer)
                4 -> defaultInt = buffer.int
                5, 6 -> {
                    val size = buffer.short.toInt() and 0xFFFF
                    map = HashMap(size)
                    array = arrayOfNulls(size)
                    for (i in 0 until size) {
                        val key = buffer.int
                        val value: Any = if (opcode == 5) ByteBufferUtils.getString(buffer) else buffer.int
                        array!![i] = value
                        map!![key] = value
                    }
                }
            }
        }
    }
}
