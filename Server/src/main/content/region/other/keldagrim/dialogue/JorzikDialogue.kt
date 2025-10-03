package content.region.other.keldagrim.dialogue

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
 * Represents the Jorzik dialogue.
 */
@Initializable
class JorzikDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npc(FaceAnim.OLD_DEFAULT, "Do you want to trade?").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "What are you selling?", 2),
                Topic(FaceAnim.FRIENDLY, "No, thanks.", 5),
            )
            2 -> npcl(FaceAnim.OLD_DEFAULT, "The finest smiths from all over Gielinor come here to work, and I buy the fruit of their craft. Armour made from the strongest metals!").also { stage++ }
            3 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Let's have a look, then.", 4),
                Topic(FaceAnim.FRIENDLY, "No, thanks.", 5),
            )
            4 -> {
                end()
                openNpcShop(player, NPCs.JORZIK_2565)
            }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "You just don't appreciate the beauty of fine metalwork.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = JorzikDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.JORZIK_2565)
}
