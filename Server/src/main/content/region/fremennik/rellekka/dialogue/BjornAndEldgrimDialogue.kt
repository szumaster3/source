package content.region.fremennik.rellekka.dialogue

import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Bjorn and Eldgrim dialogue.
 */
@Initializable
class BjornAndEldgrimDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.DRUNK, "Hey! Itsh you again! Whatshyerfashe!").also { stage = 10 }
        } else {
            player(FaceAnim.FRIENDLY, "Hello there.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.DRUNK, "Hey! scheck it out! Itsh an outerlandub! Yer shud go shpeak to the chieftain!").also { stage++ }
            1 -> player(FaceAnim.ASKING, "The who?").also { stage++ }
            2 -> npcl(FaceAnim.DRUNK, "That guy over there by that stuff! (hic) Yeh, abshoultely! He's da bosh!").also { stage = 16 }
            10 -> player(FaceAnim.ASKING, "${player.name}?").also { stage++ }
            11 -> npcl(FaceAnim.DRUNK, "Nah nah nah, not them, the other one, whatshyerfashe!").also { stage++ }
            12 -> player(FaceAnim.ASKING, "${player.getAttribute("fremennikname", "fremmyname") ?: "outerlander"}?").also { stage++ }
            13 -> npc(FaceAnim.DRUNK, "Thatsh what I said diddle I?").also { stage++ }
            14 -> player(FaceAnim.ASKING, "Um.... okay. I'll leave you to your drinking.").also { stage++ }
            15 -> npc(FaceAnim.DRUNK, "Thanksh pal! You're alright!").also { stage = 16 }
            16 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BjornAndEldgrimDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BJORN_1284, NPCs.ELDGRIM_1285)
}
