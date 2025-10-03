package content.region.other.zanaris.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Fairy Chef dialogue.
 */
@Initializable
class FairyChefDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "'Ello, sugar. I'm afraid I can't gossip right now, I've got a cake in the oven.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = FairyChefDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FAIRY_CHEF_3322, NPCs.FAIRY_CHEF_3323)
}
