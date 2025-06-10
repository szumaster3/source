package content.region.kandarin.handlers.ardougne

import content.global.handlers.item.withnpc.grownCatItemIds
import content.global.handlers.item.withnpc.kittenItemIds
import core.api.replaceSlot
import core.api.sendItemDialogue
import core.api.sendNPCDialogue
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Handles sell the cats to civilians.
 */
@Initializable
class CivilianPlugin : UseWithHandler(*grownCatItemIds, *kittenItemIds) {

    companion object {
        private val civilians = intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        civilians.forEach { npcId ->
            addHandler(npcId, NPC_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        val item = event.usedItem
        val npc = event.usedWith.asNpc() ?: return false

        if (!civilians.contains(npc.id)) {
            return false
        }

        return when {
            grownCatItemIds.contains(item.id) -> {
                player.familiarManager.removeDetails(item.idHash)
                sendItemDialogue(player, Items.DEATH_RUNE_560, "You hand over the cat.<br>You are given 100 Death Runes.")
                replaceSlot(player, item.slot, Item(Items.DEATH_RUNE_560, 100))
                true
            }
            kittenItemIds.contains(item.id) -> {
                sendNPCDialogue(player, npc.id, "That kitten isn't big enough; come back when it's bigger.")
                true
            }
            else -> false
        }
    }
}
