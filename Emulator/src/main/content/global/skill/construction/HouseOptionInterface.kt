package content.global.skill.construction

import content.global.plugin.iface.WarningListener
import content.global.plugin.iface.Warnings
import core.api.sendMessage
import core.game.interaction.InterfaceListener
import shared.consts.Components

/**
 * Handles the house options interface.
 */
class HouseOptionInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.POH_HOUSE_OPTIONS_398) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                14 -> {
                    if (player.houseManager.isInHouse(player) && !WarningListener.isDisabled(player, Warnings.PLAYER_OWNED_HOUSES)) {
                        WarningListener.openWarning(
                            player,
                            Warnings.PLAYER_OWNED_HOUSES,
                        )
                    } else {
                        player.houseManager.toggleBuildingMode(player, true)
                    }
                    return@on true
                }
                1 -> {
                    player.houseManager.toggleBuildingMode(player, false)
                    return@on true
                }

                15 -> {
                    player.houseManager.expelGuests(player)
                    return@on true
                }

                13 -> {
                    if (!player.houseManager.isInHouse(player)) {
                        sendMessage(player, "You can't do this outside of your house.")
                        return@on true
                    }
                    HouseManager.leave(player)
                    return@on true
                }

                else -> return@on false
            }
        }
    }
}
