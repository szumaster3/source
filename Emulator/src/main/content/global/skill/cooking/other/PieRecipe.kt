package content.global.skill.cooking.other

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class PieRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a pie shell from pastry dough and a pie dish.
         */

        onUseWith(IntType.ITEM, Items.PASTRY_DOUGH_1953, Items.PIE_DISH_2313) { player, used, with ->
            val pieDish = with.asItem().slot
            if (removeItem(player, Item(used.id, 1))) {
                replaceSlot(player, pieDish, Item(Items.PIE_SHELL_2315, 1))
                sendMessage(player, "You put the pastry dough into the pie dish to make a pie shell.")
            }
            return@onUseWith true
        }

        /*
         * Handles apple pie recipe.
         */

        onUseWith(IntType.ITEM, Items.PIE_SHELL_2315, Items.COOKING_APPLE_1955) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 30) {
                sendMessage(player, "You need a Cooking level of 30 to make that.")
                return@onUseWith false
            }
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.UNCOOKED_APPLE_PIE_2317, 1, Container.INVENTORY)
                sendMessage(player, "You fill the pie with apple slices.")
            }
            return@onUseWith true
        }

        /*
         * Handles red berries pie recipe.
         */

        onUseWith(IntType.ITEM, Items.PIE_SHELL_2315, Items.REDBERRIES_1951) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 10) {
                sendMessage(player, "You need a Cooking level of 10 to make that.")
                return@onUseWith false
            }
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.UNCOOKED_BERRY_PIE_2321, 1, Container.INVENTORY)
                sendMessage(player, "You fill the pie with redberries.")
            }
            return@onUseWith true
        }

    }

}