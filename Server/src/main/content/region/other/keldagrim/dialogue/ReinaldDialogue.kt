package content.region.other.keldagrim.dialogue

import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.NPCs

/**
 * Represents the Reinald dialogue.
 */
@Initializable
class ReinaldDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Hello, human! Would you like to browse", "my little shop of bracelets?").also { stage++ }
            1 -> options("Yes, please!", "No, thanks.").also { stage++ }
            2 -> when (buttonId) {
                1 -> {
                    end()
                    openInterface(player, Components.REINALD_SMITHING_EMPORIUM_593)
                }

                2 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ReinaldDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.REINALD_2194)
}
