package content.region.asgarnia.burthope.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Anton dialogue.
 */
@Initializable
class AntonDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.ASKING, "Ahhh, hello there. How can I help?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.NEUTRAL, "Looks like you have a good selection of weapons around here...")
                stage++
            }
            1 -> {
                npcl(FaceAnim.FRIENDLY, "Indeed so, specially imported from the finest smiths around the lands, take a look at my wares.")
                stage++
            }
            2 -> {
                end()
                openNpcShop(player, NPCs.ANTON_4295)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ANTON_4295)
}
