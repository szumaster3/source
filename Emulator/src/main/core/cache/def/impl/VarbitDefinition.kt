package core.cache.def.impl

import core.api.getVarbit
import core.cache.Cache.getData
import core.cache.CacheIndex
import core.game.node.entity.player.Player
import java.nio.ByteBuffer

/**
 * The type Varbit definition.
 */
class VarbitDefinition {
    /**
     * Gets id.
     *
     * @return the id
     */
    val id: Int

    /**
     * Gets varp id.
     *
     * @return the varp id
     */
    var varpId: Int = 0

    /**
     * Gets start bit.
     *
     * @return the start bit
     */
    var startBit: Int = 0

    /**
     * Gets end bit.
     *
     * @return the end bit
     */
    var endBit: Int = 0
        private set

    /**
     * Instantiates a new Varbit definition.
     *
     * @param id the id
     */
    constructor(id: Int) {
        this.id = id
    }

    /**
     * Instantiates a new Varbit definition.
     *
     * @param varpId   the varp id
     * @param id       the id
     * @param startBit the start bit
     * @param endBit   the end bit
     */
    constructor(varpId: Int, id: Int, startBit: Int, endBit: Int) {
        this.varpId = varpId
        this.id = id
        this.startBit = startBit
        this.endBit = endBit
    }

    /**
     * Gets value.
     *
     * @param player the player
     * @return the value
     */
    fun getValue(player: Player): Int {
        return getVarbit(player, id)
    }

    val mask: Int
        /**
         * Gets mask.
         *
         * @return the mask
         */
        get() {
            var mask = 0
            for (i in startBit..endBit) mask = mask or (1 shl (i - startBit))
            return mask
        }

    override fun toString(): String {
        return "ConfigFileDefinition [id=$id, configId=$varpId, bitShift=$startBit, bitSize=$endBit]"
    }

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
         * For scenery id varbit definition.
         *
         * @param id the id
         * @return the varbit definition
         */
        @JvmStatic
        fun forSceneryId(id: Int): VarbitDefinition {
            return forId(id)
        }

        /**
         * For npc id varbit definition.
         *
         * @param id the id
         * @return the varbit definition
         */
        @JvmStatic
        fun forNpcId(id: Int): VarbitDefinition {
            return forId(id)
        }

        /**
         * For item id varbit definition.
         *
         * @param id the id
         * @return the varbit definition
         */
        @JvmStatic
        fun forItemId(id: Int): VarbitDefinition {
            return forId(id)
        }

        /**
         * For id varbit definition.
         *
         * @param id the id
         * @return the varbit definition
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
         * Create.
         *
         * @param varpId   the varp id
         * @param varbitId the varbit id
         * @param startBit the start bit
         * @param endBit   the end bit
         */
        @JvmStatic
        fun create(
            varpId: Int,
            varbitId: Int,
            startBit: Int,
            endBit: Int,
        ) {
            val def =
                VarbitDefinition(
                    varpId,
                    varbitId,
                    startBit,
                    endBit,
                )
            MAPPING[varbitId] = def
        }

        val mapping: Map<Int, VarbitDefinition>
            /**
             * Gets mapping.
             *
             * @return the mapping
             */
            get() = MAPPING
    }
}
