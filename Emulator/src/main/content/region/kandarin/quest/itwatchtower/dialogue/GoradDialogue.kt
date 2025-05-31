package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.inInventory
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Gorad dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class GoradDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if(inInventory(player, Items.TOBANS_GOLD_2393)) {
            player("I've come to knock your teeth out!")
        } else {
            player("Hello!")
            stage = 2
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "You shut your face! I smack you till you dead and", "sorry!").also { stage++ }
            1 -> {
                end()
                npc.attack(player)
            }
            2 -> player("I seek another task.").also { stage++ }
            3 -> npcl(FaceAnim.OLD_CALM_TALK2, "You know who I is?").also { stage++ }
            4 -> options("A big, ugly brown creature.", "I don't know who you are..").also { stage++ }
            5 -> when (buttonId) {
                1 -> player("A big, ugly brown creature.").also { stage++ }
                2 -> player("I don't know who you are.").also { stage = 6 }
            }
            6 -> npcl(FaceAnim.OLD_NORMAL, "I am Gorad, and you are tiny. Go now and I won't chase you!").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GORAD_856)
}