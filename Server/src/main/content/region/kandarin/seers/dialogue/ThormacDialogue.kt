package content.region.kandarin.seers.dialogue

import content.region.kandarin.seers.quest.scorpcatcher.dialogue.ThormacRequestDialogue
import core.api.openDialogue
import core.api.openInterface
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Thormac dialogue.
 *
 * # Relations
 * - [Scorpion Catcher quest][content.region.kandarin.seers.quest.scorpcatcher.ScorpionCatcherPlugin]
 * - Battlestaff enchanting.
 */
@Initializable
class ThormacDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {

        /*
         * Represents the headband, when a player owns, receives a discount.
         */
        val hasHeadband = DiaryManager(player).hasHeadband()

        when (stage) {

            /*
             * Scorpion Catcher dialogue.
             */
            0 -> if (!isQuestComplete(player, Quests.SCORPION_CATCHER)) {
                openDialogue(player, ThormacRequestDialogue())
            } else {
                npcl(FaceAnim.NEUTRAL, "Thank you for rescuing my scorpions.").also { stage++ }
            }
            1 -> options("That's okay.", "You said you'd enchant my battlestaff for me.").also { stage++ }
            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.NEUTRAL, "That's okay.").also { stage = END_DIALOGUE }
                2 -> playerl(FaceAnim.NEUTRAL, "You said you'd enchant my battlestaff for me.").also { stage++ }
            }
            3 -> npc(FaceAnim.NEUTRAL, "Yes, although it'll cost you " + (if (hasHeadband) "27,000" else "40,000") + " coins for the", "materials. What kind of staff did you want enchanting?").also { stage++ }
            4 -> {
                end()
                openInterface(player!!, Components.STAFF_ENCHANT_332)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ThormacDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.THORMAC_389)
}
