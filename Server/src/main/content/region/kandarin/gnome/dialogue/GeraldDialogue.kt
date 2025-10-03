package content.region.kandarin.gnome.dialogue

import core.api.animate
import core.api.getQuestStage
import core.api.runTask
import core.api.stopWalk
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Gerald dialogue.
 */
@Initializable
class GeraldDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.WATERFALL_QUEST) >= 1) {
            player("Hello.")
            stage = 5
            return true
        }

        player("Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Good day to you traveller, are you here to fish or just looking around?").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "I've caught some beauties down here.").also { stage++ }
            2 -> player("Really?").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "The last one was this big!").also { stage++ }
            4 -> {
                end()
                stopWalk(player)
                runTask(player, 1) {
                    animate(npc, Animations.STEP_BACK_WAVE_1240)
                }
            }
            5 -> npcl(FaceAnim.NEUTRAL, "Hello traveller.").also { stage++ }
            6 -> npcl(FaceAnim.HALF_ASKING, "Are you here to fish or to hunt for treasure?").also { stage++ }
            7 -> player("Why do you say that?").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL, "Adventurers pass through here every week, they never find anything though.").also { stage = 9 }
            9 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GeraldDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GERALD_303)
}