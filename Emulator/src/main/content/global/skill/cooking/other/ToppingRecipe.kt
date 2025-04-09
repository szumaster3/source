package content.global.skill.cooking.other

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class ToppingRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating Chilli con carne (first variant) using Spicy Sauce and Cooked Meat.
         *
         * Requirements:
         *  - Cooking Level 9
         *  - Items: Spicy Sauce and Cooked Meat.
         *  - Result: Chilli con Carne (25 XP).
         *
         * Ticks: 2 (1.2 seconds)
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
         * Handles creating Chilli con carne (second variant) using Spicy Sauce and Minced Meat.
         *
         * Requirements:
         *  - Cooking Level 9
         *  - Items: Spicy Sauce and Minced Meat.
         *  - Result: Chilli con Carne (25 XP) with Bowl.
         *
         * Ticks: 2 (1.2 seconds)
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
         * Handles creating Tuna and Corn topping.
         *
         * Requirements:
         *  - Cooking Level 67
         *  - Items: Cooked Sweetcorn and Chopped Tuna.
         *  - Result: Tuna and Corn topping (204 XP).
         *
         * Ticks: 2 (1.2 seconds)
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

        /*
         * Handles creating an Egg and tomato by combining a tomato with scrambled eggs.
         *
         * Requirements:
         *  - Egg and tomato: Level 23 Cooking, +50.0 XP
         *
         * Ticks: 2 (1.2 seconds)
         */

        onUseWith(IntType.ITEM, Items.TOMATO_1982, Items.SCRAMBLED_EGG_7078) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 23) {
                sendMessage(player, "You need a Cooking level of 23 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.EGG_AND_TOMATO_7064, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 50.0)
                    sendMessage(player, "You mix the scrambled egg with the tomato.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.POTATO_WITH_BUTTER_6703)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            addItem(player, Items.EGG_AND_TOMATO_7064, 1, Container.INVENTORY)
                            rewardXP(player, Skills.COOKING, 50.0)
                            sendMessage(player, "You mix the scrambled egg with the tomato.")
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
         * Handles creating a Wrapped oomlie by combining a raw oomlie and a palm leaf.
         *
         * Requirements:
         *  - Wrapped oomlie: Level 50 Cooking, +10.0 XP
         *
         * Ticks: 1 (0.6 seconds)
         */

        onUseWith(IntType.ITEM, Items.RAW_OOMLIE_2337, Items.PALM_LEAF_2339) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 50) {
                sendMessage(player, "You need a Cooking level of 50 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.WRAPPED_OOMLIE_2341, 1, Container.INVENTORY)
                    rewardXP(player, Skills.COOKING, 50.0)
                    sendMessage(player, "You wrap the raw oomlie in the palm leaf.")
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.POTATO_WITH_BUTTER_6703)
                create { _, amount ->
                    runTask(player, 1, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                            addItem(player, Items.WRAPPED_OOMLIE_2341, 1, Container.INVENTORY)
                            rewardXP(player, Skills.COOKING, 50.0)
                            sendMessage(player, "You wrap the raw oomlie in the palm leaf.")
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