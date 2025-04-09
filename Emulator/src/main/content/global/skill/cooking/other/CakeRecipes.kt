package content.global.skill.cooking.other

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class CakeRecipes : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles making a chocolate cake.
         */

        onUseWith(IntType.ITEM, Items.CHOCOLATE_BAR_1973, Items.CAKE_1891) { player, used, with ->
            if(removeItem(player, Item(used.id, 1)) && removeItem(player, Item(with.id, 1))) {
                addItem(player, Items.CHOCOLATE_CAKE_1897)
                sendMessage(player, "You add chocolate to the cake.")
            }
            return@onUseWith true
        }
    }
}