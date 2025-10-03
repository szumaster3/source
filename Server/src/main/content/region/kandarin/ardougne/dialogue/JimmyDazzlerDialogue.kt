package content.region.kandarin.ardougne.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Jimmy Dazzler dialogue.
 */
@Initializable
class JimmyDazzlerDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.SUSPICIOUS, "What's your name and your business here?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "My name is ${player.username}, and I have a couple of questions for you.").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "No.").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "What do you mean by no?").also { stage++ }
            3 -> npcl(FaceAnim.NEUTRAL, "That just isn't the ticket. I don't know you nor I will not answer any questions. Now get gone from here, you'll attract unwanted attention to me.").also { stage++ }
            4 -> playerl(FaceAnim.ROLLING_EYES, "As if those 'clothes' you're wearing wouldn't do that already.").also { stage++ }
            5 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = JimmyDazzlerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.JIMMY_DAZZLER_2949)
}
