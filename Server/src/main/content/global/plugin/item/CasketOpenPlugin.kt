package content.global.plugin.item

import core.api.Container
import core.api.removeItem
import core.api.sendItemDialogue
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.StringUtils
import shared.consts.Items

class CasketOpenPlugin : InteractionListener {

    companion object {
        val loot = WeightBasedTable.create(
            WeightedItem(Items.COINS_995, 20, 640, 55.0, false),
            WeightedItem(Items.UNCUT_SAPPHIRE_1623, 1, 1, 32.0, false),
            WeightedItem(Items.UNCUT_EMERALD_1621, 1, 1, 16.0, false),
            WeightedItem(Items.UNCUT_RUBY_1619, 1, 1, 9.0, false),
            WeightedItem(Items.UNCUT_DIAMOND_1617, 1, 1, 2.0, false),
            WeightedItem(Items.COSMIC_TALISMAN_1454, 1, 1, 8.0, false),
            WeightedItem(Items.LOOP_HALF_OF_A_KEY_987, 1, 1, 1.0, false),
            WeightedItem(Items.TOOTH_HALF_OF_A_KEY_985, 1, 1, 1.0, false),
        )
    }

    override fun defineListeners() {

        /*
         * Handles opening the casket.
         */

        on(Items.CASKET_405, IntType.ITEM, "open") { player, node ->
            val casket = node.asItem()

            if (removeItem(player, casket, Container.INVENTORY)) {
                val finalLoot: ArrayList<Item> = loot.roll()
                finalLoot.forEach { player.inventory.add(it) }
                sendItemDialogue(
                    player, finalLoot[0],
                    "You open the casket. Inside you find " + (if (finalLoot[0].amount > 1) {
                        "some"
                    } else if (StringUtils.isPlusN(finalLoot[0].name)) {
                        "an"
                    } else {
                        "a"
                    }) + " " + finalLoot[0].name.lowercase() + ".",
                )
            }
            return@on true
        }
    }
}
