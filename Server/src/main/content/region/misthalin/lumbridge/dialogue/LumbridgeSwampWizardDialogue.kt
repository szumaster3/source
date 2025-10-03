package content.region.misthalin.lumbridge.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Wizard (Lumbridge swamp) dialogue.
 */
@Initializable
class LumbridgeSwampWizardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Why are all of you standing around here?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Hahaha you dare talk to a mighty wizard such as", "myself? I bet you can't even cast windstrike yet", "amateur!").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "...You're an idiot.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = LumbridgeSwampWizardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.WIZARD_652)
}
