package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Graphics
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
            4 -> end().also { handleGatePassage(player!!, Location.create(2546, 3065), openGate = false) }
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
                player(FaceAnim.NEUTRAL, "I have a relic fro a chieftain.").also { stage++ }
            } else {
                player(FaceAnim.HALF_GUILTY, "I don't have anything.").also { stage = 4 }
            }

            2 -> npc(FaceAnim.OLD_DEFAULT,"It's got the statue of Dalgroth. Welcome to Gu'Tanoth,", "friend of the ogres.").also { stage++ }
            3 -> end().also { handleGatePassage(player!!, Location.create(2503, 3062, 0), openGate = true) }
            4 -> npc(FaceAnim.OLD_DEFAULT,"Why have you returned with no proof of companionship?", "Back to whence you came!").also { stage++ }
            5 -> end().also {handleGatePassage(player!!, Location.create(2546, 3065), openGate = false) }
        }
    }
}

private fun handleGatePassage(player : Player, loc: Location, openGate: Boolean) {
    lock(player, 4)
    teleport(player, loc, TeleportManager.TeleportType.INSTANT)

    if (openGate) {
        var scenery = getScenery(2504, 3063, 0)
        scenery?.asScenery()?.let { DoorActionHandler.handleAutowalkDoor(player, it) }
    } else {
        sendMessage(player, "The guard pushes you back down the hill.")
        impact(player, 3, ImpactHandler.HitsplatType.NORMAL)
        sendGraphics(Graphics.STUN_BIRDIES_ABOVE_HEAD_80, player.location)
        sendChat(player, "Urrrrrgh!")
    }
}