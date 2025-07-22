package core.cache.def.impl

import core.cache.Cache
import core.cache.CacheIndex
import core.game.world.GameWorld
import core.net.g1
import core.net.g2
import core.net.g4
import core.net.gjstr
import java.io.BufferedWriter
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Represents a mapping for a CS2 script.
 */
class CS2Mapping private constructor(val scriptId: Int) {
    var unknown: Int = 0
    var unknown1: Int = 0
    var defaultString: String? = null
    var defaultInt: Int = 0
    var map: HashMap<Int, Any>? = null
    var array: Array<Any?>? = null

    companion object {
        private val maps = HashMap<Int, CS2Mapping>()

        /**
         * Generates a report of all script mappings.
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
         * Gets the [CS2Mapping] for the given script id.
         *
         * @param scriptId The script id.
         * @return The mapping.
         */
        fun forId(scriptId: Int): CS2Mapping? {
            maps[scriptId]?.let { return it }
            val mapping = CS2Mapping(scriptId)
            val bs = Cache.getData(CacheIndex.ENUM_CONFIGURATION, scriptId ushr 8, scriptId and 0xFF) ?: return null
            mapping.load(ByteBuffer.wrap(bs))
            maps[scriptId] = mapping
            return mapping
        }
    }

    /**
     * Loads mapping data from the byte buffer.
     */
    private fun load(buffer: ByteBuffer) {
        while (true) {
            when (val opcode = buffer.g1()) {
                0 -> return
                1 -> unknown = buffer.g1()
                2 -> unknown1 = buffer.g1()
                3 -> defaultString = buffer.gjstr()
                4 -> defaultInt = buffer.g4()
                5, 6 -> {
                    val size = buffer.g2()
                    map = HashMap(size)
                    array = arrayOfNulls(size)
                    for (i in 0 until size) {
                        val key = buffer.g4()
                        val value: Any = if (opcode == 5) buffer.gjstr() else buffer.g4()
                        array!![i] = value
                        map!![key] = value
                    }
                }
            }
        }
    }
}
