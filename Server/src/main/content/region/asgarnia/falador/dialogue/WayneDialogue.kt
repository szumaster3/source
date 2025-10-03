package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Wayne dialogue.
 */
@Initializable
class WayneDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Welcome to Wayne's Chains. Do you wanna buy or", "sell some chain mail?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.WAYNE_581)
                }
                2 -> end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WAYNE_581)
}
