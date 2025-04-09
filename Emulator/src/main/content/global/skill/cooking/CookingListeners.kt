package content.global.skill.cooking

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class CookingListeners : InteractionListener {

    private val potteryId = intArrayOf(
        Scenery.POTTERY_OVEN_2643,
        Scenery.POTTERY_OVEN_4308,
        Scenery.POTTERY_OVEN_11601,
        Scenery.POTTERY_OVEN_34802
    )

    private val kebabId = intArrayOf(
        Items.KEBAB_1971,
        Items.UGTHANKI_KEBAB_1883,
        Items.UGTHANKI_KEBAB_1885
    )

    override fun defineListeners() {
        /*
         * Handles creating a super kebab from a kebab and red-hot sauce.
         */

        onUseWith(IntType.ITEM, Items.RED_HOT_SAUCE_4610, *kebabId) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItemOrDrop(player, Items.SUPER_KEBAB_4608)
            }
            return@onUseWith true
        }

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
         * Handles interacting with the pottery oven.
         */

        on(potteryId, IntType.SCENERY, "fire") { player, node ->
            player.faceLocation(node.location)
            player.dialogueInterpreter.open(99843, true, true)
            return@on true
        }

        /*
         * Handles using a jug of water with grapes to create unfermented wine.
         */

        onUseWith(IntType.ITEM, Items.JUG_OF_WATER_1937, Items.GRAPES_1987) { player, used, with ->
            val itemSlot = used.asItem().slot
            if (getStatLevel(player, Skills.COOKING) < 35) {
                sendDialogue(player, "You need a cooking level of 35 to do this.")
                return@onUseWith false
            }
            if (removeItem(player, with.asItem())) {
                replaceSlot(player, itemSlot, Item(Items.UNFERMENTED_WINE_1995, 1))
                submitIndividualPulse(player, WineFermentingPulse(1, player))
            }
            return@onUseWith true
        }

        /*
         * Handles attempting to add cheese to a baked potato without butter.
         */

        onUseWith(IntType.ITEM, Items.POTATO_1943, Items.CHEESE_1985) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }

    }

}
