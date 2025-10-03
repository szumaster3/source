package content.global.plugin.item.withitem

import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class CleaningClothPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating cleaning cloth.
         */

        onUseWith(IntType.ITEM, Items.KARAMJAN_RUM_431, Items.SILK_950) { player, _, with ->
            replaceSlot(player, with.asItem().slot, Item(Items.CLEANING_CLOTH_3188, 1))
            sendMessage(player, "You pour some of the Karamjan rum over the silk.")
            return@onUseWith true
        }
    }
}
