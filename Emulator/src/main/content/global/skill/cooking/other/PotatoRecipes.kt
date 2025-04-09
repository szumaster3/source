package content.global.skill.cooking.other

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class PotatoRecipes : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles attempting to add cheese to a baked potato without butter.
         */

        onUseWith(IntType.ITEM, Items.POTATO_1943, Items.CHEESE_1985) { player, _, _ ->
            sendMessage(player, "You must add butter to the baked potato before adding toppings.")
            return@onUseWith true
        }

        /*
         * Handles tuna potato recipe.
         * Ticks: 2 (1.2s)
         */

        onUseWith(IntType.ITEM, Items.POTATO_WITH_BUTTER_6703, Items.TUNA_AND_CORN_7068) { player, used, with ->
            if (getStatLevel(player, Skills.COOKING) < 68) {
                sendMessage(player, "You need a Cooking level of 68 to make that.")
                return@onUseWith false
            }

            if (amountInInventory(player, used.id) == 1 || amountInInventory(player, with.id) == 1) {
                if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                    addItem(player, Items.TUNA_POTATO_7060, 1)
                    addItem(player, Items.BOWL_1923, 1)
                    rewardXP(player, Skills.COOKING, 10.0)
                }
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Items.TUNA_POTATO_7060)
                create { _, amount ->
                    runTask(player, 2, amount) {
                        if (amount < 1) return@runTask
                        if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(
                                player,
                                Item(with.id, 1),
                                Container.INVENTORY
                            )
                        ) {
                            addItem(player, Items.TUNA_POTATO_7060, 1, Container.INVENTORY)
                            addItem(player, Items.BOWL_1923, 1, Container.INVENTORY)
                            rewardXP(player, Skills.CRAFTING, 10.0)
                            sendMessage(player, "You add the topping to the potato.")
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