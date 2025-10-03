package content.minigame.puropuro.plugin

import core.api.*
import core.api.setMinimapState
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneType
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Regions

@Initializable
class ImpetuousImpulses : MapZone("puro puro", true), Plugin<Any> {

    init {
        setZoneType(ZoneType.SAFE.id)
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        PULSE.stop()
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? = null

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val p = e.asPlayer()
            setMinimapState(p, 2)
        }
        if (!PULSE.isRunning) {
            spawnWheat()
            PULSE.restart()
            PULSE.start()
            Pulser.submit(PULSE)
        }
        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            val p = e.asPlayer()
            if (!logout) {
                setMinimapState(p, 0)
                closeAllInterfaces(p)
            }
        }
        return super.leave(e, logout)
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean = super.interact(e, target, option)

    private fun spawnWheat() {
        for (set in WHEAT) {
            set.init()
        }
    }

    override fun configure() {
        registerRegion(Regions.PURO_PURO_10307)
        WHEAT.add(WheatSet(0, Location.create(2606, 4329, 0), Location.create(2606, 4328, 0)))
        WHEAT.add(WheatSet(1, Location.create(2596, 4331, 0), Location.create(2597, 4331, 0)))
        WHEAT.add(WheatSet(0, Location.create(2580, 4326, 0), Location.create(2580, 4325, 0)))
        WHEAT.add(WheatSet(1, Location.create(2595, 4308, 0), Location.create(2596, 4308, 0)))
        WHEAT.add(WheatSet(0, Location.create(2603, 4314, 0), Location.create(2603, 4313, 0)))
        WHEAT.add(WheatSet(1, Location.create(2599, 4305, 0), Location.create(2600, 4305, 0)))
        WHEAT.add(WheatSet(0, Location.create(2577, 4327, 0), Location.create(2577, 4328, 0)))
        WHEAT.add(WheatSet(1, Location.create(2587, 4334, 0), Location.create(2586, 4334, 0)))
        WHEAT.add(WheatSet(0, Location.create(2609, 4310, 0), Location.create(2609, 4309, 0)))
        WHEAT.add(WheatSet(1, Location.create(2586, 4302, 0), Location.create(2587, 4302, 0)))
        WHEAT.add(WheatSet(0, Location.create(2574, 4310, 0), Location.create(2574, 4311, 0)))
        WHEAT.add(WheatSet(1, Location.create(2582, 4337, 0), Location.create(2581, 4337, 0)))
        WHEAT.add(WheatSet(0, Location.create(2571, 4316, 0), Location.create(2571, 4315, 0)))
        WHEAT.add(WheatSet(1, Location.create(2601, 4340, 0), Location.create(2602, 4340, 0)))
        WHEAT.add(WheatSet(0, Location.create(2612, 4324, 0), Location.create(2612, 4323, 0)))
        WHEAT.add(WheatSet(1, Location.create(2584, 4296, 0), Location.create(2583, 4296, 0)))
        WHEAT.add(WheatSet(0, Location.create(2568, 4329, 0), Location.create(2568, 4330, 0)))
        WHEAT.add(WheatSet(1, Location.create(2595, 4343, 0), Location.create(2596, 4343, 0)))
        WHEAT.add(WheatSet(0, Location.create(2615, 4315, 0), Location.create(2615, 4314, 0)))
        WHEAT.add(WheatSet(1, Location.create(2601, 4293, 0), Location.create(2600, 4293, 0)))
        WHEAT.add(WheatSet(0, Location.create(2565, 4310, 0), Location.create(2565, 4311, 0)))
        WHEAT.add(WheatSet(1, Location.create(2582, 4346, 0), Location.create(2583, 4346, 0)))
        WHEAT.add(WheatSet(0, Location.create(2568, 4348, 0), Location.create(2568, 4347, 0)))
        WHEAT.add(WheatSet(0, Location.create(2615, 4347, 0), Location.create(2615, 4348, 0)))
        WHEAT.add(WheatSet(0, Location.create(2612, 4345, 0), Location.create(2612, 4344, 0)))
        WHEAT.add(WheatSet(0, Location.create(2614, 4292, 0), Location.create(2614, 4291, 0)))
        WHEAT.add(WheatSet(0, Location.create(2568, 4292, 0), Location.create(2568, 4291, 0)))
        WHEAT.add(WheatSet(0, Location.create(2571, 4295, 0), Location.create(2571, 4294, 0)))
        WHEAT.add(WheatSet(0, Location.create(2575, 4297, 0), Location.create(2575, 4298, 0)))
        WHEAT.add(WheatSet(0, Location.create(2584, 4330, 0), Location.create(2584, 4329, 0)))
        WHEAT.add(WheatSet(0, Location.create(2599, 4329, 0), Location.create(2599, 4330, 0)))
        WHEAT.add(WheatSet(1, Location.create(2602, 4312, 0), Location.create(2601, 4312, 0)))
        WHEAT.add(WheatSet(1, Location.create(2610, 4312, 0), Location.create(2611, 4312, 0)))
        WHEAT.add(WheatSet(1, Location.create(2570, 4309, 0), Location.create(2569, 4309, 0)))
        WHEAT.add(WheatSet(0, Location.create(2583, 4304, 0), Location.create(2583, 4303, 0)))
    }

    /**
     * Represents a set of wheat patches.
     *
     * @property rot The rotation applied to all scenery objects in this set.
     * @property locations The locations of each wheat patch in this set.
     */
    class WheatSet(private val rot: Int, vararg locations: Location) {

        /**
         * The list of locations for this wheat set.
         */
        val locations: List<Location> = locations.toList()

        /**
         * The array of scenery objects corresponding to each location.
         */
        private val scenery = arrayOfNulls<Scenery>(locations.size)

        /**
         * The game tick when the next whilt can occur.
         */
        var nextWhilt: Int = 0
            private set

        /**
         * The tick when the wheat set is busy performing an action.
         */
        private var busyTicks = 0

        /**
         * Whether the wheat has been removed (used for animation state).
         */
        private var removed = false

        /**
         * Initializes the scenery objects for this wheat set.
         */
        fun init() {
            for ((index, location) in locations.withIndex()) {
                val scenery = Scenery(25021, location, 22, rot)
                SceneryBuilder.add(scenery)
                this.scenery[index] = scenery
            }
            setNextWhilt()
        }

        /**
         * Handles the action on all wheat patches in this set.
         *
         * This handles animation, removal, and adding of scenery objects, and
         * schedules pulses for the animation timing.
         */
        fun whilt() {
            busyTicks = ticks + 5
            for (`object` in scenery) {
                if (`object` == null) continue
                if (removed) {
                    submitWorldPulse(object : Pulse() {
                        var counter = 0
                        override fun pulse(): Boolean {
                            if (counter++ == 0) {
                                animateScenery(`object`, 6596)
                                delay = animationDuration(Animation(6596))
                                return false
                            }
                            return true
                        }
                    })
                    addScenery(`object`)
                    continue
                }
                submitWorldPulse(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        if (counter++ == 0) {
                            animateScenery(`object`, 6599)
                            delay = animationDuration(Animation(6599))
                            return false
                        }
                        removeScenery(`object`)
                        return true
                    }
                })
            }
            removed = !removed
            setNextWhilt()
        }

        /**
         * Resets scenery objects based on their locations.
         */
        fun setScenery() {
            for (i in locations.indices) scenery[i] = getObject(locations[i])
        }

        /**
         * Schedules the next whilt tick randomly.
         */
        fun setNextWhilt() {
            nextWhilt = ticks + RandomFunction.random(40, 300)
        }

        /**
         * Checks if the wheat can currently whilt.
         */
        fun canWhilt(): Boolean = ticks > nextWhilt && ticks > busyTicks
    }

    companion object {
        private val WHEAT: MutableList<WheatSet> = ArrayList(20)

        private val PULSE: Pulse =
            object : Pulse(1) {
                override fun pulse(): Boolean {
                    for (set in WHEAT) {
                        if (set.canWhilt()) {
                            set.whilt()
                        }
                    }
                    return false
                }
            }
    }
}
