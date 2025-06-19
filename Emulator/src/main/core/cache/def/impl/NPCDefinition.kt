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
 * Represents the NPC definitions.
 */
class NPCDefinition(id: Int) : Definition<NPC?>() {
    @JvmField var size: Int = 1
    @JvmField var combatLevel: Int
    @JvmField var headIcons: Int
    @JvmField var minimapDot: Boolean
    @JvmField var dropTables: NPCDropTables = NPCDropTables(this)
    @JvmField var ambientLight: Int
    @JvmField var shadowLight: Int
    @JvmField var modelSizeX: Int
    @JvmField var rotated: Boolean
    @JvmField var modelSizeY: Int
    @JvmField var varpId: Int
    @JvmField var childNPCIds: IntArray? = null
    @JvmField var ambientSoundId: Int
    @JvmField var contrast: Int
    @JvmField var modelPitch: Byte
    @JvmField var clickable: Boolean
    @JvmField var modelYaw: Int
    @JvmField var modelRoll: Byte
    @JvmField var visible: Boolean
    @JvmField var hasRenderPriority: Boolean
    @JvmField var originalColors: ShortArray? = null
    @JvmField var recolorHueShift: ByteArray? = null
    @JvmField var ambientSoundVolume: Short
    @JvmField var hasShadow: Boolean
    @JvmField var modelScaleX: Int
    @JvmField var modifiedColors: ShortArray? = null
    @JvmField var modelIds: IntArray
    @JvmField var ambientSoundMinDelay: Int
    @JvmField var ambientSoundMaxDelay: Int
    @JvmField var ambientSoundLoops: Int
    @JvmField var combatLevelOverride: Int
    @JvmField var mapIconId: Int
    @JvmField var cursorOp: Int
    @JvmField var mapSceneId: Int
    @JvmField var shadowModifier: Int
    @JvmField var originalTextures: ShortArray? = null
    @JvmField var clickableAreaId: Int
    @JvmField var varbitId: Int
    @JvmField var transparency: Int
    @JvmField var headIconModels: IntArray? = null
    @JvmField var lightModifier: Short
    @JvmField var modifiedTextures: ShortArray? = null
    @JvmField var shadowOpacity: Int
    @JvmField var modelScaleY: Int
    @JvmField var cursor: Int
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
        modelSizeY = -1
        varpId = -1
        modelSizeX = -1
        ambientSoundId = -1
        modelYaw = 32
        standAnimation = -1
        walkAnimation = -1
        combatLevel = 0
        shadowLight = -1
        name = "null"
        ambientSoundMinDelay = 0
        contrast = 255
        ambientSoundLoops = -1
        clickable = true
        ambientSoundVolume = 0
        mapSceneId = -1
        modelPitch = -96
        cursorOp = 0
        combatLevelOverride = -1
        hasRenderPriority = true
        ambientSoundMaxDelay = -1
        mapIconId = -1
        ambientLight = -1
        modelScaleX = 128
        headIcons = -1
        visible = false
        varbitId = -1
        modelRoll = -16
        hasShadow = false
        minimapDot = true
        transparency = -1
        clickableAreaId = -1
        rotated = true
        shadowModifier = -1
        modelScaleY = 128
        lightModifier = 0
        options = arrayOfNulls(5)
        shadowOpacity = 0
        cursor = -1
        modelIds = IntArray(0)
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
            if (varpId != -1) {
                configValue = VarbitDefinition.forNpcId(varpId).getValue(player)
            } else if (varbitId != -1) {
                configValue = getVarp(player, varbitId)
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
                modelIds = IntArray(length)
                var i_66_ = 0
                while (i_66_ < length) {
                    modelIds[i_66_] = buffer.getShort().toInt() and 0xFFFF
                    if ((modelIds[i_66_] xor -0x1) == -65536) modelIds[i_66_] = -1
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
                originalColors = ShortArray(length)
                modifiedTextures = ShortArray(length)
                var i_65_ = 0
                while ((length xor -0x1) < (i_65_ xor -0x1)) {
                    modifiedTextures!![i_65_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    originalColors!![i_65_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    i_65_++
                }
            }

            41 -> {
                var length = buffer.get().toInt() and 0xFF
                originalTextures = ShortArray(length)
                modifiedColors = ShortArray(length)
                var i_54_ = 0
                while ((i_54_ xor -0x1) > (length xor -0x1)) {
                    originalTextures!![i_54_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    modifiedColors!![i_54_] = (buffer.getShort().toInt() and 0xFFFF).toShort()
                    i_54_++
                }
            }

            42 -> {
                var length = buffer.get().toInt() and 0xFF
                recolorHueShift = ByteArray(length)
                var i_55_ = 0
                while (length > i_55_) {
                    recolorHueShift!![i_55_] = buffer.get()
                    i_55_++
                }
            }

            60 -> {
                var length = buffer.get().toInt() and 0xFF
                headIconModels = IntArray(length)
                var i_64_ = 0
                while ((i_64_ xor -0x1) > (length xor -0x1)) {
                    headIconModels!![i_64_] = buffer.getShort().toInt() and 0xFFFF
                    i_64_++
                }
            }

            93 -> minimapDot = false
            95 -> combatLevel = buffer.getShort().toInt() and 0xFFFF
            97 -> modelScaleX = buffer.getShort().toInt() and 0xFFFF
            98 -> modelScaleY = buffer.getShort().toInt() and 0xFFFF
            99 -> hasShadow = true
            100 -> ambientSoundMinDelay = buffer.get().toInt()
            101 -> shadowOpacity = buffer.get() * 5
            102 -> headIcons = buffer.getShort().toInt() and 0xFFFF
            103 -> modelYaw = buffer.getShort().toInt() and 0xFFFF
            106, 118 -> {
                varpId = buffer.getShort().toInt() and 0xFFFF
                if (varpId == 65535) {
                    varpId = -1
                }
                varbitId = buffer.getShort().toInt() and 0xFFFF
                if (varbitId == 65535) {
                    varbitId = -1
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

            107 -> rotated = false
            109 -> clickable = false
            111 -> hasRenderPriority = false
            113 -> {
                ambientSoundVolume = (buffer.getShort().toInt() and 0xFFFF).toShort()
                lightModifier = (buffer.getShort().toInt() and 0xFFFF).toShort()
            }

            114 -> {
                modelPitch = (buffer.get())
                modelRoll = (buffer.get())
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
     * Checks if the NPC model is rotated by 90 degrees.
     *
     * @return true if rotated.
     */
    fun isRotated(): Boolean = rotated

    /**
     * Gets the model pitch rotation.
     *
     * @return the model pitch value.
     */
    fun getModelPitch(): Byte = modelPitch

    /**
     * Checks if the NPC is clickable.
     *
     * @return true if clickable.
     */
    fun isClickable(): Boolean = clickable

    /**
     * Gets the model roll rotation.
     *
     * @return the model roll value.
     */
    fun getModelRoll(): Byte = modelRoll

    /**
     * Checks if the NPC is visible.
     *
     * @return true if visible.
     */
    fun isVisible(): Boolean = visible

    /**
     * Checks if the NPC has render priority.
     *
     * @return true if has render priority.
     */
    fun hasRenderPriority(): Boolean = hasRenderPriority

    /**
     * Gets the original model colors before any recoloring.
     *
     * @return the original colors as a ShortArray.
     */
    fun getOriginalColors(): ShortArray? = originalColors

    /**
     * Gets the hue shifts applied to the recolored model.
     *
     * @return the hue shift values as a ByteArray.
     */
    fun getRecolorHueShift(): ByteArray? = recolorHueShift

    /**
     * Gets the ambient sound volume for the NPC.
     *
     * @return the ambient sound volume.
     */
    fun getAmbientSoundVolume(): Short = ambientSoundVolume

    /**
     * Checks if the NPC has a shadow applied.
     *
     * @return true if has shadow.
     */
    fun hasShadow(): Boolean = hasShadow

    /**
     * Gets the modified (recolored) model colors.
     *
     * @return the modified colors as a ShortArray.
     */
    fun getModifiedColors(): ShortArray? = modifiedColors

    /**
     * Gets the original model textures.
     *
     * @return the original textures as a ShortArray.
     */
    fun getOriginalTextures(): ShortArray? = originalTextures

    /**
     * Gets the Varp id used for morph.
     *
     * @return the Varp id.
     */
    fun getVarpId(): Int =
        if (varpId != -1) {
            VarbitDefinition.forNpcId(varpId).varpId
        } else {
            varpId
        }

    /**
     * Gets the start bit offset of the associated Varbit.
     *
     * @return the varbit start bit offset.
     */
    val varbitOffset: Int
        get() {
            if (varpId != -1) {
                return VarbitDefinition.forNpcId(varpId).startBit
            }
            return -1
        }

    /**
     * Gets the bit size of the associated varbit.
     *
     * @return the varbit size in bits.
     */
    val varbitSize: Int
        get() {
            if (varpId != -1) {
                return VarbitDefinition.forNpcId(varpId).endBit - VarbitDefinition.forNpcId(varpId).startBit
            }
            return -1
        }

    /**
     * Gets the light modifier for the NPC.
     *
     * @return the light modifier value.
     */
    fun getLightModifier(): Short = lightModifier

    /**
     * Gets the modified (retextured) model textures.
     *
     * @return the modified textures as a ShortArray.
     */
    fun getModifiedTextures(): ShortArray? = modifiedTextures

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
