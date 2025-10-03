package content.global.plugin.item

import core.api.addItem
import core.api.freeSlots
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class TempleKnighSetsPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles unpacking the set from the initiate box.
         */

        on(Items.INITIATE_HARNESS_M_9668, IntType.ITEM, "unpack") { player, node ->
            if (freeSlots(player) < 2) {
                sendMessage(player, "You don't have enough inventory space for the component parts.")
                return@on true
            }
            replaceSlot(player, node.asItem().index, Item(Items.INITIATE_SALLET_5574, 1))
            addItem(player, Items.INITIATE_HAUBERK_5575)
            addItem(player, Items.INITIATE_CUISSE_5576)
            return@on true
        }

        /*
         * Handles unpacking the set from the proselyte box (male).
         */

        on(Items.PROSYTE_HARNESS_M_9666, IntType.ITEM, "unpack") { player, node ->
            if (freeSlots(player) < 2) {
                sendMessage(player, "You don't have enough inventory space for the component parts.")
                return@on true
            }
            replaceSlot(player, node.asItem().index, Item(Items.PROSELYTE_SALLET_9672, 1))
            addItem(player, Items.PROSELYTE_HAUBERK_9674)
            addItem(player, Items.PROSELYTE_CUISSE_9676)
            return@on true
        }

        /*
         * Handles unpacking the set from the proselyte box (female).
         */

        on(Items.PROSYTE_HARNESS_F_9670, IntType.ITEM, "unpack") { player, node ->
            if (freeSlots(player) < 2) {
                sendMessage(player, "You don't have enough inventory space for the component parts.")
                return@on true
            }
            replaceSlot(player, node.asItem().index, Item(Items.PROSELYTE_SALLET_9672, 1))
            addItem(player, Items.PROSELYTE_HAUBERK_9674)
            addItem(player, Items.PROSELYTE_TASSET_9678)
            return@on true
        }
    }
}
