package content.region.fremennik.misc.quest.misc.dialogue

import content.region.fremennik.misc.dialogue.AdvisorDialogue
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class AdvisorGhrimDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.HALF_GUILTY, "Greetings, ${if (player.isMale) "Sir" else "Madam"}").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("How do I make peace with Etceteria?", "About the Achievement Diary...").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.ANGRY, "How do I make peace with Etceteria?").also { stage++ }
                    2 -> openDialogue(player, AdvisorDialogue()).also { stage = 100 }
                }

            2 ->
                npcl(FaceAnim.HALF_GUILTY, "You should go talk to Queen Sigrid of Etceteria.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ADVISOR_GHRIM_1375)
}
