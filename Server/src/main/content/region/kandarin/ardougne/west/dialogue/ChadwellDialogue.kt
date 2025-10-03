package content.region.kandarin.ardougne.west.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Chadwell dialogue.
 */
@Initializable
class ChadwellDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Good day. What can I get you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Let's see what you've got.", "Nothing thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Let's see what you've got.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "Nothing thanks.").also { stage = 3 }
                }

            2 -> {
                end()
                openNpcShop(player, NPCs.CHADWELL_971)
            }

            3 -> npc(FaceAnim.FRIENDLY, "Okay then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ChadwellDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CHADWELL_971)
}
