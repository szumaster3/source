package content.global.plugin.item.withobject

import core.api.removeItem
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery

class SwampHoleRopePlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles using rope on dark hole (Lumbridge swamp).
         */

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.DARK_HOLE_5947) { player, used, _ ->
            if (player.savedData.globalData.hasTiedLumbridgeRope()) {
                sendDialogue(player, "There is already a rope tied to the entrance.")
                return@onUseWith true
            }
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            sendItemDialogue(player, used, "You tie the rope to the top of the entrance and throw it down.")
            player.savedData.globalData.setLumbridgeRope(true)
            return@onUseWith true
        }
    }
}
