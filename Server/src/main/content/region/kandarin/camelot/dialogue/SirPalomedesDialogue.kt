package content.region.kandarin.camelot.dialogue

import core.api.getQuestStage
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents Sir Gawain dialogue.
 */
@Initializable
class SirPalomedesDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    npcl(FaceAnim.NEUTRAL, "Hello there adventurer, what do you want of me?")
                    val questStage = getQuestStage(player!!, Quests.MERLINS_CRYSTAL)
                    stage = when (questStage) {
                        0 -> 1
                        10 -> 10
                        20, 30 -> 20
                        in 40..100 -> 40
                        else -> END_DIALOGUE
                    }
                }
                1 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on finding a quest.").also { stage++ }
                2 -> npcl(FaceAnim.NEUTRAL, "I do not know of any myself... but it would perhaps be worth your while asking the King if he has any tasks for you.").also { stage = END_DIALOGUE }
                10 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on breaking that Crystal Merlin's trapped in.").also { stage++ }
                11 -> npcl(FaceAnim.NEUTRAL, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
                20 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on breaking into Mordred's fort.").also { stage++ }
                21 -> npcl(FaceAnim.NEUTRAL, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
                40 -> playerl(FaceAnim.NEUTRAL, "I'd like some advice on finding Excalibur.").also { stage = 11 }
            }
        } else {
            when (stage) {
                0 -> npcl(FaceAnim.HALF_GUILTY, "Hello there adventurer, what do you want of me?").also {
                    if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 || isQuestComplete(player!!, Quests.HOLY_GRAIL)) {
                        stage = 1
                    } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                        stage = 10
                    }
                }
                1 -> npcl(FaceAnim.HALF_GUILTY, "After your help freeing Merlin, my help is the least I can offer as a man of honour.").also { stage++ }
                2 -> playerl(FaceAnim.HALF_GUILTY, "Nothing right now, but I'll bear it in mind. Thanks.").also { stage = END_DIALOGUE }
                10 -> playerl(FaceAnim.FRIENDLY, "I'd like some advice on finding the Grail.").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "Sorry, I cannot help you with that.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SirPalomedesDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_PALOMEDES_3787)
}
