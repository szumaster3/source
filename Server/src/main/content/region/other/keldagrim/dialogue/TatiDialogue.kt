package content.region.other.keldagrim.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Tati dialogue.
 */
@Initializable
class TatiDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_ANGRY1, "What'you want?").also { stage++ }
            1 -> showTopics(
                Topic(FaceAnim.FRIENDLY, "Do you have any pickaxes?", 2, true),
                Topic(FaceAnim.FRIENDLY, "Do you have any quests?", 7, true),
                Topic(FaceAnim.FRIENDLY, "Nothing really.", 14),
            )
            2 -> playerl(FaceAnim.FRIENDLY, "Do you-").also { stage++ }
            3 -> npcl(FaceAnim.OLD_ANGRY1, "What? Speak up, I can't hear you!").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Uhm... I'm just looking for some pickaxes! Do you have any?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_ANGRY1, "Do I have any pickaxes? Do I? Of course I do, this is a pickaxe shop, isn't it!").also { stage++ }
            6 -> {
                end()
                openNpcShop(player, NPCs.TATI_2160)
            }
            7 -> playerl(FaceAnim.FRIENDLY, "Do you-").also { stage++ }
            8 -> npcl(FaceAnim.OLD_ANGRY1, "What? Stop mumbling!").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "I want a quest!").also { stage++ }
            10 -> npcl(FaceAnim.OLD_ANGRY1, "I don't have any lousy quests... I've got someone who's helping me already!").also { stage++ }
            11 -> npcl(FaceAnim.OLD_ANGRY1, "Well, I say helping... he takes his merry time to do his chores, my son does.").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Then perhaps I can help in some way?").also { stage++ }
            13 -> npcl(FaceAnim.OLD_ANGRY1, "Pfft, I doubt it... maybe when my son fouls up again, but not now.").also { stage = END_DIALOGUE }
            14 -> npcl(FaceAnim.OLD_ANGRY1, "Then clear off!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TatiDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TATI_2160)
}
