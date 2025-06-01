package content.region.kandarin.quest.itwatchtower.dialogue

import content.region.kandarin.quest.itwatchtower.handlers.WatchtowerUtils
import core.api.inInventory
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Ogre city guards dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class OgreCityGuardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if(getQuestStage(player, Quests.WATCHTOWER) in 10..100) {
            openDialogue(player, OgreCityGateDialogue())
        } else {
            npcl(FaceAnim.OLD_DEFAULT, "Stop, creature!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_ANGRY1, "Only ogres and their friends allowed in this city.").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Show me a sign of companionship, like a lost relic", "or somefing, and you may pass.").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "I don't have anything.").also { stage = 3 }
            3 -> npc(FaceAnim.OLD_DEFAULT,"Why have you returned with no proof of companionship?", "Back to whence you came!").also { stage++ }
            4 -> end().also { WatchtowerUtils.handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false) }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OgreCityGuardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OGRE_GUARD_859)
}

class OgreCityGateDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_859)
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Well, what proof of friendship did you bring?").also { stage++ }
            1 -> if(inInventory(player!!, Items.OGRE_RELIC_2372)) {
                player(FaceAnim.NEUTRAL, "I have a relic from a chieftain.").also { stage++ }
            } else {
                player(FaceAnim.HALF_GUILTY, "I don't have anything.").also { stage = 4 }
            }

            2 -> npc(FaceAnim.OLD_DEFAULT,"It's got the statue of Dalgroth. Welcome to Gu'Tanoth,", "friend of the ogres.").also { stage++ }
            3 -> end().also { WatchtowerUtils.handleGatePassage(player!!, Location.create(2503, 3062, 0), openGate = true) }
            4 -> npc(FaceAnim.OLD_DEFAULT,"Why have you returned with no proof of companionship?", "Back to whence you came!").also { stage++ }
            5 -> end().also { WatchtowerUtils.handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false) }
        }
    }
}