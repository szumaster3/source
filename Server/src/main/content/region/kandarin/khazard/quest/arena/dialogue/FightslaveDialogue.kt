package content.region.kandarin.khazard.quest.arena.dialogue

import core.api.allInEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Fightslave dialogue.
 *
 * # Relations
 * - [Fight Arena][content.region.kandarin.khazard.quest.arena.FightArena]
 */
@Initializable
class FightslaveDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Do you know of a Justin or Jeremy in this arena?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                if (allInEquipment(player, Items.KHAZARD_HELMET_74, Items.KHAZARD_ARMOUR_75)) {
                    npcl(FaceAnim.FRIENDLY, "Please leave me alone.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.AFRAID, "I've not met anybody in here by that name.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FIGHTSLAVE_262)
}
