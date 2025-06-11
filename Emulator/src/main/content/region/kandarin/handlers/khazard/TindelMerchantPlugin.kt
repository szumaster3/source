package content.region.kandarin.handlers.khazard

import content.data.items.BrokenItem
import content.region.kandarin.dialogue.khazard.TindelMerchantDialogue
import core.api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.FaceAnim
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import org.rs.consts.Sounds

/**
 * Handles the exchange of rusty weapons.
 */
@Initializable
class TindelMerchantPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(TINDEL).handlers["option:talk-to"] = this
        NPCDefinition.forId(TINDEL).handlers["option:Give-Sword"] = this

        SceneryDefinition.forId(ANTIQUE_SHOP_STALL).handlers["option:ring-bell"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val optionLower = option.lowercase()

        return when (node.id) {
            ANTIQUE_SHOP_STALL -> {
                if (optionLower == "ring-bell") {
                    playGlobalAudio(player.location, BELL_SOUND)
                    sendDialogue(player, "You ring for attention.")
                    runTask(player, 1) {
                        openDialogue(player, TindelMerchantDialogue())
                    }
                    true
                } else false
            }

            TINDEL -> {
                when (optionLower) {
                    "talk-to" -> {
                        openDialogue(player, TindelMerchantDialogue())
                        true
                    }

                    "give-sword" -> exchangeRustyWeapon(player)
                    else -> false
                }
            }

            else -> false
        }
    }

    override fun getDestination(n: Node?, node: Node?): Location? {
        val targetId = when (node) {
            is Node -> node.asNpc().id
            else -> return null
        }
        return if (targetId == TINDEL) {
            Location(2678, 3152, 0)
        } else null
    }

    companion object {
        private const val TINDEL = NPCs.TINDEL_MARCHANT_1799
        private const val BELL_SOUND = Sounds.BELL_2192
        private const val ANTIQUE_SHOP_STALL = Scenery.ANTIQUES_SHOP_STALL_5831
        private const val RUSTY_SWORD = Items.RUSTY_SWORD_686
        private const val RUSTY_SCIMITAR = Items.RUSTY_SCIMITAR_6721
        private const val FAKE_COINS = Items.COINS_8896

        fun success(player: Player, skill: Int): Boolean {
            val level = player.getSkills().getLevel(skill).toDouble()
            val minChance = 50.0
            val maxChance = 100.0
            val maxLevel = 99.0

            val successChance = minChance + (level - 1) * (maxChance - minChance) / (maxLevel - 1)
            val roll = RandomFunction.random(0.0, 100.0)
            return roll < successChance
        }

        fun exchangeRustyWeapon(player: Player): Boolean {
            val rustyItemId = when {
                inInventory(player, RUSTY_SWORD) -> RUSTY_SWORD
                inInventory(player, RUSTY_SCIMITAR) -> RUSTY_SCIMITAR
                else -> {
                    sendNPCDialogue(
                        player,
                        TINDEL,
                        "Sorry my friend, but you don't seem to have any swords that need to be identified.",
                        FaceAnim.HALF_GUILTY
                    )
                    return false
                }
            }

            if (!inInventory(player, Items.COINS_995, 100)) {
                sendNPCDialogue(player, TINDEL, "Sorry, you don't have enough coins.", FaceAnim.HALF_GUILTY)
                return false
            }

            sendDoubleItemDialogue(
                player,
                rustyItemId,
                FAKE_COINS,
                "You hand Tindel 100 coins plus the ${getItemName(rustyItemId).lowercase()}."
            )

            addDialogueAction(player) { _, _ ->
                val equipmentType = when (rustyItemId) {
                    RUSTY_SWORD -> BrokenItem.EquipmentType.SWORDS
                    RUSTY_SCIMITAR -> BrokenItem.EquipmentType.SCIMITARS
                    else -> null
                } ?: return@addDialogueAction

                val rustyItem = Item(rustyItemId, 1)
                val repairedItem = BrokenItem.getRepair(equipmentType)
                val itemName = getItemName(repairedItem!!.id).lowercase()

                removeItem(player, Item(Items.COINS_995, 100))
                removeItem(player, rustyItem)

                val success = success(player, core.game.node.entity.skill.Skills.SMITHING)
                if (success) {
                    sendItemDialogue(player, repairedItem.id, "Tindel gives you a $itemName.")
                    addItem(player, repairedItem.id, 1)
                } else {
                    sendNPCDialogue(
                        player,
                        TINDEL,
                        "Sorry my friend, but the item wasn't worth anything. I've disposed of it for you.",
                        FaceAnim.HALF_GUILTY
                    )
                }
            }

            return true
        }
    }

}
