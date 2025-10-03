package content.region.island.apeatoll.dialogue

import core.api.addItem
import core.api.openNpcShop
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Daga dialogue.
 */
@Initializable
class DagaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Would you like to buy or sell some scimitars?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thanks.", "Do you have any Dragon Scimitars in stock?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "Yes, please.").also { stage = 10 }
                2 -> player(FaceAnim.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
                3 -> player(FaceAnim.HALF_ASKING, "Do you have any Dragon Scimitars in stock?").also { stage = 30 }
            }

            10 -> {
                end()
                openNpcShop(player, NPCs.DAGA_1434)
            }
            30 -> npcl(FaceAnim.OLD_DEFAULT, "It just so happens I recently got a fresh delivery, do you want to buy one?").also { stage++ }
            31 -> options("Yes, please.", "No, thanks.").also { stage++ }
            32 -> when (buttonId) {
                1 -> {
                    end()
                    if (!removeItem(player, Item(Items.COINS_995, 100000))) {
                        npcl(FaceAnim.OLD_NORMAL, "Sorry but you don't have enough to buy one, at the moment it costs 100,000 gold coins.")
                    } else {
                        addItem(player, Items.DRAGON_SCIMITAR_4587, 1)
                    }
                }

                2 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DagaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DAGA_1434)
}
