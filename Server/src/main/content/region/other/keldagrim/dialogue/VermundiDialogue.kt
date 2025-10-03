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
 * Represents the Vermundi dialogue.
 */
@Initializable
class VermundiDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Welcome to my clothes stall, can I help you", "with anything?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, what clothes do you have in stock?", "No, I'm just browsing.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Yes, what clothes do you have in stock?").also { stage++ }
                2 -> player("No, I'm just browsing.").also { stage = END_DIALOGUE }
            }

            2 -> npc(FaceAnim.OLD_NORMAL, "Not a lot, I'm afraid, most of what I produce goes to my", "sister. Her shop is in Keldagrim-West.").also { stage++ }
            3 -> player("Well, show me what you do have then.").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, NPCs.VERMUNDI_2162)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = VermundiDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.VERMUNDI_2162)
}
