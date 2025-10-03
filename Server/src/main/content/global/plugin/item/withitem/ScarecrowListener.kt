package content.global.plugin.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items

class ScarecrowListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles using hay sack with watermelon to creating scarecrow.
         */

        onUseWith(IntType.ITEM, Items.HAY_SACK_6058, Items.WATERMELON_5982) { player, used, _ ->
            if (getStatLevel(player, Skills.FARMING) < 23) {
                sendMessage(player, "Your Farming level is not high enough to do this.")
            } else {
                removeItem(player, Items.WATERMELON_5982)
                replaceSlot(player, used.asItem().slot, Item(Items.SCARECROW_6059, 1))
                rewardXP(player, Skills.FARMING, 25.0)
                sendMessages(
                    player,
                    "You stick a watermelon on top of the hay sack.",
                    "This would be ideal for scaring birds!",
                )
            }
            return@onUseWith true
        }
    }
}
