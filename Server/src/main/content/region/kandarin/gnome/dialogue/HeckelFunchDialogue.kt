package content.region.kandarin.gnome.dialogue

import content.global.activity.ttrail.ClueScroll
import content.global.activity.ttrail.TreasureTrailManager
import core.api.*
import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents The Heckel Funch dialogue.
 *
 * # Relations
 * - [CrypticClue][content.global.activity.ttrail.clues.CrypticClue]
 */
@Initializable
class HeckelFunchDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Good day to you, my friend, and a beautiful one at that. Would you like some groceries? I have all sorts. Alcohol also, if you're partial to a drink.").also { stage++ }
            1 -> options("I'll have a look.", "No, thank you.").also { stage++ }
            2 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "I'll have a look.").also { stage = 4 }
                2 -> playerl(FaceAnim.NEUTRAL, "No, thank you.").also { stage = 3 }
            }
            3 -> npcl(FaceAnim.OLD_NORMAL, "Ahh well, all the best to you.").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.OLD_NORMAL, "There's a good human.").also { stage++ }
            5 -> {
                end()
                openNpcShop(player, NPCs.HECKEL_FUNCH_603)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HeckelFunchDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HECKEL_FUNCH_603)
}