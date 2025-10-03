package content.global.plugin.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

class PirateTreasureChestPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles using the chest key (Pirate's Treasure quest).
         */

        onUseWith(IntType.SCENERY, Items.CHEST_KEY_432, Scenery.CHEST_2079) { player, used, with ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            replaceScenery(with as core.game.node.scenery.Scenery, 2080, 3)
            addItem(player, Items.PIRATE_MESSAGE_433)
            playAudio(player, Sounds.PIRATECHEST_UNLOCK_2308)
            sendMessage(player, "You unlock the chest.")
            sendMessage(player, "All that's in the chest is a message...")
            sendMessage(player, "You take the message from the chest.")
            return@onUseWith true
        }
    }
}
