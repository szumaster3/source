package content.minigame.fistofguthix.plugin

import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Scenery

class FOGListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CAVE_ENTRANCE_30204, IntType.SCENERY, "enter") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location.create(1675, 5599, 0))
            return@on true
        }

        on(Scenery.STAIRS_30203, IntType.SCENERY, "exit") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location.create(3242, 3574, 0))
            return@on true
        }
    }
}
