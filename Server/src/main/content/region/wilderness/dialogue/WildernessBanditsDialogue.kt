package content.region.wilderness.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Wilderness Bandits dialogue.
 */
@Initializable
class WildernessBanditsDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        sendDialogue(player, "${npc.name} is not interested in talking.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = WildernessBanditsDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BLACK_HEATHER_202, NPCs.DONNY_THE_LAD_203, NPCs.SPEEDY_KEITH_204)
}
