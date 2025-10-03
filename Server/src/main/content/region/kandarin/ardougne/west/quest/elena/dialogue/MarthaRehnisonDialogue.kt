package content.region.kandarin.ardougne.west.quest.elena.dialogue

import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Martha Rehnison dialogue.
 *
 * # Relations
 * - [Plague City][content.region.kandarin.ardougne.west.quest.elena.PlagueCity]
 */
@Initializable
class MarthaRehnisonDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) == 9) {
            playerl(FaceAnim.NEUTRAL, "Hi, I hear a woman called Elena is staying here.")
        } else {
            npcl(FaceAnim.FRIENDLY, "Any luck finding Elena yet?")
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            9 -> when (stage) {
                0 -> npcl(FaceAnim.FRIENDLY, "Yes she was staying here, but slightly over a week ago she was getting ready to go back.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "However she never managed to leave. My daughter Milli was playing near the west wall when she saw some shadowy figures jump out and grab her. Milli is upstairs if you wish to speak to her.").also { stage = END_DIALOGUE }
            }

            in 10..98 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Not yet...").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "I wish you luck, she did a lot for us.").also { stage = END_DIALOGUE }
            }

            in 99..100 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Yes, she is safe at home now.").also { stage++ }
                1 -> npcl(FaceAnim.FRIENDLY, "That's good to hear, she helped us a lot.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MARTHA_REHNISON_722)
}
