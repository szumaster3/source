package core.cache.def.impl

import core.api.getVarp
import core.cache.Cache.getData
import core.cache.CacheIndex
import core.cache.def.Definition
import core.cache.misc.buffer.ByteBufferUtils.getMedium
import core.cache.misc.buffer.ByteBufferUtils.getString
import core.game.interaction.OptionHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.drop.NPCDropTables
import core.game.node.entity.player.Player
import core.game.system.config.NPCConfigParser
import core.game.world.GameWorld.prompt
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.StringUtils.isPlusN
import java.nio.ByteBuffer

/**
 * The type Npc definition.
 */
class NPCDefinition(
    id: Int,
) : Definition<NPC?>() {
    @JvmField var size: Int = 1

    @JvmField var combatLevel: Int

    @JvmField var headIcons: Int

    @JvmField var isVisibleOnMap: Boolean

    @JvmField var dropTables: NPCDropTables = NPCDropTables(this)

    @JvmField var anInt833: Int

    @JvmField var anInt836: Int

    @JvmField var anInt837: Int

    @JvmField var aBoolean841: Boolean

    @JvmField var anInt842: Int

    @JvmField var configFileId: Int

    @JvmField var childNPCIds: IntArray? = null

    @JvmField var anInt846: Int

    @JvmField var anInt850: Int

    @JvmField var aByte851: Byte

    @JvmField var aBoolean852: Boolean

    @JvmField var anInt853: Int

    @JvmField var aByte854: Byte

    @JvmField var aBoolean856: Boolean

    @JvmField var aBoolean857: Boolean

    @JvmField var aShortArray859: ShortArray? = null

    @JvmField var aByteArray861: ByteArray? = null

    @JvmField var aShort862: Short

    @JvmField var aBoolean863: Boolean

    @JvmField var anInt864: Int

    @JvmField var aShortArray866: ShortArray? = null

    @JvmField var anIntArray868: IntArray

    @JvmField var anInt869: Int

    @JvmField var anInt870: Int

    @JvmField var anInt871: Int

    @JvmField var anInt872: Int

    @JvmField var anInt874: Int

    @JvmField var anInt875: Int

    @JvmField var anInt876: Int

    @JvmField var anInt879: Int

    @JvmField var aShortArray880: ShortArray? = null

    @JvmField var anInt884: Int

    @JvmField var configId: Int

    @JvmField var anInt889: Int

    @JvmField var anIntArray892: IntArray? = null

    @JvmField var aShort894: Short

    @JvmField var aShortArray896: ShortArray? = null

    @JvmField var anInt897: Int

    @JvmField var anInt899: Int

    @JvmField var anInt901: Int

    @JvmField var standAnimation: Int

    @JvmField var walkAnimation: Int

    @JvmField var renderAnimationId: Int = 0

    @JvmField var combatDistance: Int = 0

    @JvmField var combatGraphics: Array<Graphics?> = arrayOfNulls(3)

    @JvmField var turnAnimation: Int = 0

    @JvmField var turn180Animation: Int = 0

    @JvmField var turnCWAnimation: Int = 0

    @JvmField var turnCCWAnimation: Int = 0

    init {
        this.id = id
        anInt842 = -1
        configFileId = -1
        anInt837 = -1
        anInt846 = -1
        anInt853 = 32
        standAnimation = -1
        walkAnimation = -1
        combatLevel = 0
        anInt836 = -1
        name = "null"
        anInt869 = 0
        anInt850 = 255
        anInt871 = -1
        aBoolean852 = true
        aShort862 = 0
        anInt876 = -1
        aByte851 = -96
        anInt875 = 0
        anInt872 = -1
        aBoolean857 = true
        anInt870 = -1
        anInt874 = -1
        anInt833 = -1
        anInt864 = 128
        headIcons = -1
        aBoolean856 = false
        configId = -1
        aByte854 = -16
        aBoolean863 = false
        isVisibleOnMap = true
        anInt889 = -1
        anInt884 = -1
        aBoolean841 = true
        anInt879 = -1
        anInt899 = 128
        aShort894 = 0
        options = arrayOfNulls(5)
        anInt897 = 0
        anInt901 = -1
        anIntArray868 = IntArray(0)
    }

    /**
     * Gets child npc.
     *
     * @param player the player
     * @return the child npc
     */
    fun getChildNPC(player: Player?): NPCDefinition {
        if (childNPCIds == null || childNPCIds!!.size < 1) {
            return this
        }
        var configValue = -1
        if (player != null) {
            if (configFileId != -1) {
                configValue = VarbitDefinition.forNpcId(configFileId).getValue(player)
            } else if (configId != -1) {
                configValue = getVarp(player, configId)
            }
        } else {
            configValue = 0
        }
        if (configValue < 0 || configValue >= childNPCIds!!.size - 1 || childNPCIds!![configValue] == -1) {
            val objectId = childNPCIds!![childNPCIds!!.size - 1]
            if (objectId != -1) {
                return forId(objectId)
            }
            return this
        }
        return forId(childNPCIds!![configValue])
    }

    private fun parse(buffer: ByteBuffer) {
        while (true) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode == 0) {
                break
            }
            parseOpcode(buffer, opcode)
        }
    }

    private fun parseOpcode(
        buffer: ByteBuffer,
        opcode: Int,
    ) {
        when (opcode) {
            1 -> {
                val length = buffer.get().toInt() and 0xFF
                anIntArray868 = IntArray(length)
                var i_66_ = 0
                while (i_66_ < length) {
                    anIntArray868[i_66_] = buffer.getShort().toInt() and 0xFFFF
                    if ((anIntArray868[i_66_] xor -0x1) == -65536) anIntArray868[i_66_] = -1
                    i_66_++
                }
            }

            2 -> name = getString(buffer)
            12 -> size = buffer.get().toInt() and 0xFF
            13 -> standAnimation = buffer.getShort().toInt()
            14 -> walkAnimation = buffer.getShort().toInt()
            15 -> turnAnimation = buffer.getShort().toInt()
            16 -> buffer.getShort() // Another turn animation
            17 -> {
                walkAnimation = buffer.getShort().toInt()
                turn180Animation = buffer.getShort().toInt()
                turnCWAnimation = buffer.getShort().toInt()
                turnCCWAnimation = buffer.getShort().toInt()
            }

            30, 31, 32, 33, 34 -> options[opcode - 30] = getString(buffer)
            40 -> {
                var length = buffer.get().toInt() and 0xFF
                aShortArray859 = ShortArray(length)
                aShortArray896 = ShortArray(length)
                var i_65_ = 0
                while ((length xor -0x1) < (i_65_ xor -0x1)) {
                    aShortArray896!![i_65_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    aShortArray859!![i_65_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    i_65_++
                }
            }

            41 -> {
                var length = buffer.get().toInt() and 0xFF
                aShortArray880 = ShortArray(length)
                aShortArray866 = ShortArray(length)
                var i_54_ = 0
                while ((i_54_ xor -0x1) > (length xor -0x1)) {
                    aShortArray880!![i_54_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    aShortArray866!![i_54_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    i_54_++
                }
            }

            42 -> {
                var length = buffer.get().toInt() and 0xFF
                aByteArray861 = ByteArray(length)
                var i_55_ = 0
                while (length > i_55_) {
                    aByteArray861!![i_55_] = buffer.get()
                    i_55_++
                }
            }

            60 -> {
                var length = buffer.get().toInt() and 0xFF
                anIntArray892 = IntArray(length)
                var i_64_ = 0
                while ((i_64_ xor -0x1) > (length xor -0x1)) {
                    anIntArray892!![i_64_] = buffer.getShort().toInt() and 0xFFFF
                    i_64_++
                }
            }

            93 -> isVisibleOnMap = false
            95 -> combatLevel = buffer.getShort().toInt() and 0xFFFF
            97 -> anInt864 = buffer.getShort().toInt() and 0xFFFF
            98 -> anInt899 = buffer.getShort().toInt() and 0xFFFF
            99 -> aBoolean863 = true
            100 -> anInt869 = buffer.get().toInt()
            101 -> anInt897 = buffer.get() * 5
            102 -> headIcons = buffer.getShort().toInt() and 0xFFFF
            103 -> anInt853 = buffer.getShort().toInt() and 0xFFFF
            106, 118 -> {
                configFileId = buffer.getShort().toInt() and 0xFFFF
                if (configFileId == 65535) {
                    configFileId = -1
                }
                configId = buffer.getShort().toInt() and 0xFFFF
                if (configId == 65535) {
                    configId = -1
                }
                var defaultValue = -1
                if ((opcode xor -0x1) == -119) {
                    defaultValue = buffer.getShort().toInt() and 0xFFFF
                    if (defaultValue == 65535) {
                        defaultValue = -1
                    }
                }
                var length = buffer.get().toInt() and 0xFF
                childNPCIds = IntArray(2 + length)
                var i = 0
                while (length >= i) {
                    childNPCIds!![i] = buffer.getShort().toInt() and 0xFFFF
                    if (childNPCIds!![i] == 65535) {
                        childNPCIds!![i] = -1
                    }
                    i++
                }
                childNPCIds!![length + 1] = defaultValue
            }

            107 -> aBoolean841 = false
            109 -> aBoolean852 = false
            111 -> aBoolean857 = false
            113 -> {
                aShort862 = (buffer.getShort().toInt() and 0xFFFF).toShort()
                aShort894 = (buffer.getShort().toInt() and 0xFFFF).toShort()
            }

            114 -> {
                aByte851 = (buffer.get())
                aByte854 = (buffer.get())
            }

            115 -> {
                buffer.get() // & 0xFF;
                buffer.get() // & 0xFF;
            }

            119 -> buffer.get()
            121 -> {
                var length = buffer.get().toInt() and 0xFF
                var i = 0
                while (i < length) {
                    buffer.get()
                    buffer.get()
                    buffer.get()
                    buffer.get()
                    i++
                }
            }

            122 -> buffer.getShort()
            123 -> buffer.getShort()
            125 -> buffer.get()
            126 -> {
                buffer.getShort()
                renderAnimationId = buffer.getShort().toInt()
            }

            127 -> renderAnimationId = buffer.getShort().toInt()
            128 -> buffer.get()
            134 -> {
                buffer.getShort()
                buffer.getShort()
                buffer.getShort()
                buffer.getShort()
                buffer.get()
            }

            135 -> {
                buffer.get()
                buffer.getShort()
            }

            136 -> {
                buffer.get()
                buffer.getShort()
            }

            137 -> buffer.getShort()
            249 -> {
                var length = buffer.get().toInt() and 0xFF
                var i = 0
                while (i < length) {
                    val string = buffer.get().toInt() == 1
                    getMedium(buffer) // script id
                    if (!string) {
                        buffer.getInt() // Value
                    } else {
                        getString(buffer) // value
                    }
                    i++
                }
            }

            else -> {}
        }
    }

    /**
     * Has attack option boolean.
     *
     * @return the boolean
     */
    fun hasAttackOption(): Boolean {
        for (option in options) {
            if (option != null && option.equals("attack", ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Has action boolean.
     *
     * @param optionName the option name
     * @return the boolean
     */
    fun hasAction(optionName: String?): Boolean {
        if (options == null) {
            return false
        }
        for (action in options) {
            if (action == null) {
                continue
            }
            if (action.equals(optionName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun getExamine(): String? {
        val string = getConfiguration(NPCConfigParser.EXAMINE, examine)
        if (string != null) {
            return string
        }
        if (name.length <= 1) {
            return string
        }
        return "It's a" + (if (isPlusN(name)) "n " else " ") + name + "."
    }

    override fun setExamine(examine: String) {
        this.examine = examine
    }

    /**
     * Init combat graphics.
     *
     * @param config the config
     */
    fun initCombatGraphics(config: Map<String?, Any?>) {
        if (config.containsKey(NPCConfigParser.START_GRAPHIC)) {
            combatGraphics[0] =
                Graphics(
                    (config[NPCConfigParser.START_GRAPHIC] as Int?)!!,
                    getConfiguration(NPCConfigParser.START_HEIGHT, 0),
                )
        }
        if (config.containsKey(NPCConfigParser.PROJECTILE)) {
            combatGraphics[1] =
                Graphics(
                    (config[NPCConfigParser.PROJECTILE] as Int?)!!,
                    getConfiguration(NPCConfigParser.PROJECTILE_HEIGHT, 42),
                )
        }
        if (config.containsKey(NPCConfigParser.END_GRAPHIC)) {
            combatGraphics[2] =
                Graphics(
                    (config[NPCConfigParser.END_GRAPHIC] as Int?)!!,
                    getConfiguration(NPCConfigParser.END_HEIGHT, 96),
                )
        }
    }

    /**
     * Gets combat animation.
     *
     * @param index the index
     * @return the combat animation
     */
    fun getCombatAnimation(index: Int): Animation? {
        val name =
            when (index) {
                0 -> NPCConfigParser.MELEE_ANIMATION
                1 -> NPCConfigParser.MAGIC_ANIMATION
                2 -> NPCConfigParser.RANGE_ANIMATION
                3 -> NPCConfigParser.DEFENCE_ANIMATION
                4 -> NPCConfigParser.DEATH_ANIMATION
                else -> return null
            }
        return getConfiguration<Animation>(name, null) ?: return null
    }

    /**
     * Isa boolean 841 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean841(): Boolean = aBoolean841

    /**
     * Gets byte 851.
     *
     * @return the byte 851
     */
    fun getaByte851(): Byte = aByte851

    /**
     * Isa boolean 852 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean852(): Boolean = aBoolean852

    /**
     * Gets byte 854.
     *
     * @return the byte 854
     */
    fun getaByte854(): Byte = aByte854

    /**
     * Isa boolean 856 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean856(): Boolean = aBoolean856

    /**
     * Isa boolean 857 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean857(): Boolean = aBoolean857

    /**
     * Geta short array 859 short [ ].
     *
     * @return the short [ ]
     */
    fun getaShortArray859(): ShortArray? = aShortArray859

    /**
     * Geta byte array 861 byte [ ].
     *
     * @return the byte [ ]
     */
    fun getaByteArray861(): ByteArray? = aByteArray861

    /**
     * Gets short 862.
     *
     * @return the short 862
     */
    fun getaShort862(): Short = aShort862

    /**
     * Isa boolean 863 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean863(): Boolean = aBoolean863

    /**
     * Geta short array 866 short [ ].
     *
     * @return the short [ ]
     */
    fun getaShortArray866(): ShortArray? = aShortArray866

    /**
     * Geta short array 880 short [ ].
     *
     * @return the short [ ]
     */
    fun getaShortArray880(): ShortArray? = aShortArray880

    /**
     * Gets config id.
     *
     * @return the config id
     */
    fun getConfigId(): Int =
        if (configFileId != -1) {
            VarbitDefinition.forNpcId(configFileId).varpId
        } else {
            configFileId
        }

    val varbitOffset: Int
        /**
         * Gets varbit offset.
         *
         * @return the varbit offset
         */
        get() {
            if (configFileId != -1) {
                return VarbitDefinition.forNpcId(configFileId).startBit
            }
            return -1
        }

    val varbitSize: Int
        /**
         * Gets varbit size.
         *
         * @return the varbit size
         */
        get() {
            if (configFileId != -1) {
                return VarbitDefinition.forNpcId(configFileId).endBit - VarbitDefinition.forNpcId(configFileId).startBit
            }
            return -1
        }

    /**
     * Gets short 894.
     *
     * @return the short 894
     */
    fun getaShort894(): Short = aShort894

    /**
     * Geta short array 896 short [ ].
     *
     * @return the short [ ]
     */
    fun getaShortArray896(): ShortArray? = aShortArray896

    companion object {
        private val DEFINITIONS: MutableMap<Int, NPCDefinition> = HashMap()
        private val OPTION_HANDLERS: MutableMap<String, OptionHandler?> = HashMap()

        /**
         * For id npc definition.
         *
         * @param id the id
         * @return the npc definition
         */
        @JvmStatic
        fun forId(id: Int): NPCDefinition {
            var def = DEFINITIONS[id]
            if (def == null) {
                def = NPCDefinition(id)
                val data = getData(CacheIndex.NPC_CONFIGURATION, id ushr 7, id and 0x7f)
                if (data == null) {
                    if (id != -1) {
                    }
                } else {
                    def.parse(ByteBuffer.wrap(data))
                }
                DEFINITIONS[id] = def
            }
            return def
        }

        /**
         * The entry point of application.
         *
         * @param args the input arguments
         * @throws Throwable the throwable
         */
        @Throws(Throwable::class)
        @JvmStatic
        fun main(args: Array<String>) {
            prompt(false)
        }

        /**
         * Gets option handler.
         *
         * @param nodeId the node id
         * @param name   the name
         * @return the option handler
         */
        @JvmStatic
        fun getOptionHandler(
            nodeId: Int,
            name: String,
        ): OptionHandler? {
            val def = forId(nodeId)
            val handler = def.getConfiguration<OptionHandler>("option:$name")
            if (handler != null) {
                return handler
            }
            return OPTION_HANDLERS[name]
        }

        /**
         * Sets option handler.
         *
         * @param name    the name
         * @param handler the handler
         * @return the option handler
         */
        @JvmStatic
        fun setOptionHandler(
            name: String,
            handler: OptionHandler?,
        ): Boolean = OPTION_HANDLERS.put(name, handler) != null

        val optionHandlers: Map<String, OptionHandler?>
            /**
             * Gets option handlers.
             *
             * @return the option handlers
             */
            get() = OPTION_HANDLERS

        val definitions: Map<Int, NPCDefinition>
            /**
             * Gets definitions.
             *
             * @return the definitions
             */
            get() = DEFINITIONS
    }
}
