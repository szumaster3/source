package content.global.skill.cooking.other

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class StewRecipe : InteractionListener {

    private val incompleteStew = intArrayOf(Items.INCOMPLETE_STEW_1997, Items.INCOMPLETE_STEW_1999)

    override fun defineListeners() {

        /*
         * Handles creating incomplete stews.
         */

        onUseWith(IntType.ITEM, Items.BOWL_OF_WATER_1921, Items.POTATO_1942, Items.COOKED_MEAT_2142) { player, used, ingredient ->
            if (getStatLevel(player, Skills.COOKING) < 25) {
                sendMessage(player, "You need a Cooking level of 25 to make that.")
                return@onUseWith false
            }

            val stew = when (ingredient.id) {
                Items.POTATO_1942 -> Items.INCOMPLETE_STEW_1997
                Items.COOKED_MEAT_2142 -> Items.INCOMPLETE_STEW_1999
                else -> return@onUseWith false
            }

            fun makeIncompleteStew(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(ingredient.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, stew, 1, Container.INVENTORY)
                    sendMessage(player, "You cut up the ${ingredient.name.lowercase()} and put it into the bowl.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, ingredient.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeIncompleteStew()
            }

            sendSkillDialogue(player) {
                withItems(stew)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeIncompleteStew()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating an uncooked stew.
         */

        onUseWith(IntType.ITEM, incompleteStew, Items.COOKED_MEAT_2142, Items.POTATO_1942) { player, used, ingredient ->
            if (getStatLevel(player, Skills.COOKING) < 25) {
                sendMessage(player, "You need a Cooking level of 25 to make that.")
                return@onUseWith false
            }

            fun makeUncookedStew(): Boolean {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) &&
                    removeItem(player, Item(ingredient.id, 1), Container.INVENTORY)
                ) {
                    addItem(player, Items.UNCOOKED_STEW_2001, 1, Container.INVENTORY)
                    sendMessage(player, "You cut up the ${ingredient.name.lowercase().replace("cooked", "").trim()} and put it into the stew.")
                    return true
                }
                return false
            }

            val amountUsed = amountInInventory(player, used.id)
            val amountWith = amountInInventory(player, ingredient.id)

            if (amountUsed == 1 || amountWith == 1) {
                return@onUseWith makeUncookedStew()
            }

            sendSkillDialogue(player) {
                withItems(Items.UNCOOKED_STEW_2001)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount > 0) makeUncookedStew()
                    }
                }
                calculateMaxAmount {
                    min(amountWith, amountUsed)
                }
            }

            return@onUseWith true
        }
    }
}