package content.region.kandarin.ardougne.east.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Zenesha dialogue.
 */
@Initializable
class ZeneshaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.SUSPICIOUS, "Hello there! I sell plate armour. Are you interested?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I may be interested.", "Sorry, I'm not interested.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.ZENESHA_589)
                }
                2 -> player("Sorry, I'm not interested.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ZeneshaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ZENESHA_589)
}
