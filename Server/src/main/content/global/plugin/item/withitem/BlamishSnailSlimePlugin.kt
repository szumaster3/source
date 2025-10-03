package content.global.plugin.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Animations
import shared.consts.Items

class BlamishSnailSlimePlugin: InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating the blamish snail slime.
         */

        onUseWith(IntType.ITEM, Items.THIN_SNAIL_3363, Items.PESTLE_AND_MORTAR_233) { player, used, with ->
            val r = arrayOf(13621, 13877, 13876, 13875)
            for (i in r) if (!inBorders(player, getRegionBorders(i))) return@onUseWith false
            if (!inInventory(player, Items.SAMPLE_BOTTLE_3377, 1) &&
                anyInInventory(player, Items.FAT_SNAIL_3367, Items.THIN_SNAIL_3363)
            ) {
                sendItemDialogue(
                    player,
                    Items.SAMPLE_BOTTLE_3377,
                    "You need an empty sample bottle to place the slime in.",
                )
                return@onUseWith false
            } else {
                if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                    animate(player, Animations.PESTLE_MORTAR_364)
                    sendMessage(player, "You grind the blamish snail up into slimy pulp.")
                    replaceSlot(player, used.asItem().slot, Item(Items.BLAMISH_SNAIL_SLIME_1581, 1))
                } else {
                    return@onUseWith false
                }
            }
            return@onUseWith true
        }
    }
}
