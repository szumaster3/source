package content.region.kandarin.quest.itwatchtower.dialogue

import content.region.kandarin.quest.itwatchtower.handlers.WatchtowerUtils
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class BattlementDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_859)
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT,"Oi! Where do you think you are going? You are for", "the cooking pot!").also { stage++ }
            1 -> showTopics(
                Topic("But I am a friend to ogres...", 2),
                Topic("Not if I can help it.", 5)
            )
            2 -> npc(FaceAnim.OLD_DEFAULT, "Prove it to us with a gift. Get us something from the", "market.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Like what?").also { stage++ }
            4 -> npc(FaceAnim.OLD_DEFAULT, "Surprise us...").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.OLD_DEFAULT, "You can help by being tonight's dinner, or", "you can go away.").also { stage++ }
            6 -> npc(FaceAnim.OLD_NEUTRAL, "Now, which shall it be?").also { stage++ }
            7 -> showTopics(
                Topic("Okay, okay, I'm going.", 8),
                Topic("I tire of ogres, prepare to die!", 3)
            )
            8 -> npc(FaceAnim.OLD_DEFAULT, "Back to whence you came!").also { stage++ }
            9 -> end().also { WatchtowerUtils.handleGatePassage(player!!, Location.create(2546, 3065), openGate = false) }
        }
    }
}