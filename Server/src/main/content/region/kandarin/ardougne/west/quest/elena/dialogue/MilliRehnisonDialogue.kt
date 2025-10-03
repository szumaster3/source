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
 * Represents the Milli Rehnison dialogue.
 *
 * # Relations
 * - [Plague City][content.region.kandarin.ardougne.west.quest.elena.PlagueCity]
 */
@Initializable
class MilliRehnisonDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) == 9) {
            playerl(FaceAnim.FRIENDLY, "Hello. Your parents say you saw what happened to Elena...")
        } else {
            npcl(FaceAnim.CHILD_NORMAL, "Any luck finding Elena yet?")
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            9 -> when (stage) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "*sniff* Yes I was near the south east corner when I saw Elena walking by. I was about to run to greet her when some men jumped out. They shoved a sack over her head and dragged her into a building.").also { stage++ }
                1 -> playerl(FaceAnim.FRIENDLY, "Which building?").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "It was the boarded up building with no windows in the south east corner of West Ardougne.").also { stage++ }
                3 -> {
                    end()
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 11)
                    stage = END_DIALOGUE
                }
            }
            in 10..98 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Not yet...").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "I wish you luck, she did a lot for us.").also { stage = END_DIALOGUE }
            }
            in 99..100 -> when (stage) {
                1 -> playerl(FaceAnim.FRIENDLY, "Yes, she is safe at home now.").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "That's good to hear, she helped us a lot.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MILLI_REHNISON_724)
}
