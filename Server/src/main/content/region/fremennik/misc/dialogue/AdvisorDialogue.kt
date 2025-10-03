package content.region.fremennik.misc.dialogue

import core.api.freeSlots
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import shared.consts.NPCs

class AdvisorDialogue : DialogueFile() {

    var level = 2

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ADVISOR_GHRIM_1375)
        when (stage) {
            0 -> {
                val diary = player!!.achievementDiaryManager.getDiary(DiaryType.FREMENNIK)!!
                stage = when {
                    diary.isComplete(level, true) && !Diary.hasClaimedLevelRewards(player, DiaryType.FREMENNIK, level) -> 10
                    Diary.canReplaceReward(player, DiaryType.FREMENNIK, level) -> 12
                    else -> 13
                }
                player(FaceAnim.FRIENDLY, "About the Achievement Diary...")
            }

            10 -> player("I've completed all of the hard tasks in my Fremennik", "Achievement Diary.").also { stage++ }
            11 -> if (freeSlots(player!!) < 1) {
                end()
                npc(FaceAnim.FRIENDLY, "Although, you'll have to come back when you've the space", "to take them.").also { stage = END_DIALOGUE }
            } else {
                npc("There you go. They're powerful things in their own", "way, so take care to study them and find all the ways", "they can help you.").also {
                    Diary.flagRewarded(player!!, DiaryType.FREMENNIK, 2)
                    stage = 21
                }
            }
            12 -> if (freeSlots(player!!) < 1) {
                end()
                npc(FaceAnim.FRIENDLY, "Although, you'll have to come back when you've the space", "to take them.").also {
                    stage = END_DIALOGUE
                }
            } else {
                npc("So you have. I have some special boots I've kept aside", "for you.").also {
                    Diary.grantReplacement(player!!, DiaryType.FREMENNIK, 2)
                    stage = 21
                }
            }
            13 -> options("What is the Achievement Diary?", "What are the rewards?", "How do I claim the rewards?", "See you later.").also { stage++ }
            14 -> when (buttonID) {
                1 -> playerl(FaceAnim.ASKING, "What is the Achievement Diary?").also { stage = 15 }
                2 -> playerl(FaceAnim.ASKING, "What are the rewards?").also { stage = 18 }
                3 -> playerl(FaceAnim.ASKING, "How do I claim the rewards?").also { stage = 20 }
                4 -> playerl(FaceAnim.ASKING, "See you later.").also { stage = END_DIALOGUE }
            }
            15 -> npc(FaceAnim.FRIENDLY, "Very well; the Achievements Diary is a collection of", "Tasks you may wish to complete while adventuring", "world of ${GameWorld.settings!!.name}. You can earn special rewards", "for completing Tasks.").also { stage++ }
            16 -> npc(FaceAnim.NEUTRAL, "Some also give items that will help to complete", "other Tasks, and many count as progress towards", "the set for the area they're in.").also { stage++ }
            17 -> npc(FaceAnim.NEUTRAL, "Some of the Fremennik set's Tasks are simple, some will", "require certain skill levels, and some might require", "quests to be started or completed.").also { stage = 13 }
            18 -> npc(FaceAnim.NEUTRAL, "For completing the Fremennik diaries, you are presented", "with a pair of sea boots. These boots will become increasingly", "useful with each difficulty level of the set that you complete.").also { stage++ }
            19 -> npc(FaceAnim.NEUTRAL, "When you are presented with your rewards,", "you will be told of their uses.").also { stage = 13 }
            20 -> npc("To claim your Fremennik Diaries rewards", "speak to Council Workman south of the Rellekka,", "Advisor Ghrim on Miscellania, or myself.").also { stage = 13 }
            21 -> playerl(FaceAnim.HAPPY, "Thanks!").also { stage = 13 }
            100 -> end()
        }
    }
}
