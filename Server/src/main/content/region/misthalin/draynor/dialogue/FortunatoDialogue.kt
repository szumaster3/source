package content.region.misthalin.draynor.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Fortunato dialogue.
 */
@Initializable
class FortunatoDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.ASKING, "Can I help you at all?").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Yes, what are you selling?", 2),
                Topic(FaceAnim.FRIENDLY, "Not at the moment", 3),
            )
            2 -> {
                end()
                openNpcShop(player, NPCs.FORTUNATO_3671)
            }
            3 -> npcl(FaceAnim.ANGRY, "Then move along, you filthy ragamuffin, I have customers to serve!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FortunatoDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FORTUNATO_3671)
}
