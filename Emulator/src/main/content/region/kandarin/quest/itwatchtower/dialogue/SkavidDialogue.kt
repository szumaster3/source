package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Skavid dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class SkavidDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc(FaceAnim.OLD_NEUTRAL, "Cur bidith...")
        sendMessage(player, "The skavid is trying to communicate...")
        sendMessage(player, "You don't know any skavid words yet!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SKAVID_865)
}
