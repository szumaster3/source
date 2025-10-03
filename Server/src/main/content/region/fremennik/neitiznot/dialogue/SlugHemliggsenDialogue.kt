package content.region.fremennik.neitiznot.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class SlugHemliggsenDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npcl(FaceAnim.WORRIED, "Shhh. Go away. I'm not allowed to talk to you.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.ANNOYED, "Fine, whatever ...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SlugHemliggsenDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SLUG_HEMLIGSSEN_5520)
}
