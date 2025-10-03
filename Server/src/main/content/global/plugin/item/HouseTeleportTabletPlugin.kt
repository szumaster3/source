package content.global.plugin.item

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import shared.consts.Items

class HouseTeleportTabletPlugin : InteractionListener {

    private val houseTeleport = Items.TP_TO_HOUSE_8013

    override fun defineListeners() {
        on(houseTeleport, IntType.ITEM, "break") { player, node ->
            var hasHouse = player.houseManager.location.exitLocation != null
            if (!hasHouse) {
                sendMessage(player, "You must have a house to teleport to before attempting that.")
                return@on false
            }
            if (hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }
            closeInterface(player)
            player.locks.lockComponent(8)
            player.lock(8)
            if (inInventory(player, node.id)) {
                player.houseManager.preEnter(player, false)
                val location = player.houseManager.getEnterLocation()
                if (teleport(player, location, TeleportManager.TeleportType.TELETABS)) {
                    removeItem(player, Item(node.id, 1))
                    player.houseManager.postEnter(player, false)
                }
            }
            return@on true
        }
    }
}
