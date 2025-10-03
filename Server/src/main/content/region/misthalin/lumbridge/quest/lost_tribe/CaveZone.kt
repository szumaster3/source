package content.region.misthalin.lumbridge.quest.lost_tribe

import core.api.resetAnimator
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Animations

@Initializable
class CaveZone :
    MapZone("TLT Cave Zone", true),
    Plugin<Unit> {
    val triggers = ArrayList<Location>()

    override fun configure() {
        register(ZoneBorders(3306, 9661, 3222, 9600))
        triggers.add(Location.create(3241, 9614, 0))
        triggers.add(Location.create(3257, 9616, 0))
        triggers.add(Location.create(3258, 9633, 0))
        triggers.add(Location.create(3244, 9635, 0))
        triggers.add(Location.create(3233, 9626, 0))
        triggers.add(Location.create(3249, 9646, 0))
        triggers.add(Location.create(3260, 9638, 0))
        triggers.add(Location.create(3273, 9641, 0))
        triggers.add(Location.create(3275, 9631, 0))
        triggers.add(Location.create(3287, 9631, 0))
        triggers.add(Location.create(3302, 9618, 0))
    }

    override fun move(e: Entity, from: Location, to: Location): Boolean {
        if (triggers.contains(from) && e is Player) {
            e.asPlayer().walkingQueue.reset()
            e.asPlayer().lock()
            trigger(e.asPlayer())
        }
        return super.move(e, from, to)
    }

    fun trigger(player: Player) {
        if (RandomFunction.random(1, 6) <= 2) {
            player.animator.animate(
                Animation(Animations.FALL_TRAPDOOR_1950),
                Graphics(shared.consts.Graphics.HOLE_OPENS_IN_GROUND_572, 1, 3),
            )
            GameWorld.Pulser.submit(
                object : Pulse(5) {
                    override fun pulse(): Boolean {
                        player.unlock()
                        player.properties.teleportLocation = Location.create(3159, 9546, 0)
                        resetAnimator(player)
                        return true
                    }
                },
            )
        } else {
            player.unlock()
            player.animator.animate(
                Animation.RESET,
                Graphics(shared.consts.Graphics.ROCKS_FALLING_ON_HEAD_302),
            )
            player.impactHandler.manualHit(player, RandomFunction.random(1, 7), ImpactHandler.HitsplatType.NORMAL)
        }
    }

    override fun newInstance(arg: Unit?): Plugin<Unit> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any = Unit
}
