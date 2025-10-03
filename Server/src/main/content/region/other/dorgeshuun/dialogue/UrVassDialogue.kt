package content.region.other.dorgeshuun.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Ur Vass dialogue.
 */
@Initializable
class UrVassDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "Ah. ${player.username}, I presume?").also { stage++ }
            1 -> player("That's me.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Pleased to meet you. On behalf of the Dorgeshuun Council, I should say how grateful we are that you saved our city.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = UrVassDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.UR_VASS_5771)
}
