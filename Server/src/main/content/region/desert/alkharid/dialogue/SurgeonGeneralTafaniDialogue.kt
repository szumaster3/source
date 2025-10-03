package content.region.desert.alkharid.dialogue

import core.api.hasLevelStat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.global.Skillcape
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Surgeon General Tafani dialogue.
 */
@Initializable
class SurgeonGeneralTafaniDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Hi. How can I help?").also { stage = 100 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            100 -> showTopics(
                Topic("Can you heal me?", AlKharidHealDialogue(true)),
                Topic("Do you see a lot of injured fighters?", 201),
                Topic("Do you come here often?", 301),
                IfTopic(
                    "Can I buy a Skillcape of Hitpoints from you?", 401, hasLevelStat(player, Skills.HITPOINTS, 99)
                ),
                IfTopic("Where can I get a cape like yours?", 501, !hasLevelStat(player, Skills.HITPOINTS, 99)),
            )
            201 -> npcl(FaceAnim.HALF_THINKING, "Yes I do. Thankfully we can cope with almost anything. Jaraah really is a wonderful surgeon, his methods are a little unorthodox but he gets the job done.").also { stage++ }
            202 -> npcl(FaceAnim.HALF_GUILTY, "I shouldn't tell you this but his nickname is 'The Butcher'.").also { stage++ }
            203 -> playerl(FaceAnim.HALF_WORRIED, "That's reassuring.").also { stage = END_DIALOGUE }
            301 -> npcl(FaceAnim.LAUGH, "I work here, so yes!").also { stage = END_DIALOGUE }
            401 -> npcl(FaceAnim.FRIENDLY, "Why, certainly my friend. However, owning such an item makes you part of an elite group and that privilege will cost you 99000 coins.").also { stage++ }
            402 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "Sorry, that's much too pricey.", 411),
                Topic(FaceAnim.HAPPY, "Sure, that's not too expensive for such a magnificent cape.", 421),
            )
            411 -> npcl(FaceAnim.NEUTRAL, "I'm sorry you feel that way. Still, if you change your mind...").also { stage = END_DIALOGUE }
            421 -> if (Skillcape.purchase(player, Skills.HITPOINTS)) {
                npcl(FaceAnim.FRIENDLY, "There you go! I hope you enjoy it.").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.HALF_GUILTY, "Sorry, you can't buy that right now.").also { stage = END_DIALOGUE }
            }
            501 -> npcl(FaceAnim.HALF_THINKING, "Well, these capes are only available for people who have achieved a Hitpoint level of 99. You should go and train some more and come back to me when your Hitpoints are higher.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SurgeonGeneralTafaniDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SURGEON_GENERAL_TAFANI_961)
}
