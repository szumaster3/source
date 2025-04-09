package content.global.skill.cooking.other

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class ToppingRecipes : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles creating Chilli con carne.
         */

        onUseWith(IntType.ITEM, Items.SPICY_SAUCE_7072, Items.COOKED_MEAT_2143) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 9) {
                sendMessage(player, "You need a Cooking level of 9 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    rewardXP(player, Skills.COOKING, 25.0)
                    addItem(player, Items.CHILLI_CON_CARNE_7062, 1)
                    sendMessage(player, "You put the cut up meat into the bowl.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.CHILLI_CON_CARNE_7062)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            rewardXP(player, Skills.COOKING, 25.0)
                            addItem(player, Items.CHILLI_CON_CARNE_7062, 1, Container.INVENTORY)
                            sendMessage(player, "You put the cut up meat into the bowl.")
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating Chilli con carne (second variant).
         */

        onUseWith(IntType.ITEM, Items.SPICY_SAUCE_7072, Items.MINCED_MEAT_7070) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 9) {
                sendMessage(player, "You need a Cooking level of 9 to make that.")
                return@onUseWith false
            }

            if (freeSlots(player) < 1) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    rewardXP(player, Skills.COOKING, 25.0)
                    addItem(player, Items.BOWL_1923, 1)
                    addItem(player, Items.CHILLI_CON_CARNE_7062, 1)
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.CHILLI_CON_CARNE_7062)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            rewardXP(player, Skills.COOKING, 25.0)
                            addItem(player, Items.BOWL_1923, 1)
                            addItem(player, Items.CHILLI_CON_CARNE_7062, 1)
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating Tuna and corn.
         * Ticks: 2 (1.2s)
         */

        onUseWith(IntType.ITEM, Items.COOKED_SWEETCORN_5988, Items.CHOPPED_TUNA_7086) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 67) {
                sendMessage(player, "You need a Cooking level of 67 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    rewardXP(player, Skills.COOKING, 204.0)
                    addItem(player, Items.TUNA_AND_CORN_7068, 1, Container.INVENTORY)
                    sendMessage(player, "You mix the ingredients to make the topping.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.TUNA_AND_CORN_7068)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            rewardXP(player, Skills.COOKING, 204.0)
                            addItem(player, Items.TUNA_AND_CORN_7068, 1, Container.INVENTORY)
                            sendMessage(player, "You mix the ingredients to make the topping.")
                        }
                    }
                }

                calculateMaxAmount { _ ->
                    min(amountInInventory(player, with.id), amountInInventory(player, used.id))
                }
            }

            return@onUseWith true
        }
    }
}