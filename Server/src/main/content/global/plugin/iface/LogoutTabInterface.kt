package content.global.plugin.iface

import core.api.sendMessage
import core.game.interaction.InterfaceListener
import core.game.world.repository.Repository
import shared.consts.Components

/**
 * Represents the interface.
 * @author Vexia
 */
class LogoutTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.LOGOUT_182) { player, _, _, buttonID, _, _ ->
            if (buttonID == 6) {
                return@on when {
                    !player.zoneMonitor.canLogout() -> true

                    player.inCombat() -> {
                        sendMessage(player, "You can't log out until 10 seconds after the end of combat.")
                        true
                    }

                    player.isTeleporting -> {
                        sendMessage(player, "Please finish your teleport before logging out.")
                        true
                    }

                    else -> {
                        Repository.disconnectionQueue.add(player)
                        true
                    }
                }
            }
            return@on true
        }
    }
}
