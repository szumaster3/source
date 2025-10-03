package content.region.kandarin.ardougne.east.dialogue.market

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class GemMerchantDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Here, look at my lovely gems.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                end()
                openNpcShop(player, NPCs.GEM_MERCHANT_570)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GemMerchantDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GEM_MERCHANT_570)
}
