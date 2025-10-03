package content.region.kandarin.ardougne.dialogue

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Bones (cat) dialogue.
 */
@Initializable
class BonesDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Who's a cute little kitty?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                end()
                if (!inEquipment(player, Items.CATSPEAK_AMULET_4677)) {
                    npcl(FaceAnim.CHILD_NEUTRAL, "Miaow!")
                } else {
                    npcl(FaceAnim.CHILD_NEUTRAL, "Jimmy doesn't like me talking to strangers.")
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = BonesDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BONES_2945)
}
