package content.region.desert.alkharid.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Aabla dialogue.
 */
@Initializable
class AablaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                Topic("Can you heal me?", AlKharidHealDialogue(true)),
                Topic("Do you see a lot of injured fighters?", 101),
                Topic("Do you come here often?", 201),
            )
            101 -> npcl(FaceAnim.FRIENDLY, "I work here, so yes!").also { stage = END_DIALOGUE }
            201 -> npcl(FaceAnim.HALF_THINKING, "Yes I do. Thankfully we can cope with almost anything. Jaraah really is a wonderful surgeon, his methods are a little unorthodox but he gets the job done.").also { stage++ }
            202 -> npcl(FaceAnim.HALF_GUILTY, "I shouldn't tell you this but his nickname is 'The Butcher'.").also { stage++ }
            203 -> playerl(FaceAnim.HALF_WORRIED, "That's reassuring.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AablaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.AABLA_959)
}
