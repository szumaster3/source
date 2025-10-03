package content.region.asgarnia.falador.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Lakki Dwarf dialogue.
 */
@Initializable
class LakkiDwarfDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "I'm sorry, I can't talk right now.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LAKKI_THE_DELIVERY_DWARF_7722, 7728)
}
