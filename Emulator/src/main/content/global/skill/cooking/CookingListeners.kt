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

    }

}
