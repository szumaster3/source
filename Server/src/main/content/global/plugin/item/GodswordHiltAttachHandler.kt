package content.global.plugin.item

import core.api.getItemName
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.StringUtils
import shared.consts.Items

@Initializable
class GodswordHiltAttachHandler : UseWithHandler(Items.ARMADYL_HILT_11702, Items.BANDOS_HILT_11704, Items.SARADOMIN_HILT_11706, Items.ZAMORAK_HILT_11708) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.GODSWORD_BLADE_11690, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        var item = event.usedItem ?: return false
        val baseItem = event.baseItem
        val player = event.player
        if (!player.inventory.containsItem(item) || !player.inventory.containsItem(baseItem)) {
            return false
        }
        if (player.inventory.replace(null, item.slot, false) !== item ||
            player.inventory.replace(null, baseItem.slot, false) !== baseItem) {
            player.inventory.update()
            return false
        }

        item = Item(item.id - 8)
        player.inventory.add(item)
        val name = getItemName(item.id)
        sendMessage(player, "You attach the hilt to the blade and make a" + (if (StringUtils.isPlusN(name)) "n " else " ") + name + ".")
        return true
    }
}
