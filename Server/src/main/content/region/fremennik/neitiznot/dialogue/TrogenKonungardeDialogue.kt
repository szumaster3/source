package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class TrogenKonungardeDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.NEUTRAL, "I'm guarding the king, I cannot speak.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "Sorry.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TrogenKonungardeDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TROGEN_KONUNGARDE_5519)
}
