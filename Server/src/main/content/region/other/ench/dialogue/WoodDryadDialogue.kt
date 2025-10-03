package content.region.other.ench.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Wood Dryad dialogue.
 */
@Initializable
class WoodDryadDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.LAUGH, "Hi, why do you have twigs growing out of you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Heehee, what a strange question; that's because I'm a dryad.").also { stage++ }
            1 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = WoodDryadDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.WOOD_DRYAD_4441)
}
