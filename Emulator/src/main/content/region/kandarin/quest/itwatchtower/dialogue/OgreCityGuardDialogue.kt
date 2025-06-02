package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.*

/**
 * Represents the Ogre Guards dialogues.
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
            npc(FaceAnim.OLD_DEFAULT, "Stop, creature! Only ogres and their friends allowed in", "this city. Show me a sign of companionship, like a lost", "relic or somefing, and you may pass.").also {
                handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false)
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OgreCityGuardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OGRE_GUARD_859)
}

/**
 * Represents the Ogre Guards near battlement dialogue (extension).
 */
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
            9 -> end().also { handleGatePassage(player!!, Location.create(2546, 3065), openGate = false) }
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
                animate(npc!!, 359)
                impact(player!!, 3, ImpactHandler.HitsplatType.NORMAL)
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

/**
 * Represents the Ogre Guards near gates dialogue (extension).
 */
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
            3 -> {
                end()
                handleGatePassage(player!!, Location.create(2503, 3062, 0), openGate = true)
                setAttribute(player!!, GameAttributes.WATCHTOWER_GATE_UNLOCK, true)
            }
            4 -> npc(FaceAnim.OLD_DEFAULT,"Why have you returned with no proof of companionship?", "Back to whence you came!").also { stage++ }
            5 -> end().also { handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false) }
        }
    }
}

/**
 * Handles gate passage.
 *
 * @param openGate boolean.
 */
private fun handleGatePassage(player: Player, loc: Location, openGate: Boolean) {
    teleport(player, loc, TeleportManager.TeleportType.INSTANT)

    if (openGate) {
        val scenery = getScenery(2504, 3063, 0)
        scenery?.asScenery()?.let { DoorActionHandler.handleAutowalkDoor(player, it) }
    } else {
        lock(player, 9)
        openInterface(player, Components.FADE_TO_BLACK_115)
        val max = player.skills.maximumLifepoints
        val damage = (max * 0.05).toInt().coerceAtLeast(1)
        queueScript(player, 1, QueueStrength.SOFT) { stage : Int ->
            when(stage) {
                0 -> {
                    sendMessage(player, "The guard pushes you back down the hill.")
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    return@queueScript delayScript(player, 6)
                }
                1 -> {
                    impact(player, damage, ImpactHandler.HitsplatType.NORMAL)
                    sendGraphics(Graphics.STUN_BIRDIES_ABOVE_HEAD_80, player.location)
                    return@queueScript delayScript(player, 3)
                }
                2 -> {
                    sendChat(player, "Urrrrrgh!")
                    return@queueScript stopExecuting(player)
                }
                else ->  return@queueScript stopExecuting(player)
            }
        }
    }
}