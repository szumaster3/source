package content.global.skill.cooking.other

import core.api.Container
import core.api.addItemOrDrop
import core.api.removeItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class KebabRecipe : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating a super kebab.
         */

        onUseWith(IntType.ITEM, Items.RED_HOT_SAUCE_4610, Items.KEBAB_1971, Items.UGTHANKI_KEBAB_1883, Items.UGTHANKI_KEBAB_1885) { player, used, with ->
            if (removeItem(player, Item(used.id, 1), Container.INVENTORY) && removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                addItemOrDrop(player, Items.SUPER_KEBAB_4608)
            }
            return@onUseWith true
        }
    }
}