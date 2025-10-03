package content.region.fremennik.piratecove.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the BettyB Bopin dialogue.
 */
@Initializable
class BettyBBopinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Dude!", "Duuuuuuude!", "Dude!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = BettyBBopinDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BETTY_BBOPPIN_4553)
}
