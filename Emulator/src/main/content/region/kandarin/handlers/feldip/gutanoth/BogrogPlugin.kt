package content.region.kandarin.handlers.feldip.gutanoth

import content.global.skill.summoning.SummoningPouch
import content.global.skill.summoning.SummoningScroll
import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.FaceAnim
import core.game.dialogue.InputType
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import kotlin.math.ceil
import kotlin.math.floor

class BogrogPlugin : InteractionListener {

    override fun defineListeners() {
        on(NPCs.BOGROG_4472, IntType.NPC, "swap", "talk-to") { player, node ->
            val npc = node.asNpc()
            when (getUsedOption(player)) {
                "swap" -> openSwap(player)
                else -> openDialogue(player, npc)
            }
            true
        }
    }

    private fun openDialogue(player: Player, npc: NPC) {
        dialogue(player) {
            npc(npc, FaceAnim.CHILD_NORMAL, "Hey, yooman, what you wanting?")
            options(null, "Can I buy some summoning supplies?", "Are you interested in buying pouch pouches or scrolls?") { selected ->
                when (selected) {
                    1 -> buySupplies(player, npc)
                    2 -> swapPouchesDialogue(player, npc)
                }
            }
        }
    }

    private fun buySupplies(player: Player, npc: NPC) {
        dialogue(player) {
            player(FaceAnim.NEUTRAL, "Can I buy some summoning supplies?")
            npc(npc, FaceAnim.CHILD_NORMAL, "Hur, hur, hur! Yooman's gotta buy lotsa stuff if yooman", "wants ta train good!")
            end { openNpcShop(player, npc.id) }
        }
    }

    private fun swapPouchesDialogue(player: Player, npc: NPC) {
        dialogue(player) {
            player(FaceAnim.NEUTRAL, "Are you interested in buying pouch pouches or scrolls?")
            npc(npc, FaceAnim.CHILD_NORMAL, "Des other ogre's stealin' Bogrog's stock. Gimmie pouches", "and scrolls and yooman gets da shardies.")
            player(FaceAnim.NEUTRAL, "Ok.")
            end { openSwap(player) }
        }
    }

    companion object {
        private const val OP_VALUE = 0
        private const val OP_SWAP_1 = 1
        private const val OP_SWAP_5 = 2
        private const val OP_SWAP_10 = 3
        private const val OP_SWAP_X = 4
        private const val SPIRIT_SHARD = Items.SPIRIT_SHARDS_12183

        @JvmStatic
        fun openSwap(player: Player) {
            if (getStatLevel(player, Skills.SUMMONING) < 21) {
                sendMessage(player, "You need a Summoning level of at least 21 in order to do that.")
                return
            }

            sendItemSelect(player, "Value", "Swap 1", "Swap 5", "Swap 10", "Swap X") { slot, index ->
                if (slot != null && index != null) {
                    handleSwap(player, index, slot)
                }
            }
        }

        private fun handleSwap(player: Player, optionIndex: Int, slot: Int): Boolean {
            val item = player.inventory.get(slot) ?: return false
            return when (optionIndex) {
                OP_VALUE -> sendValue(item.id, player)
                OP_SWAP_1 -> swap(player, 1, item.id)
                OP_SWAP_5 -> swap(player, 5, item.id)
                OP_SWAP_10 -> swap(player, 10, item.id)
                OP_SWAP_X -> {
                    sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                        swap(player, value as Int, item.id)
                    }
                    true
                }
                else -> false
            }
        }

        private fun swap(player: Player, amount: Int, itemID: Int): Boolean {
            var amt = amount
            val value = getValue(itemID)
            if (value == 0.0) return false

            val inInventory = player.inventory.getAmount(itemID)
            if (amount > inInventory) amt = inInventory

            player.inventory.remove(Item(itemID, amt))
            player.inventory.add(Item(SPIRIT_SHARD, floor(value * amt).toInt()))
            return true
        }

        private fun sendValue(itemID: Int, player: Player): Boolean {
            val value = getValue(itemID)
            if (value == 0.0) return false

            sendMessage(player, "Bogrog will give you ${floor(value).toInt()} shards for that.")
            return true
        }

        private fun getValue(itemID: Int): Double {
            val pouch = sequenceOf(
                SummoningPouch.get(itemID),
                SummoningPouch.get(Item(itemID).noteChange),
                SummoningPouch.get(SummoningScroll.forItemId(itemID)?.pouch ?: -1)
            ).firstOrNull { it != null } ?: return 0.0

            val isScroll = SummoningScroll.forItemId(itemID) != null
            val shardQuantity = pouch.items.last().amount * 0.7
            return if (isScroll) shardQuantity / 20.0 else ceil(shardQuantity)
        }
    }
}