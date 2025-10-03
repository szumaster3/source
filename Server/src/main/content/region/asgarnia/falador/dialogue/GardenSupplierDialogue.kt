package content.region.asgarnia.falador.dialogue

import core.api.openNpcShop
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Garden Supplier (Falador) dialogue.
 */
@Initializable
class GardenSupplierDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc("Hello, I sell many plants. Would you like", "to see what I have?")
        return true
    }

    override fun handle(componentId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendDialogueOptions(player, "Select one", "Yes, please!", "No, thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("Yes, please!").also { stage++ }
                2 -> player("No, thanks.").also { stage = END_DIALOGUE }
            }

            2 -> {
                end()
                openNpcShop(player, NPCs.GARDEN_SUPPLIER_4251)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GARDEN_SUPPLIER_4251)
}
