package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Drogo dialogue.
 */
@Initializable
class DrogoDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "'Ello. Welcome to my Mining shop, friend.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Do you want to trade?", "Hello, shorty.", "Why don't you ever restock ores and bars?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "Do you want to trade?").also { stage++ }
                2 -> player(FaceAnim.FRIENDLY, "Hello, shorty.").also { stage = 3 }
                3 -> player(FaceAnim.FRIENDLY, "Why don't you ever restock ores and bars?").also { stage = 4 }
            }
            2 -> {
                end()
                openNpcShop(player, NPCs.DROGO_DWARF_579)
            }
            3 -> npc(FaceAnim.OLD_ANGRY1, "I may be short, but at least I've got manners.").also { stage = 5 }
            4 -> npc(FaceAnim.OLD_DEFAULT, "The only ores and bars I sell are those sold to me.").also { stage = 5 }
            5 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DrogoDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DROGO_DWARF_579)
}
