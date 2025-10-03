package content.region.desert.nardah.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Seddu dialogue.
 */
@Initializable
class SedduDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("I buy and sell adventurer's equipment, do you want to trade?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please", "No, thanks").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.SEDDU_3038)
                }

                2 -> player("No, thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SedduDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SEDDU_3038)
}
