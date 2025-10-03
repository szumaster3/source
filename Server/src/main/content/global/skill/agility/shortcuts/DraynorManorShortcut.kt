package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityHandler
import content.global.skill.agility.AgilityShortcut
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import shared.consts.Animations

/**
 * Handles the drayor manor wall shortcut.
 */
@Initializable
class DraynorManorShortcut : AgilityShortcut(intArrayOf(37703), 28, 0.0, "squeeze-through") {

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val direction = if (player.location.x >= 3086) Direction.WEST else Direction.EAST
        val destination = player.location.transform(direction, 1)
        val animation = Animation.create(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844)

        player.lock(3)
        GameWorld.Pulser.submit(object : Pulse(1, player) {
            override fun pulse(): Boolean {
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    destination,
                    animation,
                    5,
                    0.0,
                    "You squeeze through the loose railing.",
                )
                return true
            }
        })
    }
}
