package content.region.kandarin.guild.fishing.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Roachey dialogue.
 */
@Initializable
class RoacheyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc("Would you like to buy some Fishing equipment or sell", "some fish?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thank you.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Yes, please.").also { stage++ }
                2 -> end()
            }
            2 -> end().also { openNpcShop(player, NPCs.ROACHEY_592) }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RoacheyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ROACHEY_592)
}
