package content.region.tirannwn.lletya.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Goreu dialogue.
 */
@Initializable
class GoreuDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Good day, can I help you?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "No thanks I'm just watching the world go by.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Well I can think of no better place to do it, it is beautiful here is it not?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "Indeed it is.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOREU_2363)
    }
}