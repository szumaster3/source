package core.cache.def.impl

import core.api.EquipmentSlot
import core.api.equipSlot
import core.api.log
import core.cache.Cache.getData
import core.cache.Cache.getIndexCapacity
import core.cache.CacheIndex
import core.cache.def.Definition
import core.cache.misc.buffer.ByteBufferUtils.getMedium
import core.cache.misc.buffer.ByteBufferUtils.getString
import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.item.ItemPlugin
import core.game.system.config.ItemConfigParser
import core.tools.Log
import core.tools.StringUtils.isPlusN
import org.rs.consts.Components
import org.rs.consts.Items
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.round

/**
 * The type Item definition.
 */
class ItemDefinition : Definition<Item?>() {
    @JvmField var interfaceModelId = 0

    @JvmField var modelZoom = 0

    @JvmField var modelRotationX = 0

    @JvmField var modelRotationY = 0

    @JvmField var modelOffset1: Int = 0

    @JvmField var modelOffset2: Int = 0

    @JvmField var stackable = false

    @JvmField var value = 1

    @JvmField var membersOnly = false

    @JvmField var maleWornModelId1 = -1

    @JvmField var femaleWornModelId1 = -1

    @JvmField var maleWornModelId2 = -1

    @JvmField var femaleWornModelId2 = -1

    @JvmField var maleWornModelId3: Int = -1

    @JvmField var femaleWornModelId3: Int = -1

    @JvmField var maleWornModelId4: Int = -1

    @JvmField var femaleWornModelId4: Int = -1

    @JvmField var groundOptions: Array<String?>

    @JvmField var originalModelColors: ShortArray? = null

    @JvmField var modifiedModelColors: ShortArray? = null

    @JvmField var textureColour1: ShortArray? = null

    @JvmField var textureColour2: ShortArray? = null

    @JvmField var unknownArray1: ByteArray? = null

    @JvmField var unknownArray2: IntArray? = null

    @JvmField val unknownArray3: Array<IntArray>? = null

    @JvmField var isUnnoted = true

    @JvmField var colourEquip1 = -1

    @JvmField var colourEquip2 = 0

    @JvmField var noteId = -1

    @JvmField var noteTemplateId = -1

    @JvmField var stackIds: IntArray? = null

    @JvmField var stackAmounts: IntArray? = null

    @JvmField var teamId = 0

    @JvmField var lendId = -1

    @JvmField var lendTemplateId = -1

    @JvmField var recolourId: Int = -1

    @JvmField var recolourTemplateId: Int = -1

    @JvmField var equipId = 0

    @JvmField var itemRequirements: HashMap<Int, Int>? = null

    @JvmField var clientScriptData: HashMap<Int, Any>? = null

    @JvmField var itemType = 0

    /**
     * Transfer note definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferNoteDefinition(
        reference: ItemDefinition,
        templateReference: ItemDefinition,
    ) {
        membersOnly = reference.membersOnly
        interfaceModelId = templateReference.interfaceModelId
        originalModelColors = templateReference.originalModelColors
        name = reference.name
        modelOffset2 = templateReference.modelOffset2
        textureColour1 = templateReference.textureColour1
        value = reference.value
        modelRotationY = templateReference.modelRotationY
        stackable = true
        isUnnoted = false
        modifiedModelColors = templateReference.modifiedModelColors
        modelRotationX = templateReference.modelRotationX
        modelZoom = templateReference.modelZoom
        textureColour1 = templateReference.textureColour1
        handlers[ItemConfigParser.TRADEABLE] = true
    }

    /**
     * Transfer lend definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferLendDefinition(
        reference: ItemDefinition,
        templateReference: ItemDefinition,
    ) {
        femaleWornModelId1 = reference.femaleWornModelId1
        maleWornModelId2 = reference.maleWornModelId2
        membersOnly = reference.membersOnly
        interfaceModelId = templateReference.interfaceModelId
        textureColour2 = reference.textureColour2
        groundOptions = reference.groundOptions
        unknownArray1 = reference.unknownArray1
        modelRotationX = templateReference.modelRotationX
        modelRotationY = templateReference.modelRotationY
        originalModelColors = reference.originalModelColors
        name = reference.name
        maleWornModelId1 = reference.maleWornModelId1
        colourEquip1 = reference.colourEquip1
        teamId = reference.teamId
        modelOffset2 = templateReference.modelOffset2
        clientScriptData = reference.clientScriptData
        modifiedModelColors = reference.modifiedModelColors
        colourEquip2 = reference.colourEquip2
        modelOffset1 = templateReference.modelOffset1
        textureColour1 = reference.textureColour1
        value = 0
        modelZoom = templateReference.modelZoom
        options = arrayOfNulls(5)
        femaleWornModelId2 = reference.femaleWornModelId2
        if (reference.options != null) {
            options = reference.options.clone()
        }
    }

    /**
     * Transfer recolour definition.
     *
     * @param reference         the reference
     * @param templateReference the template reference
     */
    fun transferRecolourDefinition(
        reference: ItemDefinition,
        templateReference: ItemDefinition,
    ) {
        femaleWornModelId2 = reference.femaleWornModelId2
        options = arrayOfNulls(5)
        modelRotationY = templateReference.modelRotationY
        name = reference.name
        maleWornModelId1 = reference.maleWornModelId1
        modelOffset2 = templateReference.modelOffset2
        femaleWornModelId1 = reference.femaleWornModelId1
        maleWornModelId2 = reference.maleWornModelId2
        modelOffset1 = templateReference.modelOffset1
        unknownArray1 = reference.unknownArray1
        stackable = reference.stackable
        modelRotationX = templateReference.modelRotationX
        textureColour1 = reference.textureColour1
        colourEquip1 = reference.colourEquip1
        textureColour2 = reference.textureColour2
        modifiedModelColors = reference.modifiedModelColors
        modelZoom = templateReference.modelZoom
        colourEquip2 = reference.colourEquip2
        teamId = reference.teamId
        value = 0
        groundOptions = reference.groundOptions
        originalModelColors = reference.originalModelColors
        membersOnly = reference.membersOnly
        clientScriptData = reference.clientScriptData
        interfaceModelId = templateReference.interfaceModelId
        if (reference.options != null) {
            options = reference.options.clone()
        }
    }

    /**
     * Has requirement boolean.
     *
     * @param player  the player
     * @param wield   the wield
     * @param message the message
     * @return the boolean
     */
    fun hasRequirement(
        player: Player,
        wield: Boolean,
        message: Boolean,
    ): Boolean {
        val requirements = getConfiguration<Map<Int, Int>>(ItemConfigParser.REQUIREMENTS) ?: return true
        for (skill in requirements.keys) {
            if (skill < 0 || skill >= Skills.SKILL_NAME.size) {
                continue
            }
            val level = requirements[skill]!!
            if (player.getSkills().getStaticLevel(skill) < level) {
                if (message) {
                    val name = Skills.SKILL_NAME[skill]
                    player.packetDispatch.sendMessage(
                        "You need a" + (if (isPlusN(name)) "n " else " ") + name + " level of " + level + " to " +
                            (if (wield) "wear " else "use ") +
                            "this.",
                    )
                }
                return false
            }
        }
        return true
    }

    val isAllowedOnEntrana: Boolean
        /**
         * Is allowed on entrana boolean.
         *
         * @return the boolean
         */
        get() {
            if (permittedItems.contains(getId())) {
                return true
            }
            if (forbiddenItems.contains(getId())) {
                return false
            }
            if (equipSlot(getId()) == EquipmentSlot.AMMO) {
                return true
            }
            if (getName().lowercase(Locale.getDefault()).startsWith("ring") ||
                getName()
                    .lowercase(Locale.getDefault())
                    .startsWith("amulet")
            ) {
                return true
            }
            val bonuses = getConfiguration<IntArray>(ItemConfigParser.BONUS)
            return bonuses == null || Arrays.stream(bonuses).allMatch { x: Int -> x == 0 }
        }

    /**
     * Gets requirement.
     *
     * @param skillId the skill id
     * @return the requirement
     */
    fun getRequirement(skillId: Int): Int {
        val requirements = getConfiguration<Map<Int, Int>>(ItemConfigParser.REQUIREMENTS) ?: return 0
        val level = requirements[skillId]
        return level ?: 0
    }

    val renderAnimationId: Int
        /**
         * Gets render animation id.
         *
         * @return the render animation id
         */
        get() = getConfiguration(ItemConfigParser.RENDER_ANIM_ID, 1426)

    override fun getId(): Int = id

    override fun setId(id: Int) {
        this.id = id
    }

    /**
     * Gets interface model id.
     *
     * @return the interface model id
     */
    fun getInterfaceModelId(): Int = interfaceModelId

    /**
     * Sets interface model id.
     *
     * @param interfaceModelId the interface model id
     */
    fun setInterfaceModelId(interfaceModelId: Int) {
        this.interfaceModelId = interfaceModelId
    }

    override fun getName(): String = name

    override fun setName(name: String) {
        this.name = name
    }

    val isPlayerType: Boolean
        /**
         * Is player type boolean.
         *
         * @return the boolean
         */
        get() = itemType == 0

    /**
     * Gets model zoom.
     *
     * @return the model zoom
     */
    fun getModelZoom(): Int = modelZoom

    /**
     * Sets model zoom.
     *
     * @param modelZoom the model zoom
     */
    fun setModelZoom(modelZoom: Int) {
        this.modelZoom = modelZoom
    }

    /**
     * Gets model rotation x.
     *
     * @return the model rotation x
     */
    fun getModelRotationX(): Int = modelRotationX

    /**
     * Sets model rotation x.
     *
     * @param modelRotation1 the model rotation 1
     */
    fun setModelRotationX(modelRotation1: Int) {
        this.modelRotationX = modelRotation1
    }

    /**
     * Gets model rotation y.
     *
     * @return the model rotation y
     */
    fun getModelRotationY(): Int = modelRotationY

    /**
     * Sets model rotation y.
     *
     * @param modelRotation2 the model rotation 2
     */
    fun setModelRotationY(modelRotation2: Int) {
        this.modelRotationY = modelRotation2
    }

    /**
     * Is stackable boolean.
     *
     * @return the boolean
     */
    fun isStackable(): Boolean = stackable || !this.isUnnoted

    /**
     * Sets stackable.
     *
     * @param stackable the stackable
     */
    fun setStackable(stackable: Boolean) {
        this.stackable = stackable
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    fun getValue(): Int = value

    /**
     * Has shop currency value boolean.
     *
     * @param currency the currency
     * @return the boolean
     */
    fun hasShopCurrencyValue(currency: String?): Boolean = getHandlers().getOrDefault(currency, "0") !== "0"

    /**
     * Has shop currency value boolean.
     *
     * @param currency the currency
     * @return the boolean
     */
    fun hasShopCurrencyValue(currency: Int): Boolean =
        when (currency) {
            Items.COINS_995 -> isTradeable
            Items.TOKKUL_6529 -> hasShopCurrencyValue(ItemConfigParser.TOKKUL_PRICE)
            Items.ARCHERY_TICKET_1464 -> hasShopCurrencyValue(ItemConfigParser.ARCHERY_TICKET_PRICE)
            Items.CASTLE_WARS_TICKET_4067 -> hasShopCurrencyValue(ItemConfigParser.CASTLE_WARS_TICKET_PRICE)
            else -> false
        }

    val maxValue: Int
        /**
         * Gets max value.
         *
         * @return the max value
         */
        get() {
            if ((value * 1.05).toInt() <= 0) {
                return 1
            }
            return (value * 1.05).toInt()
        }

    val minValue: Int
        /**
         * Gets min value.
         *
         * @return the min value
         */
        get() {
            if ((value * .95).toInt() <= 0) {
                return 1
            }
            return (value * .95).toInt()
        }

    /**
     * Sets value.
     *
     * @param value the value
     */
    fun setValue(value: Int) {
        this.value = value
    }

    /**
     * Is members only boolean.
     *
     * @return the boolean
     */
    fun isMembersOnly(): Boolean = membersOnly

    /**
     * Sets members only.
     *
     * @param membersOnly the members only
     */
    fun setMembersOnly(membersOnly: Boolean) {
        this.membersOnly = membersOnly
    }

    /**
     * Gets male worn model id 1.
     *
     * @return the male worn model id 1
     */
    fun getMaleWornModelId1(): Int = maleWornModelId1

    /**
     * Sets male worn model id 1.
     *
     * @param maleWornModelId1 the male worn model id 1
     */
    fun setMaleWornModelId1(maleWornModelId1: Int) {
        this.maleWornModelId1 = maleWornModelId1
    }

    /**
     * Gets female worn model id 1.
     *
     * @return the female worn model id 1
     */
    fun getFemaleWornModelId1(): Int = femaleWornModelId1

    /**
     * Sets female worn model id 1.
     *
     * @param femaleWornModelId1 the female worn model id 1
     */
    fun setFemaleWornModelId1(femaleWornModelId1: Int) {
        this.femaleWornModelId1 = femaleWornModelId1
    }

    /**
     * Gets male worn model id 2.
     *
     * @return the male worn model id 2
     */
    fun getMaleWornModelId2(): Int = maleWornModelId2

    /**
     * Sets male worn model id 2.
     *
     * @param maleWornModelId2 the male worn model id 2
     */
    fun setMaleWornModelId2(maleWornModelId2: Int) {
        this.maleWornModelId2 = maleWornModelId2
    }

    /**
     * Gets female worn model id 2.
     *
     * @return the female worn model id 2
     */
    fun getFemaleWornModelId2(): Int = femaleWornModelId2

    /**
     * Sets female worn model id 2.
     *
     * @param femaleWornModelId2 the female worn model id 2
     */
    fun setFemaleWornModelId2(femaleWornModelId2: Int) {
        this.femaleWornModelId2 = femaleWornModelId2
    }

    val inventoryOptions: Array<String>
        /**
         * Get inventory options string [ ].
         *
         * @return the string [ ]
         */
        get() = options

    /**
     * Sets inventory options.
     *
     * @param inventoryOptions the inventory options
     */
    fun setInventoryOptions(inventoryOptions: Array<String?>?) {
        this.options = inventoryOptions
    }

    /**
     * Get original model colors short [ ].
     *
     * @return the short [ ]
     */
    fun getOriginalModelColors(): ShortArray? = originalModelColors

    /**
     * Sets original model colors.
     *
     * @param originalModelColors the original model colors
     */
    fun setOriginalModelColors(originalModelColors: ShortArray) {
        this.originalModelColors = originalModelColors
    }

    /**
     * Get modified model colors short [ ].
     *
     * @return the short [ ]
     */
    fun getModifiedModelColors(): ShortArray? = modifiedModelColors

    /**
     * Sets modified model colors.
     *
     * @param modifiedModelColors the modified model colors
     */
    fun setModifiedModelColors(modifiedModelColors: ShortArray) {
        this.modifiedModelColors = modifiedModelColors
    }

    /**
     * Get texture colour 1 short [ ].
     *
     * @return the short [ ]
     */
    fun getTextureColour1(): ShortArray? = textureColour1

    /**
     * Sets texture colour 1.
     *
     * @param textureColour1 the texture colour 1
     */
    fun setTextureColour1(textureColour1: ShortArray) {
        this.textureColour1 = textureColour1
    }

    /**
     * Get texture colour 2 short [ ].
     *
     * @return the short [ ]
     */
    fun getTextureColour2(): ShortArray? = textureColour2

    /**
     * Sets texture colour 2.
     *
     * @param textureColour2 the texture colour 2
     */
    fun setTextureColour2(textureColour2: ShortArray) {
        this.textureColour2 = textureColour2
    }

    /**
     * Get unknown array 1 byte [ ].
     *
     * @return the byte [ ]
     */
    fun getUnknownArray1(): ByteArray? = unknownArray1

    /**
     * Sets unknown array 1.
     *
     * @param unknownArray1 the unknown array 1
     */
    fun setUnknownArray1(unknownArray1: ByteArray) {
        this.unknownArray1 = unknownArray1
    }

    /**
     * Sets isUnnoted.
     *
     * @param unnoted the isUnnoted
     */
    fun setUnnoted(unnoted: Boolean) {
        this.isUnnoted = unnoted
    }

    /**
     * Gets colour equip 1.
     *
     * @return the colour equip 1
     */
    fun getColourEquip1(): Int = colourEquip1

    /**
     * Sets colour equip 1.
     *
     * @param colourEquip1 the colour equip 1
     */
    fun setColourEquip1(colourEquip1: Int) {
        this.colourEquip1 = colourEquip1
    }

    /**
     * Gets colour equip 2.
     *
     * @return the colour equip 2
     */
    fun getColourEquip2(): Int = colourEquip2

    /**
     * Sets colour equip 2.
     *
     * @param colourEquip2 the colour equip 2
     */
    fun setColourEquip2(colourEquip2: Int) {
        this.colourEquip2 = colourEquip2
    }

    /**
     * Gets note id.
     *
     * @return the note id
     */
    fun getNoteId(): Int = noteId

    /**
     * Sets note id.
     *
     * @param noteId the note id
     */
    fun setNoteId(noteId: Int) {
        this.noteId = noteId
    }

    /**
     * Gets note template id.
     *
     * @return the note template id
     */
    fun getNoteTemplateId(): Int = noteTemplateId

    /**
     * Sets note template id.
     *
     * @param noteTemplateId the note template id
     */
    fun setNoteTemplateId(noteTemplateId: Int) {
        this.noteTemplateId = noteTemplateId
    }

    /**
     * Get stack ids int [ ].
     *
     * @return the int [ ]
     */
    fun getStackIds(): IntArray? = stackIds

    /**
     * Sets stack ids.
     *
     * @param stackIds the stack ids
     */
    fun setStackIds(stackIds: IntArray?) {
        this.stackIds = stackIds
    }

    /**
     * Get stack amounts int [ ].
     *
     * @return the int [ ]
     */
    fun getStackAmounts(): IntArray? = stackAmounts

    /**
     * Sets stack amounts.
     *
     * @param stackAmounts the stack amounts
     */
    fun setStackAmounts(stackAmounts: IntArray) {
        this.stackAmounts = stackAmounts
    }

    /**
     * Gets team id.
     *
     * @return the team id
     */
    fun getTeamId(): Int = teamId

    /**
     * Sets team id.
     *
     * @param teamId the team id
     */
    fun setTeamId(teamId: Int) {
        this.teamId = teamId
    }

    /**
     * Gets lend id.
     *
     * @return the lend id
     */
    fun getLendId(): Int = lendId

    /**
     * Sets lend id.
     *
     * @param lendId the lend id
     */
    fun setLendId(lendId: Int) {
        this.lendId = lendId
    }

    /**
     * Gets lend template id.
     *
     * @return the lend template id
     */
    fun getLendTemplateId(): Int = lendTemplateId

    /**
     * Sets lend template id.
     *
     * @param lendTemplateId the lend template id
     */
    fun setLendTemplateId(lendTemplateId: Int) {
        this.lendTemplateId = lendTemplateId
    }

    /**
     * Gets equip id.
     *
     * @return the equip id
     */
    fun getEquipId(): Int = equipId

    /**
     * Sets equip id.
     *
     * @param equipId the equip id
     */
    fun setEquipId(equipId: Int) {
        this.equipId = equipId
    }

    /**
     * Gets client script data.
     *
     * @return the client script data
     */
    fun getClientScriptData(): HashMap<Int, Any>? = clientScriptData

    /**
     * Sets client script data.
     *
     * @param clientScriptData the client script data
     */
    fun setClientScriptData(clientScriptData: HashMap<Int, Any>?) {
        this.clientScriptData = clientScriptData
    }

    /**
     * Gets alchemy value.
     *
     * @param highAlchemy the high alchemy
     * @return the alchemy value
     */
    fun getAlchemyValue(highAlchemy: Boolean): Int {
        if (!isUnnoted && noteId > -1) {
            return forId(noteId).getAlchemyValue(highAlchemy)
        }
        if (highAlchemy) {
            return getConfiguration(ItemConfigParser.HIGH_ALCHEMY, round(value * 0.6).toInt())
        }
        return getConfiguration(ItemConfigParser.LOW_ALCHEMY, round(value * 0.4).toInt())
    }

    val isAlchemizable: Boolean
        /**
         * Is alchemizable boolean.
         *
         * @return the boolean
         */
        get() {
            if (!getConfiguration(ItemConfigParser.ALCHEMIZABLE, false)) {
                return false
            }
            return true
        }

    val isTradeable: Boolean
        /**
         * Is tradeable boolean.
         *
         * @return the boolean
         */
        get() {
            if (hasDestroyAction() && !getName().contains("impling jar")) {
                return false
            }
            if (!getConfiguration(ItemConfigParser.TRADEABLE, false)) {
                return false
            }
            return true
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

    /**
     * Has destroy action boolean.
     *
     * @return the boolean
     */
    fun hasDestroyAction(): Boolean = hasAction("destroy") || hasAction("dissolve")

    /**
     * Has wear action boolean.
     *
     * @return the boolean
     */
    fun hasWearAction(): Boolean {
        if (options == null) {
            return false
        }
        for (action in options) {
            if (action == null) {
                continue
            }
            if (action.equals("wield", ignoreCase = true) ||
                action.equals(
                    "wear",
                    ignoreCase = true,
                ) ||
                action.equals("equip", ignoreCase = true)
            ) {
                return true
            }
        }
        return false
    }

    /**
     * Has special bar boolean.
     *
     * @return the boolean
     */
    fun hasSpecialBar(): Boolean {
        if (clientScriptData == null) {
            return false
        }
        val specialBar = clientScriptData!![686]
        if (specialBar != null && specialBar is Int) {
            return specialBar == 1
        }
        return false
    }

    val questId: Int
        /**
         * Gets quest id.
         *
         * @return the quest id
         */
        get() {
            if (clientScriptData == null) {
                return -1
            }
            val questId = clientScriptData!![861]
            if (questId != null && questId is Int) {
                return questId
            }
            return -1
        }

    val archiveId: Int
        /**
         * Gets archive id.
         *
         * @return the archive id
         */
        get() = id ushr 8

    val fileId: Int
        /**
         * Gets file id.
         *
         * @return the file id
         */
        get() = 0xff and id

    /**
     * Instantiates a new Item definition.
     */
    init {
        groundOptions = arrayOf(null, null, "take", null, null)
        options = arrayOf(null, null, null, null, "drop")
    }

    /**
     * Has plugin boolean.
     *
     * @return the boolean
     */
    fun hasPlugin(): Boolean = itemPlugin != null

    var itemPlugin: ItemPlugin?
        /**
         * Gets item plugin.
         *
         * @return the item plugin
         */
        get() = getConfiguration<ItemPlugin>("wrapper", null)

        /**
         * Sets item plugin.
         *
         * @param plugin the plugin
         */
        set(plugin) {
            getHandlers()["wrapper"] = plugin
        }

    /**
     * Gets item type.
     *
     * @return the item type
     */
    fun getItemType(): Int = itemType

    /**
     * Sets item type.
     *
     * @param itemType the item type
     */
    fun setItemType(itemType: Int) {
        this.itemType = itemType
    }

    override fun getExamine(): String {
        examine = super.getExamine()
        if (isUnnoted) {
            examine = "Swap this note at any bank for the equivalent item."
        }
        return examine
    }

    companion object {
        /**
         * Gets definitions.
         *
         * @return the definitions
         */
        @JvmField
        val definitions: MutableMap<Int, ItemDefinition> = HashMap()
        private val OPTION_HANDLERS: MutableMap<String, OptionHandler?> = HashMap()

        /**
         * Parse.
         */
        fun parse() {
            for (itemId in 0 until getIndexCapacity(CacheIndex.ITEM_CONFIGURATION)) {
                val data = getData(CacheIndex.ITEM_CONFIGURATION, itemId ushr 8, itemId and 0xFF)

                if (data == null) {
                    definitions[itemId] = ItemDefinition()
                    continue
                }
                val def = parseDefinition(itemId, ByteBuffer.wrap(data))
                if (def == null) {
                    log(
                        ItemDefinition::class.java,
                        Log.ERR,
                        "Could not load item definitions for id $itemId - no definitions found!",
                    )
                    return
                }
                definitions[itemId] = def
            }
            defineTemplates()
        }

        /**
         * For id item definition.
         *
         * @param itemId the item id
         * @return the item definition
         */
        @JvmStatic
        fun forId(itemId: Int): ItemDefinition {
            val def = definitions[itemId]
            if (def != null) {
                return def
            }
            return ItemDefinition()
        }

        /**
         * Parse definition item definition.
         *
         * @param itemId the item id
         * @param buffer the buffer
         * @return the item definition
         */
        private fun parseDefinition(
            itemId: Int,
            buffer: ByteBuffer,
        ): ItemDefinition {
            val def = ItemDefinition()
            def.id = itemId
            while (true) {
                val opcode = buffer.get().toInt() and 0xFF
                if (opcode == 0) {
                    break
                } else if (opcode == 1) {
                    def.interfaceModelId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 2) {
                    def.name = getString(buffer)
                } else if (opcode == 3) {
                    def.handlers["examine"] = getString(buffer) // Examine
                    // info.
                } else if (opcode == 4) {
                    def.modelZoom = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 5) {
                    def.modelRotationX = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 6) {
                    def.modelRotationY = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 7) {
                    def.modelOffset1 = buffer.getShort().toInt() and 0xFFFF
                    if (def.modelOffset1 > 32767) def.modelOffset1 -= 65536
                } else if (opcode == 8) {
                    def.modelOffset2 = buffer.getShort().toInt() and 0xFFFF
                    if (def.modelOffset2 > 32767) {
                        def.modelOffset2 -= 65536
                    }
                } else if (opcode == 10) {
                    // buffer.getShort(); //10 is unused opcode.
                } else if (opcode == 11) {
                    def.stackable = true
                } else if (opcode == 12) {
                    def.value = ((buffer.get().toInt() and 0xFF) shl 24) + (
                        (
                            buffer
                                .get()
                                .toInt() and 0xFF
                        ) shl 16
                    ) + ((buffer.get().toInt() and 0xFF) shl 8) + (
                        buffer
                            .get()
                            .toInt() and 0xFF
                    )
                } else if (opcode == 16) {
                    def.membersOnly = true
                } else if (opcode == 23) {
                    def.maleWornModelId1 = buffer.getShort().toInt() and 0xFFFF
                    // buffer.get();
                } else if (opcode == 24) {
                    def.femaleWornModelId1 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 25) {
                    def.maleWornModelId2 = buffer.getShort().toInt() and 0xFFFF
                    // buffer.get();
                } else if (opcode == 26) {
                    def.femaleWornModelId2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode >= 30 && opcode < 35) {
                    def.groundOptions[opcode - 30] = getString(buffer)
                } else if (opcode >= 35 && opcode < 40) {
                    def.options[opcode - 35] = getString(buffer)
                } else if (opcode == 40) {
                    val length = buffer.get().toInt() and 0xFF
                    def.originalModelColors = ShortArray(length)
                    def.modifiedModelColors = ShortArray(length)
                    for (index in 0 until length) {
                        def.originalModelColors!![index] = buffer.getShort()
                        def.modifiedModelColors!![index] = buffer.getShort()
                    }
                } else if (opcode == 41) {
                    val length = buffer.get().toInt() and 0xFF
                    def.textureColour1 = ShortArray(length)
                    def.textureColour2 = ShortArray(length)
                    for (index in 0 until length) {
                        def.textureColour1!![index] = buffer.getShort()
                        def.textureColour2!![index] = buffer.getShort()
                    }
                } else if (opcode == 42) {
                    val length = buffer.get().toInt() and 0xFF
                    def.unknownArray1 = ByteArray(length)
                    for (index in 0 until length) def.unknownArray1!![index] = buffer.get()
                } else if (opcode == 65) {
                    def.isUnnoted = true
                } else if (opcode == 78) {
                    def.colourEquip1 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 79) {
                    def.colourEquip2 = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 90) {
                    def.maleWornModelId3 = buffer.getShort().toInt()
                } else if (opcode == 91) {
                    def.femaleWornModelId3 = buffer.getShort().toInt()
                } else if (opcode == 92) {
                    def.maleWornModelId4 = buffer.getShort().toInt()
                } else if (opcode == 93) {
                    def.femaleWornModelId4 = buffer.getShort().toInt()
                } else if (opcode == 95) {
                    buffer.getShort()
                } else if (opcode == 96) {
                    def.itemType = buffer.get().toInt()
                } else if (opcode == 97) {
                    def.noteId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 98) {
                    def.noteTemplateId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode >= 100 && opcode < 110) {
                    if (def.stackIds == null) {
                        def.stackIds = IntArray(10)
                        def.stackAmounts = IntArray(10)
                    }
                    def.stackIds!![opcode - 100] = buffer.getShort().toInt() and 0xFFFF
                    def.stackAmounts!![opcode - 100] = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 110) {
                    buffer.getShort()
                } else if (opcode == 111) {
                    buffer.getShort()
                } else if (opcode == 112) {
                    buffer.getShort()
                } else if (opcode == 113) {
                    buffer.get()
                } else if (opcode == 114) {
                    buffer.get()
                } else if (opcode == 115) {
                    def.teamId = buffer.get().toInt()
                } else if (opcode == 121) {
                    def.lendId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 122) {
                    def.lendTemplateId = buffer.getShort().toInt() and 0xFFFF
                } else if (opcode == 125) {
                    buffer.get()
                    buffer.get()
                    buffer.get()
                } else if (opcode == 126) {
                    buffer.get()
                    buffer.get()
                    buffer.get()
                } else if (opcode == 127) {
                    buffer.get()
                    buffer.getShort()
                } else if (opcode == 128) {
                    buffer.get()
                    buffer.getShort()
                } else if (opcode == 129) {
                    buffer.get()
                    buffer.getShort()
                } else if (opcode == 130) {
                    buffer.get()
                    buffer.getShort()
                } else if (opcode == 249) {
                    val length = buffer.get().toInt() and 0xFF
                    if (def.clientScriptData == null) {
                        def.clientScriptData = HashMap()
                    }
                    for (index in 0 until length) {
                        val string = (buffer.get().toInt() and 0xFF) == 1
                        val key = getMedium(buffer)
                        val value: Any = if (string) getString(buffer) else buffer.getInt()
                        def.clientScriptData!![key] = value
                    }
                } else {
                    break
                }
            }
            return def
        }

        /**
         * Define templates.
         */
        fun defineTemplates() {
            var equipId = 0
            for (i in 0 until getIndexCapacity(CacheIndex.ITEM_CONFIGURATION)) {
                val def = forId(i)
                if (def.noteTemplateId != -1) {
                    def.transferNoteDefinition(forId(def.noteId), forId(def.noteTemplateId))
                }
                if (def.lendTemplateId != -1) {
                    def.transferLendDefinition(forId(def.lendId), forId(def.lendTemplateId))
                }
                if (def.recolourTemplateId != -1) {
                    def.transferRecolourDefinition(forId(def.recolourId), forId(def.recolourTemplateId))
                }
                if (def != null && (def.maleWornModelId1 >= 0 || def.maleWornModelId2 >= 0)) {
                    def.equipId = equipId++
                }
            }
            forId(Items.DRAGON_CHAINBODY_2513).equipId = forId(Items.DRAGON_CHAINBODY_3140).equipId
        }

        /**
         * Can enter entrana boolean.
         *
         * @param player the player
         * @return the boolean
         */
        @JvmStatic
        fun canEnterEntrana(player: Player): Boolean {
            val container = arrayOf(player.inventory, player.equipment)
            for (c in container) {
                for (i in c.toArray()) {
                    if (i == null) {
                        continue
                    }
                    if (!i.definition.isAllowedOnEntrana) {
                        return false
                    }
                }
            }
            return true
        }

        private val permittedItems =
            arrayOf(
                Items.PENANCE_GLOVES_10553,
                Items.ICE_GLOVES_1580,
                Items.BOOTS_OF_LIGHTNESS_88,
                Items.CLIMBING_BOOTS_3105,
                Items.SPOTTED_CAPE_10069,
                Items.SPOTTIER_CAPE_10071,
                Items.SARADOMIN_CAPE_2412,
                Items.ZAMORAK_CAPE_2414,
                Items.GUTHIX_CAPE_2413,
                Items.SARADOMIN_CLOAK_10446,
                Items.ZAMORAK_CLOAK_10450,
                Items.GUTHIX_CLOAK_10448,
                Items.HOLY_BOOK_3840,
                Items.DAMAGED_BOOK_3839,
                Items.UNHOLY_BOOK_3842,
                Items.DAMAGED_BOOK_3841,
                Items.BOOK_OF_BALANCE_3844,
                Items.DAMAGED_BOOK_3843,
                Items.WIZARD_BOOTS_2579,
                Items.COMBAT_BRACELET1_11124,
                Items.COMBAT_BRACELET2_11122,
                Items.COMBAT_BRACELET3_11120,
                Items.COMBAT_BRACELET4_11118,
                Items.REGEN_BRACELET_11133,
                Items.WARLOCK_CLOAK_14081,
                Items.WARLOCK_LEGS_14077,
                Items.WARLOCK_TOP_14076,
                Items.MONKS_ROBE_542,
                Items.MONKS_ROBE_544,
                Items.HAM_SHIRT_4298,
                Items.HAM_ROBE_4300,
                Items.HAM_HOOD_4302,
                Items.HAM_CLOAK_4304,
                Items.HAM_LOGO_4306,
                Items.GLOVES_4308,
                Items.BOOTS_4310,
            )

        private val forbiddenItems =
            arrayOf(
                Items.DWARF_CANNON_SET_11967,
                Items.CANNON_BARRELS_10,
                Items.CANNON_BASE_6,
                Items.CANNON_STAND_8,
                Items.CANNON_FURNACE_12,
                Items.COOKING_GAUNTLETS_775,
                Items.CHAOS_GAUNTLETS_777,
                Items.GOLDSMITH_GAUNTLETS_776,
                Items.KARAMJA_GLOVES_1_11136,
                Items.KARAMJA_GLOVES_2_11138,
                Items.KARAMJA_GLOVES_3_11140,
                Items.VYREWATCH_TOP_9634,
                Items.VYREWATCH_LEGS_9636,
                Items.VYREWATCH_SHOES_9638,
            )

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
            if (def == null) {
                if (nodeId == 22937) {
                    log(
                        ItemDefinition::class.java,
                        Log.ERR,
                        "[ItemDefinition] No definition for item id $nodeId!",
                    )
                }
                return null
            }
            val handler = def.getConfiguration<OptionHandler>("option:$name")
            if (handler != null) {
                return handler
            }
            return OPTION_HANDLERS[name]
        }

        private val BONUS_NAMES =
            arrayOf(
                "Stab: ",
                "Slash: ",
                "Crush: ",
                "Magic: ",
                "Ranged: ",
                "Stab: ",
                "Slash: ",
                "Crush: ",
                "Magic: ",
                "Ranged: ",
                "Summoning: ",
                "Strength: ",
                "Prayer: ",
            )

        /**
         * Stats update.
         *
         * @param player the player
         */
        fun statsUpdate(player: Player) {
            if (!player.getAttribute("equip_stats_open", false)) {
                return
            }
            var index = 0
            val bonuses = player.properties.bonuses
            for (i in 36..49) {
                if (i == 47) {
                    continue
                }
                val bonus = bonuses[index]
                val bonusValue = if (bonus > -1) ("+$bonus") else bonus.toString()
                player.packetDispatch.sendString(BONUS_NAMES[index++] + bonusValue, Components.EQUIP_SCREEN2_667, i)
            }
            player.packetDispatch.sendString("Attack bonus", Components.EQUIP_SCREEN2_667, 34)
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
    }
}
