package content.global.skill.cooking.other

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.Animations
import org.rs.consts.Items

class CuttingRecipes : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles cutting ingredients with a knife.
         * Products:
         *  - Calquat keg
         *  - Chocolate dust
         *
         * Ticks: 4 (2.4s)
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.CALQUAT_FRUIT_5980, Items.CHOCOLATE_BAR_1973) { player, _, items ->
            val (base, product, animation) = when (items.id) {
                Items.CALQUAT_FRUIT_5980 -> Triple(Items.CALQUAT_FRUIT_5980, Items.CALQUAT_KEG_5769, Animations.CARVE_CALQUAT_KEG_2290)
                Items.CHOCOLATE_BAR_1973 -> Triple(Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_DUST_1975, Animations.CUTTING_CHOCOLATE_BAR_1989)
                else -> return@onUseWith false
            }

            player.pulseManager.run(object : Pulse(1) {
                override fun pulse(): Boolean {
                    super.setDelay(4)
                    val amount = amountInInventory(player, base)
                    if (amount > 0) {
                        if (removeItem(player, Item(base, 1), Container.INVENTORY)) {
                            animate(player, animation)
                            addItem(player, product, 1, Container.INVENTORY)
                        }
                    }
                    return amount <= 0
                }
            })

            return@onUseWith true
        }

        /*
         * Handles chopping ingredients into a bowl using a knife.
         * Products:
         *  - Chopped tuna
         *  - Chopped onion
         *  - Sliced mushrooms
         *  - Minced meat
         *
         * Ticks: 2 (1.2s)
         */

        onUseWith(
            IntType.ITEM,
            Items.BOWL_1923,
            // Ingredients:
            Items.TUNA_361,
            Items.ONION_1957,
            Items.MUSHROOM_6004,
            Items.COOKED_MEAT_2142
        ) { player, used, ingredients ->
            if (!inInventory(player, Items.KNIFE_946)) {
                sendMessage(player, "You need a knife to slice up the ${ingredients.name}.")
                return@onUseWith false
            }

            val (product, message) = when (ingredients.id) {
                Items.TUNA_361 -> Items.CHOPPED_TUNA_7086 to "You chop the tuna into the bowl."
                Items.ONION_1957 -> Items.CHOPPED_ONION_1871 to "You chop the onion into small pieces."
                Items.MUSHROOM_6004 -> Items.SLICED_MUSHROOMS_7080 to "You slice the mushrooms."
                Items.COOKED_MEAT_2142 -> Items.MINCED_MEAT_7070 to "You chop the meat into the bowl."
                else -> return@onUseWith false
            }

            player.pulseManager.run(object : Pulse(1) {
                override fun pulse(): Boolean {
                    super.setDelay(2)
                    val amount = amountInInventory(player, ingredients.id)
                    if (amount > 0) {
                        if (removeItem(player, Item(ingredients.id, 1), Container.INVENTORY) && removeItem(player, Item(used.id, 1), Container.INVENTORY)) {
                            animate(player, Animations.CUT_THING_WITH_KNIFE_IN_HAND_5756)
                            addItem(player, product, 1, Container.INVENTORY)
                            rewardXP(player, Skills.COOKING, 1.0)
                            sendMessage(player, message)
                        }
                    }
                    return amount <= 0
                }
            })
            return@onUseWith true
        }
    }
}