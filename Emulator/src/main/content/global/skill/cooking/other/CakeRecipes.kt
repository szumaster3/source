package content.global.skill.cooking.other

import core.api.*
import core.api.item.allInInventory
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class CakeRecipes : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles creating an uncooked cake.
         */

        onUseWith(IntType.ITEM, Items.CAKE_TIN_1887, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944) { player, used, _ ->
            if (anyInInventory(player, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944) && !allInInventory(player, Items.POT_OF_FLOUR_1933, Items.BUCKET_OF_MILK_1927, Items.EGG_1944)) {
                return@onUseWith false
            }

            if (player.inventory.remove(Item(Items.POT_OF_FLOUR_1933, 1)) &&
                player.inventory.remove(Item(Items.BUCKET_OF_MILK_1927, 1)) &&
                player.inventory.remove(Item(Items.EGG_1944, 1)) &&
                player.inventory.remove(Item(Items.CAKE_TIN_1887, 1))) {

                addItem(player, Items.UNCOOKED_CAKE_1889, 1, Container.INVENTORY)

                sendMessage(player, "You mix the milk, flour, and egg together to make a raw cake mix.")
                return@onUseWith true
            }

            return@onUseWith false
        }

        /*
         * Handles making a chocolate cake.
         */

        onUseWith(IntType.ITEM, Items.CHOCOLATE_BAR_1973, Items.CAKE_1891) { player, used, with ->
            if(removeItem(player, Item(used.id, 1)) && removeItem(player, Item(with.id, 1))) {
                addItem(player, Items.CHOCOLATE_CAKE_1897, 1, Container.INVENTORY)
                sendMessage(player, "You add chocolate to the cake.")
            }
            return@onUseWith true
        }
    }
}