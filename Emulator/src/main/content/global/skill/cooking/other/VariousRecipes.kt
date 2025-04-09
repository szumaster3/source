package content.global.skill.cooking.other

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items

class VariousRecipes : InteractionListener {

    override fun defineListeners() {

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
/*
        /*
         * Handles butter potato recipe.
         */

        onUseWith(IntType.ITEM, Items.BAKED_POTATO_6701, Items.PAT_OF_BUTTER_6697) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 39) {
                sendMessage(player, "You need a Cooking level of 39 to make that.")
                return@onUseWith false
            }
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.POTATO_WITH_BUTTER_6703, 1, Container.INVENTORY)
                rewardXP(player, Skills.COOKING, 40.0)
                sendMessage(player, "You add the butter to the potato.")
            }
            return@onUseWith true
        }

        /*
         * Handles chilli potato recipe.
         */

        onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, Items.CHILLI_CON_CARNE_7062) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.CHILLI_POTATO_7054, 1, Container.INVENTORY)
                sendMessage(player, "You add the topping to the potato.")
            }
            return@onUseWith true
        }

        /*
         * Handles cheese potato recipe.
         */

        onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, Items.CHEESE_1985) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItem(player, Items.POTATO_WITH_CHEESE_6705, 1, Container.INVENTORY)
                sendMessage(player, "You add the cheese to the potato.")
            }
            return@onUseWith true
        }

        /*
         * Handles egg potato recipe.
         */

        onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, Items.EGG_AND_TOMATO_7064) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.EGG_POTATO_7056)
                sendMessage(player, "You add the topping to the potato.")
            }
            return@onUseWith true
        }

        /*
         * Handles mushroom potato recipe.
         */

        onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, Items.MUSHROOM_AND_ONION_7066) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.MUSHROOM_POTATO_7058)
                sendMessage(player, "You add the mushroom to the potato.")
            }
            return@onUseWith true
        }

        /*
         * Handles tuna and corn recipe.
         */
        onUseWith(IntType.ITEM, Items.CHOPPED_TUNA_7086, Items.COOKED_SWEETCORN_5988) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.TUNA_AND_CORN_7068)
                sendMessage(player, "You mix the ingredients to make the topping.")
            }
            return@onUseWith true
        }

        /*
         * Handles chilli con carne Recipe
         */

        onUseWith(IntType.ITEM, Items.SPICY_SAUCE_7072, Items.COOKED_MEAT_2142) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.CHILLI_CON_CARNE_7062)
                sendMessage(player, "You put the cut up meat into the bowl.")
            }
            return@onUseWith true
        }
    }
}
*/