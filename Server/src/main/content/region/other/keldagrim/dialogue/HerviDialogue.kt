package content.region.other.keldagrim.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Hervi dialogue.
 */
@Initializable
class HerviDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Greetings, human... can I interest you in", "some fine gems?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Show me the wares!", "No thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Show me the wares!").also { stage++ }
                2 -> player("No thanks.").also { stage = END_DIALOGUE }
            }
            2 -> end().also { openNpcShop(player, NPCs.HERVI_2157) }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HerviDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HERVI_2157)
}
