package content.global.plugin.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class CapAndGogglesPlugin : InteractionListener {

    private val capAndGoggles = Items.CAP_AND_GOGGLES_9946
    private val bomberCap = Items.BOMBER_CAP_9945
    private val gnomeGoggles = Items.GNOME_GOGGLES_9472

    override fun defineListeners() {

        /*
         * Handles split.
         */

        on(capAndGoggles, IntType.ITEM, "split") { player, node ->
            if (freeSlots(player) < 2) {
                sendDialogue(player, "You don't have enough inventory space for that.")
                return@on true
            }

            if(removeItem(player, Item(capAndGoggles, 1))) {
                replaceSlot(
                    player,
                    slot = node.index,
                    item = Item(bomberCap, 1),
                    container = Container.INVENTORY,
                )
                addItem(player, gnomeGoggles, 1)
            }
            return@on true
        }
    }
}
