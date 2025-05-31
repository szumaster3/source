package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Og dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class OgDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val questStage = getQuestStage(player, Quests.WATCHTOWER)

        when {
            questStage == 2 -> {
                npc(FaceAnim.OLD_DEFAULT, "Why you here, little t'ing?")
            }
            questStage >= 3 -> {
                npc(FaceAnim.OLD_DEFAULT, "Where my gold from dat dirty Toban?")
                stage = 9
            }
            !isQuestComplete(player, Quests.WATCHTOWER) -> {
                sendMessage(player, "The ogre has nothing to say at the moment.")
            }
            else -> {
                sendMessage(player, "The ogre is not interested in you anymore.")
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I seek entrance to the city of ogres.", "I have come to kill you.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("I seek entrance to the city of ogres.").also { stage++ }
                2 -> player("I have come to kill you!").also { stage = 7 }
            }
            2 -> npc(FaceAnim.OLD_DEFAULT, "You got no business there!").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT, "Just a minute... Maybe if you did somet'ing for me, I", "might help you get in.").also { stage++ }
            4 -> player("What can I do to help an ogre?").also { stage++ }
            5 -> npc(FaceAnim.OLD_DEFAULT, "South-east of here der is more ogres - the name of the", "chieftain is Toban. He stole gold from me and I want it", "back!").also { stage++ }
            6 -> npc(FaceAnim.OLD_DEFAULT, "Here is a key to the chest it's in. If you bring it here,", "I may reward you...").also {
                end()
                addItemOrDrop(player, Items.TOBANS_KEY_2378, 1)
                setQuestStage(player, Quests.WATCHTOWER, 3)
            }
            7 -> npc(FaceAnim.OLD_DEFAULT, "Kill me, eh? You shall be crushed. Guards!").also { stage++ }
            8 -> {
                end()
                RegionManager.getLocalNpcs(player).firstOrNull { it.id == NPCs.OGRE_115 }?.attack(player)
                sendMessage(player, "You are under attack!")
            }
            9 -> options("I have your gold.", "I haven't got it yet.", "I have lost the key!").also { stage++ }
            10 -> when(buttonId) {
                1 -> player("I have your gold.").also { stage++ }
                2 -> player("I haven't got it yet.").also { stage = 13 }
                3 -> player("I have lost the key!").also { stage = 14 }
            }
            11 -> {
                if (!inInventory(player, Items.TOBANS_GOLD_2393)) {
                    npc(FaceAnim.OLD_DEFAULT, "Dat is not what I want! If you want to impress me,", "get da gold I asked for!")
                    stage = END_DIALOGUE
                } else if (freeSlots(player) == 0) {
                    npc(FaceAnim.OLD_NEUTRAL, "Hah! This little t'ing is pretty smart for such a little t'ing!", "Pity you don't have any space for your reward.")
                    stage = END_DIALOGUE
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Well, well, the little t'ing has got it!", "Take dis to show", "little t'ing is a friend to da ogres. Hahahahaha!")
                    stage = 12
                }
            }
            12 -> {
                end()
                removeItem(player, Items.TOBANS_GOLD_2393)
                sendItemDialogue(player, Items.RELIC_PART_1_2373, "The ogre gives you part of a horrible statue.")
                setAttribute(player, GameAttributes.WATCHTOWER_RELIC_1, true)
                addItem(player, Items.RELIC_PART_1_2373)
            }
            13 -> npcl(FaceAnim.OLD_DEFAULT, "Don't come back until you have it, unless you wanna be on tonight's menu!").also { stage = END_DIALOGUE }
            14 -> {
                if (inInventory(player, Items.TOBANS_KEY_2378)) {
                    npc(FaceAnim.OLD_DEFAULT, "Oh yeah? What's dat den?")
                    stage = 15
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Stoopid! Take another and don't lose it!")
                    addItem(player, Items.TOBANS_KEY_2378, 1)
                }
            }
            15 -> {
                end()
                sendMessage(player, "It seems you still have the key...")
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OG_853)
}
