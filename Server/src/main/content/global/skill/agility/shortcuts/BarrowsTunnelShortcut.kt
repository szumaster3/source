package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.teleport
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable

/**
 * Handles the barrows tunnel shortcut.
 */
@Initializable
class BarrowsTunnelShortcut : AgilityShortcut(intArrayOf(30261, 30262, 30265), 1, 0.0, "open") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val destination = when (scenery.id) {
            30261, 30262 -> Location(3509, 3448)
            30265 -> Location(3500, 9812)
            else -> null
        }
        destination?.let { teleport(player, it) }
    }
}
