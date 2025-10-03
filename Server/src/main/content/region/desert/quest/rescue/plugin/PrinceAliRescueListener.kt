package content.region.desert.quest.rescue.plugin

import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items

class PrinceAliRescueListener: InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.YELLOW_DYE_1765, Items.WIG_2421) { player, used, with ->
            val itemUsed = used.asItem()
            val itemSlot = player.inventory.getSlot(with.asItem())
            val itemReward = Item(Items.WIG_2419)

            if (removeItem(player, itemUsed)) {
                replaceSlot(player, itemSlot, itemReward)
                sendMessage(player, "You dye the wig blonde.")
            }
            return@onUseWith true
        }
    }
}
