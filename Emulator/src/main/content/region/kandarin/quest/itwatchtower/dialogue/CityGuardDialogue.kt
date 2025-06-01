package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the City guard dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
class CityGuardDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.CITY_GUARD_862)
        when(stage) {
            0 -> if(getAttribute(player!!, GameAttributes.WATCHTOWER_RIDDLE, false) && inInventory(player!!, Items.DEATH_RUNE_560)) {
                removeItem(player!!, Item(Items.DEATH_RUNE_560, 1))
                player("I worked it out!")
                stage = 11
            } else {
                npc(FaceAnim.OLD_ANGRY1, "Grrrr, what business you got here?")
                stage = 1
            }
            1 -> player("I am on an errand.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_CALM_TALK2, "So what you want with me?").also { stage++ }
            3 -> showTopics(
                Topic("I am an ogre killer come to destroy you!", 4),
                Topic("I seek passage into the skavid caves.", 5)
            )
            4 -> {
                end()
                npc?.attack(player)
            }
            5 -> npc(FaceAnim.OLD_NORMAL, "Is that so... You humour me small t'ing, answer this", "riddle and I will help you...").also { stage++ }
            6 -> npc(FaceAnim.OLD_DEFAULT, "I want you to bring me an item: I will give you all the", "letters of this item, you work out what it is...").also { stage++ }
            7 -> npc(FaceAnim.OLD_DEFAULT,"My first is in days, but not in years, My second is in", "evil, and also in tears, My third is in all, but not in", "none, My fourth is in hot, but not in sun.").also { stage++ }
            8 -> npc(FaceAnim.OLD_DEFAULT, "My fifth is in heaven, and also in hate, My sixth is in", "fearing, but not in fate, My seventh is in plush, but not", "in place.").also { stage++ }
            9 -> npc(FaceAnim.OLD_DEFAULT, "My eighth is in nine, but not in eight, My last is in", "earth, and also in great.").also { stage++ }
            10 -> npc(FaceAnim.OLD_DEFAULT, "My whole is an object, that magic will make, It brings", "wrack and ruin to all in its wake... Now how long, I", "wonder, will this riddle take?").also {
                setAttribute(player!!, GameAttributes.WATCHTOWER_RIDDLE, true)
                stage = END_DIALOGUE
            }
            11 -> npc("Well, well. The imp has done it! Thanks for the rune;", "this is what you be needing...").also { stage++ }
            12 -> {
                end()
                addItemOrDrop(player!!, Items.SKAVID_MAP_2376, 1)
                sendMessage(player!!, "The guard gives you a map.")
            }
        }
    }
}