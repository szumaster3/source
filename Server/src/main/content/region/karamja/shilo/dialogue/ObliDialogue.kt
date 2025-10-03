package content.region.karamja.shilo.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Obli dialogue.
 */
@Initializable
class ObliDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Welcome to Obli's General Store Bwana!", "Would you like to see my items?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please!", "No, but thanks for the offer.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "Yes, please.").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY, "No, but thanks for the offer.").also { stage = END_DIALOGUE }
            }

            2 -> {
                end()
                openNpcShop(player, NPCs.OBLI_516)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OBLI_516)
}
