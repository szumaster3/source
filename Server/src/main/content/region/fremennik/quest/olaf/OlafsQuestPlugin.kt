package content.region.fremennik.quest.olaf

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Components
import shared.consts.Items

class OlafsQuestPlugin : InteractionListener {

    override fun defineListeners() {

        on(Items.SVENS_LAST_MAP_11034, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.OLAF2_TREASUREMAP_254)
            return@on true
        }
    }
}
