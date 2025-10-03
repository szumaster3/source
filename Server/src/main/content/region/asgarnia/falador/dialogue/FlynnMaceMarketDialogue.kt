package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Flynn dialogue.
 */
@Initializable
class FlynnMaceMarketDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Hello. Do you want to buy or sell any maces?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Well, I'll have a look, at least.", "No, thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> {
                    end()
                    openNpcShop(player, NPCs.FLYNN_580)
                }

                2 -> player(FaceAnim.NEUTRAL, "No, thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FlynnMaceMarketDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FLYNN_580)
}
