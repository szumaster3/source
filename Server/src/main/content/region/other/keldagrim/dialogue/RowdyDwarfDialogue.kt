package content.region.other.keldagrim.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Rowdy Dwarf dialogue.
 */
@Initializable
class RowdyDwarfDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_DRUNK_LEFT, "Get OUT of my WAY!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RowdyDwarfDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ROWDY_DWARF_2187)
}
