package content.region.misthalin.varrock.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Pox (cat) dialogue.
 */
@Initializable
class PoxDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi cat!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_FRIENDLY, "Miaooow!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = PoxDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.POX_2943)
}
