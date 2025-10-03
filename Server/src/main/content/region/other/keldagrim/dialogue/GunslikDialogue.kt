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
 * Represents the Gunslik dialogue.
 */
@Initializable
class GunslikDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_DEFAULT, "What can I interest you in? We have something of everything here!").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Oh good!", 2),
                Topic(FaceAnim.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
            )
            2 -> {
                end()
                openNpcShop(player, NPCs.GUNSLIK_2154)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GunslikDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GUNSLIK_2154)
}
