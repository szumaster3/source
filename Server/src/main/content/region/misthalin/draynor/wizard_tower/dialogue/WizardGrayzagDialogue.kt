package content.region.misthalin.draynor.wizard_tower.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class WizardGrayzagDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Not now, I'm trying to concentrate on a", "very difficult spell!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.WIZARD_GRAYZAG_707)
}
