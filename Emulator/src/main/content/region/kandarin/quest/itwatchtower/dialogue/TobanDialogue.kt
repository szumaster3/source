package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Toban dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class TobanDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val hasRelic = hasAnItem(player, Items.RELIC_PART_3_2375).container != null
        if(getQuestStage(player, Quests.WATCHTOWER) > 1) {
            npc(FaceAnim.OLD_NEUTRAL, "What do you want, small t'ing?")
        } else {
            npc(FaceAnim.OLD_NEUTRAL, "Hahaha! Small t'ing returns. Did you bring the dragon", "bone?")
            stage = 10
        }
        if(getAttribute(player, GameAttributes.WATCHTOWER_RELIC_3, false) && !hasRelic) {
            player("I can't find the relic part you have me.").also { stage = 14 }
        } else {
                if (!isQuestComplete(player, Quests.WATCHTOWER)) {
                    sendMessage(player, "The ogre has nothing to say at the moment.")
                } else {
                    sendMessage(player, "The ogre is not interested in you anymore.")
                }
            }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val hasRelic = hasAnItem(player, Items.RELIC_PART_3_2375).container != null
        when (stage) {
            0 -> options("I seek entrance to the city of ogres.", "Die, creature!").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("I seek entrance to the city of ogres.").also { stage += 2 }
                2 -> player("Die, creature!").also { stage++ }
            }
            2 -> npcl(FaceAnim.OLD_NEUTRAL, "Hahaha! It t'inks it's a match for Toban, does it?").also {
                end()
                RegionManager.getLocalNpcs(player).firstOrNull { it.id == NPCs.OGRE_115 }?.attack(player)
                sendMessage(player, "You are under attack!")
            }
            3 -> npc(FaceAnim.OLD_NEUTRAL, "Hahaha! You'll never get in there.").also { stage++ }
            4 -> player("I'll find a way, trust me.").also { stage++ }
            5 -> npc(FaceAnim.OLD_NEUTRAL, "Bold words for a t'ing so small.").also { stage++ }
            6 -> options("I could do something for you...", "Die, creature!").also { stage++ }
            7 -> when (buttonId) {
                1 -> player("I could do something for you...").also { stage++ }
                2 -> player("Die, creature!").also { stage = 2 }
            }
            8 -> npc(FaceAnim.OLD_NEUTRAL, "Hahaha! This creature t'inks it can help me! I would eat", "you now, but for your punt size.").also { stage++ }
            9 -> npc(FaceAnim.OLD_NEUTRAL, "Prove to me your might: bring me the bones of an", "adult dragon to chew on, and I'll eat them, not you.",).also { stage = END_DIALOGUE }
            10 -> if(!inInventory(player, Items.DRAGON_BONES_536)) {
                player("I have nothing for you.")
                stage = 13
            } else {
                if(freeSlots(player) == 0) {
                    npcl(FaceAnim.OLD_NEUTRAL, "Hahaha! Small t'ing has done it. Toban is glad, but you can't hold his reward; you'd best put somet'ing down.")
                    return true
                }
                npcl(FaceAnim.OLD_NEUTRAL, "Hahaha! Small t'ing has done it. Toban is glad - take this...")
                stage++
            }
            11 -> player("When I say I will get something, I get it!").also { stage++ }
            12 -> {
                if(removeItem(player, Items.DRAGON_BONES_536)) {
                    sendItemDialogue(player, Items.RELIC_PART_3_2375, "The ogre gives you part of a statue.")
                    addItem(player, Items.RELIC_PART_3_2375)
                    setAttribute(player, GameAttributes.WATCHTOWER_RELIC_3, true)
                }
            }
            13 -> npc(FaceAnim.OLD_NEUTRAL, "Then you shall get nothing from me!").also { stage = END_DIALOGUE }
            14 -> {
                if (!hasRelic) {
                    npc(FaceAnim.OLD_NEUTRAL, "Small thing, how could you be so careless?")
                    stage = 16
                } else {
                    npc(FaceAnim.OLD_NEUTRAL, "Small thing, you like to me!")
                    stage++
                }
            }

            15 -> npc(FaceAnim.OLD_NEUTRAL, "I always says that small things are big trouble...").also { stage = END_DIALOGUE }
            16 -> npc(FaceAnim.OLD_NEUTRAL, "Here, take this one.").also { stage++ }
            17 -> {
                end()
                sendItemDialogue(player, Items.RELIC_PART_3_2375, "The ogre gives you part of a statue.")
                addItem(player, Items.RELIC_PART_3_2375)
            }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TobanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TOBAN_855)
}
