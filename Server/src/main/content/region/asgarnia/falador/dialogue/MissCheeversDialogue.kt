package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.rd.plugin.MissCheeversDialogueFile
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Miss Cheevers dialogue.
 */
@Initializable
class MissCheeversDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MissCheeversDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MISS_CHEEVERS_2288)
}
