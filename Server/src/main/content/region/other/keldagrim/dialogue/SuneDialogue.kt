package content.region.other.keldagrim.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Sune dialogue.
 */
@Initializable
class SuneDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_ANGRY1, "Can you leave me alone please? I'm trying to get a bit of rest.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SuneDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SUNE_2191)
}
