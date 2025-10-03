package content.region.misthalin.varrock.quest.phoenixgang.dialogue

import content.region.misthalin.varrock.quest.phoenixgang.ShieldofArrav
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Jonny the beard dialogue.
 *
 * # Relations
 * - [Shield of Arrav][content.region.misthalin.varrock.quest.phoenixgang.ShieldofArrav]
 */
class JonnyTheBeardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (ShieldofArrav.isPhoenixMission(player)) {
            sendMessage(player, "Johnny the beard is not interested in talking.")
            end()
            return true
        }
        npc(FaceAnim.HALF_GUILTY, "Will you buy me a beer?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "No, I don't think I will.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = JonnyTheBeardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.JONNY_THE_BEARD_645)
}
