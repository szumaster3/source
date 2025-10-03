package content.region.kandarin.ardougne.east.quest.ikov.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

@Initializable
class LekaDialogue(player: Player? = null) : Dialogue(player) {

    override fun newInstance(player: Player?): Dialogue = LekaDialogue(player)

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.ANGRY, "Who dares to enter the Temple of Ikov!").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.ANGRY, "A mighty hero!", 2),
                Topic(FaceAnim.FRIENDLY, "A humble pilgrim.", 4),
            )
            2 -> npcl(FaceAnim.ANGRY, "Pathetic fool! Prepare to die!").also { stage++ }
            3 -> {
                npc!!.attack(player)
                end()
            }
            4 -> npcl(FaceAnim.ANGRY, "I haven't seen a pilgrim for thousands of years!").also { stage++ }
            5 -> npcl(FaceAnim.ANGRY, "Temple is closed!").also { stage = END_DIALOGUE }
        }
        return false
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FIRE_WARRIOR_OF_LESARKUS_277)
}
