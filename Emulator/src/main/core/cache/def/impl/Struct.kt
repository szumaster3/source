package core.cache.def.impl

import core.api.log
import core.cache.Cache
import core.cache.CacheArchive
import core.cache.CacheIndex
import core.cache.misc.buffer.ByteBufferUtils
import core.tools.Log
import java.nio.ByteBuffer

/**
 * Represents a Struct, which is a container for key-value pairs with various data types.
 *
 * @property id The id for struct.
 * @property dataStore A mutable map holding the key-value pairs of the struct.
 */
class Struct(val id: Int) {
    val dataStore = mutableMapOf<Int, Any>()

    /**
     * Gets an integer value associated with a given key from the Struct.
     *
     * @param key The key to look up in the `dataStore`.
     * @return The integer value associated with the key, or -1 if the value is not valid.
     */
    fun getInt(key: Int): Int {
        return dataStore[key] as? Int ?: run {
            log(javaClass, Log.ERR, "Invalid value passed for key: [$key] struct: [$id]")
            -1
        }
    }

    /**
     * Gets a string value associated with a given key from the Struct.
     *
     * @param key The key to look up in the `dataStore`.
     * @return The string value associated with the key, or null if the value is not a string.
     */
    fun getString(key: Int): String? {
        return dataStore[key] as? String
    }

    /**
     * Provides a string representation of the Struct, including its id and the stored key-value pairs.
     *
     * @return A string describing the Struct.
     */
    override fun toString(): String {
        return "Struct(id=$id, dataStore=$dataStore)"
    }

    companion object {
        private val definitions = mutableMapOf<Int, Struct>()

        /**
         * Retrieves the Struct for a given id.
         *
         * @param id The id of the Struct to retrieve.
         * @return The Struct associated with the given id.
         */
        @JvmStatic
        fun get(id: Int): Struct {
            return definitions[id] ?: run {
                val data = Cache.getData(CacheIndex.CONFIGURATION, CacheArchive.STRUCT_TYPE, id)
                parse(id, data).also { definitions[id] = it }
            }
        }

        /**
         * Parses a byte array of data to create and initialize a Struct.
         *
         * @param id The id of the Struct to be created.
         * @param data The byte array containing the data to parse.
         * @return The fully initialized Struct instance.
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
