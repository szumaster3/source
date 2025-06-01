package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import content.region.kandarin.quest.itwatchtower.handlers.WatchtowerUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs

class BattlementDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_860)
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT,"Oi! Where do you think you are going? You are for", "the cooking pot!").also { stage++ }
            1 -> if(!getAttribute(player!!, GameAttributes.WATCHTOWER_ROCK_CAKE, false)) {
                showTopics(
                    Topic("But I am a friend to ogres...", 2),
                    Topic("Not if I can help it.", 5)
                )
            } else {
                npcl(FaceAnim.OLD_DEFAULT, "Stop, creature! Oh it's you. Well, what have you got for us, then?").also { stage = 13 }
            }
            2 -> npc(FaceAnim.OLD_DEFAULT, "Prove it to us with a gift. Get us something from the", "market.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Like what?").also { stage++ }
            4 -> npc(FaceAnim.OLD_DEFAULT, "Surprise us...").also {
                setAttribute(player!!, GameAttributes.WATCHTOWER_ROCK_CAKE, true)
                stage = END_DIALOGUE
            }
            5 -> npc(FaceAnim.OLD_DEFAULT, "You can help by being tonight's dinner, or", "you can go away.").also { stage++ }
            6 -> npc(FaceAnim.OLD_NEUTRAL, "Now, which shall it be?").also { stage++ }
            7 -> showTopics(
                Topic("Okay, okay, I'm going.", 8),
                Topic("I tire of ogres, prepare to die!", 12)
            )
            8 -> npc(FaceAnim.OLD_DEFAULT, "Back to whence you came!").also { stage++ }
            9 -> end().also { WatchtowerUtils.handleGatePassage(player!!, Location.create(2546, 3065), openGate = false) }
            10 -> npc(FaceAnim.OLD_DEFAULT, "Well, well, look at this. My favourite: rock cake! Okay,", "we will let it through.").also { stage++ }
            11 -> {
                endFile()
                val destination = player!!.location.transform(2,0,0)
                forceMove(player!!, player!!.location, destination, 0, 90, null, Animations.CLIMB_OVER_THING_5038)
                sendMessage(player!!, "You climb over the low wall.")
            }
            12 -> {
                end()
                npc(FaceAnim.OLD_DEFAULT, "Grrrrr!")

                val p = player
                val n = npc

                if (p != null && n != null) {
                    val localNpc = findLocalNPC(p, n.id)
                    localNpc?.attack(p)
                }
            }
            13 -> {
                if(!inInventory(player!!, Items.ROCK_CAKE_2379)) {
                    player(FaceAnim.HALF_GUILTY, "I didn't bring anything.").also { stage++ }
                } else {
                    player(FaceAnim.HALF_ASKING, "How about this?").also { stage = 11 }
                }
            }
            14 -> npc(FaceAnim.OLD_DEFAULT, "Didn't bring anything? In that case shove off!").also { stage = 9 }
        }
    }
}