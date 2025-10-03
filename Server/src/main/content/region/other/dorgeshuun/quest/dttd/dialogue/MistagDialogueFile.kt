package content.region.other.dorgeshuun.quest.dttd.dialogue

import core.api.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Mistag dialogue.
 *
 * Relations:
 * - Death to the Dorgeshuun quest
 */
class MistagDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MISTAG_2084)
        val questStage = getQuestStage(player!!, Quests.DEATH_TO_THE_DORGESHUUN)
        when {
            (questStage == 0) -> {
                when (stage) {
                    0 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Surface-dwellers have been visiting the mines for some",
                            "time now, but no Dorgeshuun has yet visited the",
                            "surface.",
                        ).also { stage++ }

                    1 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "We are curious about the surface, and we are also",
                            "worried about the dangers it poses. We know that",
                            "Lumbridge is friendly but we are worried that the HAM",
                            "group may be plotting against us.",
                        ).also { stage++ }

                    2 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "We are planning to send an agent to the surface and",
                            "we would like you to act as a guide.",
                        ).also { stage++ }

                    3 -> options("I'll act as a guide.", "I'm too busy.").also { stage++ }
                    4 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "I'll act as a guide.").also { stage = 6 }
                            2 -> playerl(FaceAnim.NEUTRAL, "I'm too busy.").also { stage = 5 }
                        }

                    5 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Oh dear. Our agent is ready, but the Council will not let this go ahead without a guide. If you reconsider please let me know!",
                        ).also { stage = END_DIALOGUE }

                    6 -> npc(FaceAnim.OLD_HAPPY, "Thank you!").also { stage++ }
                    7 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "In order to get into the HAM base undetected you will",
                            "both need to go in disguise. You should get two full sets",
                            "of HAM robes. Once you have them, our agent will",
                            "meet you in the cellar of Lumbridge castle.",
                        ).also {
                            stage = END_DIALOGUE
                        }
                }
            }
        }
    }
}
