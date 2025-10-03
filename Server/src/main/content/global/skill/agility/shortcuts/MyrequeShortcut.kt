package content.global.skill.agility.shortcuts

import core.api.sendMessage
import core.api.teleport
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.Scenery

/**
 * Handles the broken fence shortcut.
 */
class MyrequeShortcut : InteractionListener {

    override fun defineListeners() {
        on(Scenery.TRAPDOOR_5055, IntType.SCENERY, "open") { player, _ ->
            teleport(player, Location(3477, 9845))
            sendMessage(player, "You open the trap door and find yourself in the inn basement.")
            return@on true
        }

        on(Scenery.WALL_5052, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the wall and find the door which Veliaf told you about.")
            sendMessage(player, "You walk through.", 1)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }
    }
}
