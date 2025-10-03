package content.region.misthalin.varrock.quest.phoenixgang.plugin

import core.api.removeItem
import core.api.replaceSlot
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class CertificatePlugin: InteractionListener {

    override fun defineListeners() {

        /*
         * Handles combining two half-certificates into a full certificate.
         */

        onUseWith(IntType.ITEM, Items.HALF_CERTIFICATE_11173, Items.HALF_CERTIFICATE_11174) { player, used, with ->
            if (removeItem(player, with.asItem())) {
                replaceSlot(player, used.asItem().slot, Item(Items.CERTIFICATE_769))
            }
            return@onUseWith true
        }
    }
}
