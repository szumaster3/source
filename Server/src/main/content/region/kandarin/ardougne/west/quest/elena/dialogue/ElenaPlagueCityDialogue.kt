package content.region.kandarin.ardougne.west.quest.elena.dialogue

import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Elena dialogue.
 *
 * # Relations
 * - [Plague City][content.region.kandarin.ardougne.west.quest.elena.PlagueCity]
 */
@Initializable
class ElenaPlagueCityDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) >= 16) {
            playerl(FaceAnim.FRIENDLY, "Hi, you're free to go! Your kidnappers don't seem to be about right now.").also { stage = 1 }
        } else {
            npcl(FaceAnim.FRIENDLY, "Go and see my father, I'll make sure he adequately rewards you. Now I'd better leave while I still can.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage) {
            1 -> npcl(FaceAnim.FRIENDLY, "Thank you, being kidnapped was so inconvenient. I was on my way back to East Ardougne with some samples, I want to see if I can diagnose a cure for this plague.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "Well you can leave via the manhole near the gate.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Go and see my father, I'll make sure he adequately rewards you. Now I'd better leave while I still can.").also { stage++ }
            4 -> {
                end()
                setQuestStage(player!!, Quests.PLAGUE_CITY, 99)
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ELENA_3215)
}
