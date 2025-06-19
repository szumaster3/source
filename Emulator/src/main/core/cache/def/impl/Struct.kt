package core.cache.def.impl

import core.api.log
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.cache.misc.buffer.ByteBufferUtils
import core.tools.Log
import java.nio.ByteBuffer

/**
 * Represents a struct storing key-value pairs with mixed data types.
 */
class Struct(val id: Int) {
    val dataStore = mutableMapOf<Int, Any>()

    /**
     * Gets an integer value by key.
     *
     * @param key The key to lookup.
     * @return The integer value.
     */
    fun getInt(key: Int): Int = dataStore[key] as? Int ?: run {
        log(javaClass, Log.ERR, "Invalid value passed for key: [$key] struct: [$id]")
        -1
    }

    /**
     * Gets a string value by key.
     *
     * @param key The key to lookup.
     * @return The string value.
     */
    fun getString(key: Int): String? = dataStore[key] as? String

    override fun toString(): String = "Struct(id=$id, dataStore=$dataStore)"

    companion object {
        private val definitions = mutableMapOf<Int, Struct>()

        /**
         * Gets or loads the Struct for the given id.
         *
         * @param id The Struct id.
         * @return The struct.
         */
        @JvmStatic
        fun get(id: Int): Struct = definitions[id] ?: run {
            val data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, id)
            parse(id, data).also { definitions[id] = it }
        }

        /**
         * Parses raw byte data into a struct instance.
         *
         * @param id The Struct id.
         * @param data The raw byte data.
         * @return The parsed struct.
         */
        @JvmStatic
        fun parse(id: Int, data: ByteArray?): Struct {
            val struct = Struct(id)
            data?.let {
                val buffer = ByteBuffer.wrap(it)

                while (true) {
                    val opcode = buffer.get().toInt() and 0xFF
                    if (opcode == 0) break
                    if (opcode == 249) {
                        val size = buffer.get().toInt() and 0xFF
                        repeat(size) {
                            val isString = (buffer.get().toInt() and 0xFF) == 1
                            val key = ByteBufferUtils.getMedium(buffer)
                            val value: Any = if (isString) ByteBufferUtils.getString(buffer) else buffer.int
                            struct.dataStore[key] = value
                        }
                    }
                }
            }
            return struct
        }
    }
}
