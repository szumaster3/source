package core.cache.def.impl

import core.api.getVarbit
import core.cache.Cache.getData
import core.cache.CacheIndex
import core.game.node.entity.player.Player
import java.nio.ByteBuffer

/**
 * Represents a varbit definition used for bit-wise variable storage.
 */
class VarbitDefinition {
    val id: Int
    var varpId: Int = 0
    var startBit: Int = 0
    var endBit: Int = 0
        private set

    constructor(id: Int) {
        this.id = id
    }

    constructor(varpId: Int, id: Int, startBit: Int, endBit: Int) {
        this.varpId = varpId
        this.id = id
        this.startBit = startBit
        this.endBit = endBit
    }

    /**
     * Gets the value of this varbit for the given [Player].
     *
     * @param player The player to check.
     * @return The Varbit value.
     */
    fun getValue(player: Player): Int = getVarbit(player, id)

    /**
     * The bit mask based on [startBit] and [endBit].
     */
    val mask: Int
        get() {
            var mask = 0
            for (i in startBit..endBit) mask = mask or (1 shl (i - startBit))
            return mask
        }

    override fun toString(): String = "ConfigFileDefinition [id=$id, configId=$varpId, bitShift=$startBit, bitSize=$endBit]"

    companion object {
        private val MAPPING: MutableMap<Int, VarbitDefinition> = HashMap()
        private val BITS = IntArray(32)

        init {
            var flag = 2
            for (i in 0..31) {
                BITS[i] = flag - 1
                flag += flag
            }
        }

        /**
         * Gets the varbit definition for a scenery id.
         *
         * @param id The scenery id.
         * @return The [VarbitDefinition].
         */
        @JvmStatic
        fun forSceneryId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the varbit definition for an NPC id.
         *
         * @param id The NPC id.
         * @return The [VarbitDefinition].
         */
        @JvmStatic
        fun forNpcId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the [VarbitDefinition] for an item id.
         *
         * @param id The item id.
         * @return The corresponding [VarbitDefinition].
         */
        @JvmStatic
        fun forItemId(id: Int): VarbitDefinition = forId(id)

        /**
         * Gets the [VarbitDefinition] for the given id.
         *
         * @param id The varbit id.
         * @return The [VarbitDefinition].
         */
        @JvmStatic
        fun forId(id: Int): VarbitDefinition {
            var def = MAPPING[id]
            if (def != null) {
                return def
            }

            def = VarbitDefinition(id)
            val bs = getData(CacheIndex.VAR_BIT, id ushr 10, id and 0x3ff)
            if (bs != null) {
                val buffer = ByteBuffer.wrap(bs)
                var opcode = 0
                while (((buffer.get().toInt() and 0xFF).also { opcode = it }) != 0) {
                    if (opcode == 1) {
                        def.varpId = buffer.getShort().toInt() and 0xFFFF
                        def.startBit = buffer.get().toInt() and 0xFF
                        def.endBit = buffer.get().toInt() and 0xFF
                    }
                }
            }
            MAPPING[id] = def
            return def
        }

        /**
         * Creates and registers a new [VarbitDefinition].
         *
         * @param varpId The Varp id.
         * @param varbitId The Varbit id.
         * @param startBit The start bit.
         * @param endBit The end bit.
         */
        @JvmStatic
        fun create(varpId: Int, varbitId: Int, startBit: Int, endBit: Int, ) {
            val def =
                VarbitDefinition(
                    varpId,
                    varbitId,
                    startBit,
                    endBit,
                )
            MAPPING[varbitId] = def
        }

        /**
         * Gets the Varbit definitions mapping.
         */
        val mapping: Map<Int, VarbitDefinition>
            get() = MAPPING
    }
}
