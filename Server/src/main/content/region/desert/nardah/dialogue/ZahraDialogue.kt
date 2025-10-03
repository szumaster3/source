package content.region.desert.nardah.dialogue

import core.api.hasRequirement
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Zahra dialogue.
 */
@Initializable
class ZahraDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!hasRequirement(player, Quests.SPIRITS_OF_THE_ELID)) {
            player(FaceAnim.FRIENDLY, "Good day to you.")
        } else {
            playerl(FaceAnim.FRIENDLY, "How's life now the curse has been lifted?").also { stage = 5 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
            1 -> player("You don't look too happy.").also { stage++ }
            2 -> npc("True. We've not fallen on the best of times here.").also { stage++ }
            3 -> player("Any way that I can help?").also { stage++ }
            4 -> npcl(FaceAnim.HALF_GUILTY, "Possibly. I'd go talk to Awusah the Mayor of Nardah. He's in the big house on the east side of the town square.").also { stage = END_DIALOGUE }
            5 -> npcl(FaceAnim.HALF_GUILTY, "Much better thanks to you. We're all very impressed.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ZahraDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ZAHRA_3036)
}
