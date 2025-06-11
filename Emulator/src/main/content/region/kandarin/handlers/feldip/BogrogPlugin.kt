package content.region.kandarin.handlers.feldip

import content.region.kandarin.handlers.feldip.BogrogPouch.handle
import core.api.sendItemSelect
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * Handles the bogrog npc.
 *
 * @author Vexia
 */
@Initializable
class BogrogPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.BOGROG_4472).handlers["option:swap"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "swap" -> openSwap(player)
        }
        return true
    }

    /**
     * Opens the swap interface.
     *
     * @param player the player.
     */
    private fun openSwap(player: Player) {
        if (player.getSkills().getStaticLevel(Skills.SUMMONING) < 21) {
            player.sendMessage("You need a Summoning level of at least 21 in order to do that.")
            return
        } else {
            sendItemSelect(player, "Value", "Swap 1", "Swap 5", "Swap 10", "Swap X") { slot: Int?, index: Int? ->
                handle(player, index!!, slot!!)
                Unit
            }
        }
    }
}
