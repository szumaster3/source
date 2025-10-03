package content.region.fremennik.rellekka.dialogue

import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.openNpcShop
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Fur Trader at Fremennik dialogue.
 */
@Initializable
class FremennikFurTraderDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.ANNOYED, "I don't sell to outlanders.").also { stage = END_DIALOGUE }
        } else {
            npcl(FaceAnim.FRIENDLY, "Welcome back, ${FremennikTrials.getFremennikName(player)}. Have you seen the furs I have today?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                end()
                openNpcShop(player, NPCs.FUR_TRADER_1316)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FremennikFurTraderDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FUR_TRADER_1316)
}
