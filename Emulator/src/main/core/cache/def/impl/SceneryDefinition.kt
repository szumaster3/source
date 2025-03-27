package core.cache.def.impl

import core.api.getVarp
import core.api.log
import core.cache.Cache.getData
import core.cache.Cache.getIndexCapacity
import core.cache.CacheIndex
import core.cache.def.Definition
import core.cache.misc.buffer.ByteBufferUtils.getMedium
import core.cache.misc.buffer.ByteBufferUtils.getString
import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.GameWorld.prompt
import core.tools.Log
import java.nio.ByteBuffer

/**
 * The type Scenery definition.
 */
class SceneryDefinition : Definition<Scenery?>() {
    var originalColors: ShortArray? = null

    @JvmField var configObjectIds: IntArray? = null

    @JvmField var modelIds: IntArray? = null
    private var modelConfiguration: IntArray? = null

    @JvmField var anIntArray3833: IntArray? = null

    @JvmField val anInt3834: Int = 0

    @JvmField var anInt3835: Int = -1
    private val aByte3837: Byte = 0

    @JvmField var anInt3838: Int = -1

    @JvmField var mirrored: Boolean

    @JvmField var contrast: Int

    @JvmField var modelSizeZ: Int

    @JvmField var anInt3844: Int

    @JvmField var aBoolean3845: Boolean = false
    private val aByte3847: Byte = 0
    private val aByte3849: Byte = 0

    @JvmField var anInt3850: Int = 0

    @JvmField var anInt3851: Int

    @JvmField var isBlocksLand: Boolean

    @JvmField var aBoolean3853: Boolean

    @JvmField var anInt3855: Int

    @JvmField var isFirstBool: Boolean

    @JvmField var anInt3857: Int

    @JvmField var recolourPalette: ByteArray? = null

    @JvmField var anIntArray3859: IntArray? = null

    @JvmField var anInt3860: Int

    @JvmField var varbitID: Int

    @JvmField var modifiedColors: ShortArray? = null

    @JvmField var anInt3865: Int = 255

    @JvmField var aBoolean3866: Boolean = false

    @JvmField var aBoolean3867: Boolean = false

    @JvmField var isProjectileClipped: Boolean

    @JvmField val anIntArray3869: IntArray?

    @JvmField var aBoolean3870: Boolean

    @JvmField var sizeY: Int

    @JvmField var isCastsShadow: Boolean = true

    @JvmField var membersOnly: Boolean

    @JvmField var thirdBoolean: Boolean

    @JvmField val anInt3875: Int

    @JvmField var addObjectCheck: Int

    @JvmField val anInt3877: Int

    @JvmField var brightness: Int

    @JvmField var solid: Int

    @JvmField val anInt3881: Int = 0

    @JvmField var anInt3882: Int

    @JvmField var offsetX: Int

    @JvmField var loader: Any? = null

    @JvmField var offsetZ: Int

    @JvmField var sizeX: Int

    @JvmField var aBoolean3891: Boolean

    @JvmField var offsetMultiplier: Int

    @JvmField var interactive: Int

    @JvmField var aBoolean3894: Boolean

    @JvmField var aBoolean3895: Boolean

    @JvmField var anInt3896: Int = 0

    @JvmField var configId: Int
    private val aByteArray3899: ByteArray? = null

    @JvmField var anInt3900: Int

    @JvmField var modelSizeX: Int

    @JvmField var anInt3904: Int

    @JvmField var anInt3905: Int

    @JvmField var aBoolean3906: Boolean

    @JvmField var anIntArray3908: IntArray? = null

    @JvmField var contouredGround: Byte

    @JvmField var anInt3913: Int
    private val aByte3914: Byte

    @JvmField var offsetY: Int

    @JvmField val anIntArrayArray3916: Array<IntArray>? = null

    @JvmField var modelSizeY: Int

    @JvmField var modifiedTextureColours: ShortArray? = null

    @JvmField var originalTextureColours: ShortArray? = null

    @JvmField var anInt3921: Int
    private val aClass194_3922: Any? = null

    @JvmField var aBoolean3923: Boolean

    @JvmField var aBoolean3924: Boolean

    @JvmField var walkingFlag: Int
    private var hasHiddenOptions = false

    @JvmField var mapIcon: Short

    /**
     * Instantiates a new Scenery definition.
     */
    init {
        anInt3860 = -1
        varbitID = -1
        anInt3851 = -1
        anInt3844 = -1
        anInt3857 = -1
        anInt3882 = -1
        options = arrayOfNulls(5)
        anInt3875 = 0
        mirrored = false
        anIntArray3869 = null
        sizeY = 1
        thirdBoolean = false
        isProjectileClipped = true
        offsetX = 0
        aBoolean3895 = true
        contrast = 0
        aBoolean3870 = false
        offsetZ = 0
        aBoolean3853 = true
        isBlocksLand = false
        solid = 2
        anInt3855 = -1
        brightness = 0
        anInt3904 = 0
        sizeX = 1
        addObjectCheck = -1
        isFirstBool = false
        aBoolean3891 = false
        anInt3905 = 0
        name = "null"
        anInt3913 = -1
        aBoolean3906 = false
        membersOnly = false
        aByte3914 = 0.toByte()
        offsetY = 0
        anInt3900 = 0
        interactive = -1
        aBoolean3894 = false
        contouredGround = 0.toByte()
        anInt3921 = 0
        modelSizeX = 128
        configId = -1
        anInt3877 = 0
        walkingFlag = 0
        offsetMultiplier = 64
        aBoolean3923 = false
        aBoolean3924 = false
        modelSizeZ = 128
        modelSizeY = 128
        mapIcon = -1
    }

    /**
     * Configure object.
     */
    fun configureObject() {
        if (interactive == -1) {
            interactive = 0
            if (modelIds != null && (getModelConfiguration() == null || getModelConfiguration()!![0] == 10)) {
                interactive = 1
            }
            for (i in 0..4) {
                if (options[i] != null) {
                    interactive = 1
                    break
                }
            }
        }
        if (configObjectIds != null) {
            for (i in configObjectIds!!.indices) {
                val def = forId(configObjectIds!![i])
                def.varbitID = varbitID
            }
        }
        if (anInt3855 == -1) {
            anInt3855 = if (solid == 0) 0 else 1
        }
        // Manual changes
        if (id == 31017) {
            sizeY = 2
            sizeX = sizeY
        }
        if (id == 29292) {
            isProjectileClipped = false
        }
    }

    /**
     * Has actions boolean.
     *
     * @return the boolean
     */
    fun hasActions(): Boolean {
        if (interactive > 0) {
            return true
        }
        if (configObjectIds == null) {
            return hasOptions(false)
        }
        for (i in configObjectIds!!.indices) {
            if (configObjectIds!![i] != -1) {
                val def = forId(configObjectIds!![i])
                if (def.hasOptions(false)) {
                    return true
                }
            }
        }
        return hasOptions(false)
    }

    /**
     * Gets child object.
     *
     * @param player the player
     * @return the child object
     */
    fun getChildObject(player: Player?): SceneryDefinition? {
        if (configObjectIds == null || configObjectIds!!.size < 1) {
            return this
        }
        var configValue = -1
        if (player != null) {
            if (varbitID != -1) {
                val def = VarbitDefinition.forSceneryId(varbitID)
                if (def != null) {
                    configValue = def.getValue(player)
                }
            } else if (configId != -1) {
                configValue = getVarp(player, configId)
            }
        } else {
            configValue = 0
        }
        val childDef = getChildObjectAtIndex(configValue)
        if (childDef != null) childDef.varbitID = this.varbitID
        return childDef
    }

    /**
     * Gets child object at index.
     *
     * @param index the index
     * @return the child object at index
     */
    fun getChildObjectAtIndex(index: Int): SceneryDefinition {
        if (configObjectIds == null || configObjectIds!!.size < 1) {
            return this
        }
        if (index < 0 || index >= configObjectIds!!.size - 1 || configObjectIds!![index] == -1) {
            val objectId = configObjectIds!![configObjectIds!!.size - 1]
            if (objectId != -1) {
                return forId(objectId)
            }
            return this
        }
        return forId(configObjectIds!![index])
    }

    val configFile: VarbitDefinition?
        /**
         * Gets config file.
         *
         * @return the config file
         */
        get() {
            if (varbitID != -1) {
                return VarbitDefinition.forSceneryId(varbitID)
            }
            return null
        }

    /**
     * Sets option.
     *
     * @param slot   the slot
     * @param option the option
     */
    fun setOption(
        slot: Int,
        option: String?,
    ) {
        require(!(slot < 0 || slot >= options.size)) { ": $slot" }
        options[slot] = option
    }

    /**
     * Remove option.
     *
     * @param slot the slot
     */
    fun removeOption(slot: Int) {
        require(!(slot < 0 || slot >= options.size)) { "Wrong index: $slot" }
        options[slot] = null
    }

    /**
     * Print options.
     */
    fun printOptions() {
        println("Options for object $id:")
        for (i in options.indices) {
            println("Slot " + i + ": " + (if (options[i] != null) options[i] else "No options"))
        }
    }

    /**
     * Is mirrored boolean.
     *
     * @return the boolean
     */
    fun isMirrored(): Boolean {
        return mirrored
    }

    /**
     * Sets mirrored.
     *
     * @param mirrored the mirrored
     */
    fun setMirrored(mirrored: Boolean) {
        this.mirrored = mirrored
    }

    /**
     * Gets byte 3837.
     *
     * @return the byte 3837
     */
    fun getaByte3837(): Byte {
        return aByte3837
    }

    /**
     * Isa boolean 3845 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3845(): Boolean {
        return aBoolean3845
    }

    /**
     * Gets byte 3847.
     *
     * @return the byte 3847
     */
    fun getaByte3847(): Byte {
        return aByte3847
    }

    /**
     * Gets byte 3849.
     *
     * @return the byte 3849
     */
    fun getaByte3849(): Byte {
        return aByte3849
    }

    /**
     * Isa boolean 3853 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3853(): Boolean {
        return aBoolean3853
    }

    override fun getOptions(): Array<String> {
        return options
    }

    /**
     * Isa boolean 3866 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3866(): Boolean {
        return aBoolean3866
    }

    /**
     * Isa boolean 3867 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3867(): Boolean {
        return aBoolean3867
    }

    /**
     * Isa boolean 3870 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3870(): Boolean {
        return aBoolean3870
    }

    /**
     * Isa boolean 3873 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3873(): Boolean {
        return membersOnly
    }

    /**
     * Isa boolean 3891 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3891(): Boolean {
        return aBoolean3891
    }

    /**
     * Isa boolean 3894 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3894(): Boolean {
        return aBoolean3894
    }

    /**
     * Isa boolean 3895 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3895(): Boolean {
        return aBoolean3895
    }

    /**
     * Geta byte array 3899 byte [ ].
     *
     * @return the byte [ ]
     */
    fun getaByteArray3899(): ByteArray? {
        return aByteArray3899
    }

    override fun getName(): String {
        return name
    }

    /**
     * Isa boolean 3906 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3906(): Boolean {
        return aBoolean3906
    }

    /**
     * Gets byte 3914.
     *
     * @return the byte 3914
     */
    fun getaByte3914(): Byte {
        return aByte3914
    }

    /**
     * Gets class 194 3922.
     *
     * @return the class 194 3922
     */
    fun getaClass194_3922(): Any? {
        return aClass194_3922
    }

    /**
     * Isa boolean 3923 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3923(): Boolean {
        return aBoolean3923
    }

    /**
     * Isa boolean 3924 boolean.
     *
     * @return the boolean
     */
    fun isaBoolean3924(): Boolean {
        return aBoolean3924
    }

    /**
     * Has action boolean.
     *
     * @param action the action
     * @return the boolean
     */
    fun hasAction(action: String?): Boolean {
        if (options == null) {
            return false
        }
        for (option in options) {
            if (option == null) {
                continue
            }
            if (option.equals(action, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Is has hidden options boolean.
     *
     * @return the boolean
     */
    fun isHasHiddenOptions(): Boolean {
        return hasHiddenOptions
    }

    /**
     * Sets has hidden options.
     *
     * @param hasHiddenOptions the has hidden options
     */
    fun setHasHiddenOptions(hasHiddenOptions: Boolean) {
        this.hasHiddenOptions = hasHiddenOptions
    }

    /**
     * Get model configuration int [ ].
     *
     * @return the int [ ]
     */
    fun getModelConfiguration(): IntArray? {
        return modelConfiguration
    }

    /**
     * Sets model configuration.
     *
     * @param modelConfiguration the model configuration
     */
    fun setModelConfiguration(modelConfiguration: IntArray) {
        this.modelConfiguration = modelConfiguration
    }

    companion object {
        /**
         * Gets definitions.
         *
         * @return the definitions
         */
        val definitions: MutableMap<Int, SceneryDefinition> = HashMap()
        private val OPTION_HANDLERS: MutableMap<String, OptionHandler?> = HashMap()
        /**
         * Gets an int 3832.
         *
         * @return the an int 3832
         */
        /**
         * The An int 3832.
         */
        var anInt3832: Int = 0
        /**
         * Gets an int 3836.
         *
         * @return the an int 3836
         */
        /**
         * The An int 3836.
         */
        var anInt3836: Int = 0
        /**
         * Gets an int 3842.
         *
         * @return the an int 3842
         */
        /**
         * The An int 3842.
         */
        var anInt3842: Int = 0
        /**
         * Gets an int 3843.
         *
         * @return the an int 3843
         */
        /**
         * The An int 3843.
         */
        var anInt3843: Int = 0
        /**
         * Gets an int 3846.
         *
         * @return the an int 3846
         */
        /**
         * The An int 3846.
         */
        var anInt3846: Int = 0

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
         * Parse.
         *
         * @throws Throwable the throwable
         */
        @Throws(Throwable::class)
        fun parse() {
            for (objectId in 0 until getIndexCapacity(CacheIndex.SCENERY_CONFIGURATION)) {
                var data = getData(CacheIndex.SCENERY_CONFIGURATION, objectId ushr 8, objectId and 0xFF)
                if (data == null) {
                    definitions[objectId] = SceneryDefinition()
                    continue
                }
                val def = parseDefinition(objectId, ByteBuffer.wrap(data))
                definitions[objectId] = def
                data = null
            }
        }

        /**
         * For id scenery definition.
         *
         * @param objectId the object id
         * @return the scenery definition
         */
        @JvmStatic
        fun forId(objectId: Int): SceneryDefinition {
            var def = definitions[objectId]
            if (def != null) {
                return def
            }
            definitions[objectId] = SceneryDefinition().also { def = it }
            def!!.id = objectId
            return def!!
        }

        /**
         * Parse definition scenery definition.
         *
         * @param objectId the object id
         * @param buffer   the buffer
         * @return the scenery definition
         */
        private fun parseDefinition(
            objectId: Int,
            buffer: ByteBuffer,
        ): SceneryDefinition {
            val def = SceneryDefinition()
            def.id = objectId
            while (true) {
                if (!buffer.hasRemaining()) {
                    log(SceneryDefinition::class.java, Log.ERR, "Buffer empty for $objectId")
                    break
                }
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 1 || opcode == 5) {
                    val length = buffer.get().toInt() and 0xff
                    if (def.modelIds == null) {
                        def.modelIds = IntArray(length)
                        if (opcode == 1) {
                            def.modelConfiguration = IntArray(length)
                        }
                        for (i in 0 until length) {
                            def.modelIds!![i] = buffer.getShort().toInt() and 0xFFFF
                            if (opcode == 1) {
                                def.modelConfiguration!![i] = buffer.get().toInt() and 0xFF
                            }
                        }
                    } else {
                        buffer.position(buffer.position() + (length * (if (opcode == 1) 3 else 2)))
                    }
                } else if (opcode == 2) {
                    def.name = getString(buffer)
                } else if (opcode == 14) {
                    def.sizeX = buffer.get().toInt() and 0xFF
                } else if (opcode == 15) {
                    def.sizeY = buffer.get().toInt() and 0xFF
                } else if (opcode == 17) {
                    def.isProjectileClipped = false
                    def.solid = 0
                } else if (opcode == 18) {
                    def.isProjectileClipped = false
                } else if (opcode == 19) {
                    def.interactive = buffer.get().toInt() and 0xFF
                } else if (opcode == 21) {
                    def.contouredGround = 1.toByte()
                } else if (opcode == 22) {
                    def.aBoolean3867 = true
                } else if (opcode == 23) {
                    def.thirdBoolean = true
                } else if (opcode == 24) {
                    def.addObjectCheck = buffer.getShort().toInt() and 0xFFFF
                    if (def.addObjectCheck == 65535) {
                        def.addObjectCheck = -1
                    }
                } else if (opcode == 27) {
                    def.solid = 1
                } else if (opcode == 28) {
                    def.offsetMultiplier = ((buffer.get().toInt() and 0xFF) shl 2)
                } else if (opcode == 29) {
                    def.brightness = buffer.get().toInt()
                } else if (opcode == 39) {
                    def.contrast = buffer.get() * 5
                } else if (opcode >= 30 && opcode < 35) {
                    def.options[opcode - 30] = getString(buffer)
                    if (def.options[opcode - 30] == "Hidden") {
                        def.options[opcode - 30] = null
                        def.hasHiddenOptions = true
                    }
                } else if (opcode == 40) {
                    val length = buffer.get().toInt() and 0xFF
                    def.originalColors = ShortArray(length)
                    def.modifiedColors = ShortArray(length)
                    for (i in 0 until length) {
                        def.originalColors!![i] = buffer.getShort()
                        def.modifiedColors!![i] = buffer.getShort()
                    }
                } else if (opcode == 41) {
                    val length = buffer.get().toInt() and 0xFF
                    def.originalTextureColours = ShortArray(length)
                    def.modifiedTextureColours = ShortArray(length)
                    for (i in 0 until length) {
                        def.originalTextureColours!![i] = buffer.getShort()
                        def.modifiedTextureColours!![i] = buffer.getShort()
                    }
                } else if (opcode == 42) {
                    val length = buffer.get().toInt() and 0xFF
                    def.recolourPalette = ByteArray(length)
                    for (i in 0 until length) {
                        def.recolourPalette!![i] = buffer.get()
                    }
                } else if (opcode == 60) {
                    def.mapIcon = buffer.getShort()
                } else if (opcode == 62) {
                    def.mirrored = true
                } else if (opcode == 64) {
                    def.isCastsShadow = false
                } else if (opcode == 65) {
                    def.modelSizeX = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 66) {
                    def.modelSizeZ = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 67) {
                    def.modelSizeY = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 68) {
                    buffer.getShort()
                } else if (opcode == 69) {
                    def.walkingFlag = buffer.get().toInt() and 0xFF
                } else if (opcode == 70) {
                    def.offsetX = buffer.getShort().toInt() shl 2
                } else if (opcode == 71) {
                    def.offsetZ = buffer.getShort().toInt() shl 2
                } else if (opcode == 72) {
                    def.offsetY = buffer.getShort().toInt() shl 2
                } else if (opcode == 73) {
                    def.isBlocksLand = true
                } else if (opcode == 74) {
                    def.isFirstBool = true
                } else if (opcode == 75) {
                    def.anInt3855 = buffer.get().toInt() and 0xFF
                } else if (opcode == 77 || opcode == 92) {
                    def.varbitID = buffer.getShort().toInt() and 0xFFFF
                    if (def.varbitID == 65535) {
                        def.varbitID = -1
                    }
                    def.configId = buffer.getShort().toInt() and 0xFFFF
                    if (def.configId == 65535) {
                        def.configId = -1
                    }
                    var defaultId = -1
                    if (opcode == 92) {
                        defaultId = buffer.getShort().toInt() and 0xFFFF
                        if (defaultId == 65535) {
                            defaultId = -1
                        }
                    }
                    val childrenAmount = buffer.get().toInt() and 0xFF
                    def.configObjectIds = IntArray(childrenAmount + 2)
                    var index = 0
                    while (childrenAmount >= index) {
                        def.configObjectIds!![index] = buffer.getShort().toInt() and 0xFFFF
                        if (def.configObjectIds!![index] == 65535) {
                            def.configObjectIds!![index] = -1
                        }
                        index++
                    }
                    def.configObjectIds!![childrenAmount + 1] = defaultId
                } else if (opcode == 78) {
                    def.anInt3860 = buffer.getShort().toInt() and 0xFFFF
                    def.anInt3904 = buffer.get().toInt() and 0xFF
                } else if (opcode == 79) {
                    def.anInt3900 = buffer.getShort().toInt() and 0xFFFF
                    def.anInt3905 = buffer.getShort().toInt() and 0xFFFF
                    def.anInt3904 = buffer.get().toInt() and 0xFF
                    val length = buffer.get().toInt() and 0xFF
                    def.anIntArray3859 = IntArray(length)
                    for (i in 0 until length) {
                        def.anIntArray3859!![i] = buffer.getShort().toInt() and 0xFFFF
                    }
                } else if (opcode == 81) {
                    def.contouredGround = 2.toByte()
                    def.anInt3882 = 256 * buffer.get() and 0xFF
                } else if (opcode == 82 || opcode == 88) {
                } else if (opcode == 89) {
                    def.aBoolean3895 = false
                } else if (opcode == 90) {
                    def.aBoolean3870 = true
                } else if (opcode == 91) {
                    def.membersOnly = true
                } else if (opcode == 93) {
                    def.contouredGround = 3.toByte()
                    def.anInt3882 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 94) {
                    def.contouredGround = 4.toByte()
                } else if (opcode == 95) {
                    def.contouredGround = 5.toByte()
                } else if (opcode == 96 || opcode == 97) {
                } else if (opcode == 100) {
                    buffer.get()
                    buffer.getShort()
                } else if (opcode == 101) {
                    buffer.get()
                } else if (opcode == 102) {
                    buffer.getShort()
                } else if (opcode == 249) { // cs2 scripts
                    val length = buffer.get().toInt() and 0xFF
                    for (i in 0 until length) {
                        val string = buffer.get().toInt() == 1
                        getMedium(buffer) // script id
                        if (!string) {
                            buffer.getInt() // Value
                        } else {
                            getString(buffer) // value
                        }
                    }
                } else {
                    if (opcode != 0) {
                        log(
                            SceneryDefinition::class.java,
                            Log.ERR,
                            "Unhandled object definition opcode: $opcode",
                        )
                    }
                    break
                }
            }
            def.configureObject()
            if (def.isFirstBool) {
                def.solid = 0
                def.isProjectileClipped = false
            }
            return def
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
        ): Boolean {
            return OPTION_HANDLERS.put(name, handler) != null
        }

        /**
         * Gets container id.
         *
         * @param id the id
         * @return the container id
         */
        fun getContainerId(id: Int): Int {
            return id ushr 8
        }
    }
}
