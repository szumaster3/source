package core.cache.def.impl

import core.api.log
import core.cache.Cache
import core.cache.CacheIndex
import core.net.g1
import core.net.g2
import core.net.g4
import core.net.gjstr
import core.tools.CP1252
import core.tools.Log
import java.nio.ByteBuffer

/**
 * Represents a DataMap storing key-value pairs with typed keys and values.
 *
 * @property id The unique identifier of the DataMap.
 */
class DataMap private constructor(val id: Int) {
    var keyType: Char = '?'
    var valueType: Char = '?'
    var defaultString: String? = null
    var defaultInt: Int = 0
    val dataStore = HashMap<Int, Any>()

    /**
     * Gets an integer value for the specified key.
     *
     * @param key The key to look up.
     * @return The integer value, or -1 if not found or invalid type.
     */
    fun getInt(key: Int): Int = (dataStore[key] as? Int) ?: run {
        log(javaClass, Log.ERR, "Invalid value passed for key: [$key] map: [$id]")
        -1
    }

    /**
     * Gets a string value for the specified key.
     *
     * @param key The key to look up.
     * @return The string value, or null if not found or invalid type.
     */
    fun getString(key: Int): String? = dataStore[key] as? String

    override fun toString(): String = "DataMapDefinition(id=$id, keyType=$keyType, valueType=${
        when (valueType) {
            'K' -> "Normal"
            'J' -> "Struct Pointer"
            else -> "Unknown"
        }
    }, defaultString=$defaultString, defaultInt=$defaultInt, dataStore=$dataStore)"

    companion object {
        private val definitions = HashMap<Int, DataMap>()

        /**
         * Gets the [DataMap] instance for the given id.
         * Loads and parses it from cache if not already loaded.
         *
         * @param id The DataMap id.
         * @return The [DataMap] instance.
         */
        @JvmStatic
        fun get(id: Int): DataMap = definitions[id] ?: run {
            val data = Cache.getData(CacheIndex.ENUM_CONFIGURATION, id ushr 8, id and 0xFF)
            val def = decode(id, data)
            definitions[id] = def
            def
        }

        /**
         * Parses raw byte data into a [DataMap] instance.
         *
         * @param id The DataMap id.
         * @param data The raw data bytes.
         * @return The parsed [DataMap].
         */
        @JvmStatic
        fun decode(id: Int, data: ByteArray?): DataMap {
            val def = DataMap(id)
            if (data != null) {
                val buffer = ByteBuffer.wrap(data)
                while (buffer.remaining() > 0) {
                    val opcode = try {
                        buffer.g1()
                    } catch (e: IndexOutOfBoundsException) {
                        log(DataMap::class.java, Log.ERR, "Error processing data: [${e.message}]")
                        break
                    }

                    try {
                        when (opcode) {
                            1 -> def.keyType = CP1252.getFromByte(buffer.get())
                            2 -> def.valueType = CP1252.getFromByte(buffer.get())
                            3 -> def.defaultString = buffer.gjstr()
                            4 -> def.defaultInt = buffer.g4()
                            5, 6 -> {
                                val size = buffer.g2()
                                repeat(size) {
                                    val key = buffer.g4()
                                    val value: Any = if (opcode == 5) buffer.gjstr() else buffer.g4()
                                    def.dataStore[key] = value
                                }
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        log(DataMap::class.java, Log.ERR, "Error processing opcode [$opcode]: [${e.message}]")
                    }
                }
            }
            return def
        }
    }
}
