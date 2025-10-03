package content.global.plugin.iface

import content.global.skill.magic.SpellUtils
import core.api.*
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

/**
 * Represents the bolt enchant interface.
 * @author Emperor, SonicForce41
 */
class BoltEnchantInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.XBOWS_ENCHANT_BOLT_432) { player, _, _, buttonID, _, itemID ->
            val bolts = Bolts.boltMap[buttonID] ?: return@on true
            val staff = SpellUtils.usingStaff(player, itemID)
            closeInterface(player)

            if (getStatLevel(player, Skills.MAGIC) < bolts.level) {
                sendMessage(player, "You need a level of " + bolts.level + " to cast this spell.")
                return@on true
            }

            for (item in bolts.runes) {
                if (!inInventory(player, item.id) && !staff) {
                    sendMessage(player, "You do not have enough runes to cast this spell.")
                    return@on true
                }
            }

            val amount = amountInInventory(player, bolts.bolt)
            if (amount < 1) {
                sendMessage(player, "You don't have any bolts to enchant.")
                return@on true
            }
            val add = Item(bolts.enchanted)
            if (amount > 10 && !hasSpaceFor(player, add)) {
                sendMessage(player, "Not enough space in your inventory.")
                return@on true
            }
            visualize(player, Animations.CRAFT_ENCHANT_4462, 759)
            queueScript(player, 1, QueueStrength.SOFT) {
                var enchant = amount
                if (enchant > 10) {
                    enchant = 10
                }
                add.amount = enchant
                removeItem(player, Item(bolts.bolt, enchant))
                addItem(player, bolts.enchanted, enchant)
                for (item in bolts.runes) {
                    if (!inInventory(player, item.id) && !staff) {
                        sendMessage(player, "You do not have enough " + item.name + "s to cast this spell.")
                        return@queueScript stopExecuting(player)
                    }
                    if (!staff) {
                        removeItem(player, item)
                    }
                }
                rewardXP(player, Skills.MAGIC, bolts.exp)
                playAudio(player, Sounds.ENCHANTED_TIPPING_2921)
                sendMessage(player, "The magic of the runes coaxes out the true nature of the gem tips.")
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

    /**
     * Enum representing different types of bolts that can be enchanted.
     *
     * @property button The button id in the interface.
     * @property bolt The base bolt item id.
     * @property level Required Magic level to enchant.
     * @property runes Required runes and their quantities.
     * @property exp Experience gained from enchanting.
     * @property enchanted The enchanted bolt item id.
     */
    enum class Bolts(val button: Int, val bolt: Int, val level: Int, val runes: Array<Item>, val exp: Double, val enchanted: Int) {
        OPAL(14, Items.OPAL_BOLTS_879, 4, arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 2)), 9.0, Items.OPAL_BOLTS_E_9236),
        SAPPHIRE(29, Items.SAPPHIRE_BOLTS_9337, 7, arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.MIND_RUNE.id, 1), Item(Runes.WATER_RUNE.id, 1)), 17.0, Items.SAPPHIRE_BOLTS_E_9240),
        JADE(18, Items.JADE_BOLTS_9335, 14, arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 2)), 19.0, Items.JADE_BOLTS_E_9237),
        PEARL(22, Items.PEARL_BOLTS_880, 24, arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.WATER_RUNE.id, 2)), 29.0, Items.PEARL_BOLTS_E_9238),
        EMERALD(32, Items.EMERALD_BOLTS_9338, 27, arrayOf(Item(Runes.NATURE_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.AIR_RUNE.id, 3)), 37.0, Items.EMERALD_BOLTS_E_9241),
        RED_TOPAZ(26, Items.TOPAZ_BOLTS_9336, 29, arrayOf(Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 2)), 33.0, Items.TOPAZ_BOLTS_E_9239),
        RUBY(35, Items.RUBY_BOLTS_9339, 49, arrayOf(Item(Runes.BLOOD_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 5)), 59.0, Items.RUBY_BOLTS_E_9242),
        DIAMOND(38, Items.DIAMOND_BOLTS_9340, 57, arrayOf(Item(Runes.LAW_RUNE.id, 2), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 10)), 67.0, Items.DIAMOND_BOLTS_E_9243),
        DRAGONSTONE(41, Items.DRAGON_BOLTS_9341, 68, arrayOf(Item(Runes.SOUL_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.EARTH_RUNE.id, 15)), 78.0, Items.DRAGON_BOLTS_E_9244),
        ONYX(44, Items.ONYX_BOLTS_9342, 87, arrayOf(Item(Runes.DEATH_RUNE.id, 1), Item(Runes.COSMIC_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 20)), 97.0, Items.ONYX_BOLTS_E_9245),
        ;

        companion object {
            /**
             * Mapping from button to corresponding bolt enchantment.
             */
            val boltMap = HashMap<Int, Bolts>()

            init {
                for (bolts in values()) {
                    boltMap[bolts.button] = bolts
                }
            }
        }
    }
}
