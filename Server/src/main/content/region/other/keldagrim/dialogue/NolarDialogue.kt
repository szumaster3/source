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
 * Represents the Nolar dialogue.
 */
@Initializable
class NolarDialogue(player: Player? = null) : Dialogue(player) {


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npc(FaceAnim.OLD_DEFAULT, "I have a wide variety of crafting tools on offer,", "care to take a look?").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Yes please!", 2),
                Topic(FaceAnim.FRIENDLY, "No thanks.", END_DIALOGUE),
            )
            2 -> {
                end()
                openNpcShop(player, NPCs.NOLAR_2158)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = NolarDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.NOLAR_2158)
}
