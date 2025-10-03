package content.region.misthalin.lumbridge.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Sir Vant dialogue.
 */
@Initializable
class SirVantDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        sendDialogue(player, "Sir Vant seems too busy to talk.").also { stage = END_DIALOGUE }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_VANT_7942)
}
