package content.global.skill.agility.courses

import content.data.GameAttributes
import content.global.skill.agility.AgilityCourse
import core.api.getAttribute
import core.api.playAudio
import core.api.sendMessage
import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.DARK_GREEN
import shared.consts.Sounds

@Initializable
class GnomeStrongholdCourse : AgilityCourse {

    constructor() : super(null, 7, 39.0)

    constructor(player: Player?) : super(player, 7, 39.0)

    companion object {
        private val USED_PIPES = IntArray(2)
        private val TRAINERS = arrayOfNulls<NPC>(5)
    }

    override fun configure() {
        TRAINERS[0] = NPC.create(162, Location.create(2473, 3438, 0))
        TRAINERS[1] = NPC.create(162, Location.create(2478, 3426, 0))
        TRAINERS[2] = NPC.create(162, Location.create(2474, 3422, 1))
        TRAINERS[3] = NPC.create(162, Location.create(2472, 3419, 2))
        TRAINERS[4] = NPC.create(162, Location.create(2489, 3425, 0))

        for (npc in TRAINERS) {
            npc!!.init()
            npc.walkRadius = 3
        }

        SceneryDefinition.forId(2295).handlers["option:walk-across"] = this
        SceneryDefinition.forId(2285).handlers["option:climb-over"] = this
        SceneryDefinition.forId(35970).handlers["option:climb"] = this
        SceneryDefinition.forId(2312).handlers["option:walk-on"] = this
        SceneryDefinition.forId(4059).handlers["option:walk-on"] = this
        SceneryDefinition.forId(2314).handlers["option:climb-down"] = this
        SceneryDefinition.forId(2315).handlers["option:climb-down"] = this
        SceneryDefinition.forId(2286).handlers["option:climb-over"] = this
        SceneryDefinition.forId(4058).handlers["option:squeeze-through"] = this
        SceneryDefinition.forId(154).handlers["option:squeeze-through"] = this
    }

    override fun createInstance(player: Player): AgilityCourse {
        return GnomeStrongholdCourse(player)
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        getCourse(player!!)
        val scenery = node as Scenery
        when (scenery.id) {
            2295 -> {
                TRAINERS[0]!!.sendChat("Okay get over that log, quick quick!")
                sendMessage(player, "You walk carefully across the slippery log...")
                playAudio(player, Sounds.LOG_BALANCE_2470)
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    0,
                    Location.create(2474, 3436, 0),
                    Location.create(2474, 3429, 0),
                    Animation.create(155),
                    7.5,
                    "...You make it safely to the other side.",
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            2285 -> {
                TRAINERS[1]!!.sendChat("Move it, move it, move it!")
                playAudio(player, Sounds.CLIMB_WALL_2453)
                sendMessage(player, "You climb the netting...")
                content.global.skill.agility.AgilityHandler.climb(
                    player,
                    1,
                    Animation.create(828),
                    node.location.transform(0, -1, 1),
                    7.5,
                    null,
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            35970 -> {
                TRAINERS[2]!!.sendChat("That's it - straight up.")
                playAudio(player, Sounds.TREE_CLIMBING_1705)
                sendMessage(player, "You climb the tree..")
                content.global.skill.agility.AgilityHandler.climb(
                    player,
                    2,
                    Animation.create(828),
                    Location.create(2473, 3420, 2),
                    5.0,
                    "...To the platform above.",
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            2312 -> {
                TRAINERS[3]!!.sendChat("Come on scaredy cat, get across that rope!")
                playAudio(player, Sounds.ROPECLIMB_2482)
                sendMessage(player, "You carefully cross the tightrope.")
                content.global.skill.agility.AgilityHandler.walk(
                    player,
                    3,
                    Location.create(2477, 3420, 2),
                    Location.create(2483, 3420, 2),
                    Animation.create(155),
                    7.5,
                    null,
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            4059 -> {
                sendMessage(player, "You can't do that from here.")
                return true
            }

            2314, 2315 -> {
                playAudio(player, Sounds.TREE_CLIMBING_1705)
                sendMessage(player, "You climb down the tree..")
                content.global.skill.agility.AgilityHandler.climb(
                    player,
                    4,
                    Animation.create(828),
                    Location.create(2487, 3420, 0),
                    5.0,
                    "You land on the ground.",
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            2286 -> {
                TRAINERS[4]!!.sendChat("My Granny can move faster than you.")
                player.faceLocation(player.location.transform(0, 2, 0))
                playAudio(player, Sounds.CLIMB_WALL_2453)
                sendMessage(player, "You climb the netting...")
                content.global.skill.agility.AgilityHandler.climb(
                    player,
                    5,
                    Animation.create(828),
                    player.location.transform(0, 2, 0),
                    7.5,
                    null,
                )
                trackLaps(player, true, scenery.id)
                return true
            }

            4058, 154 -> {
                val index = if (node.id == 154) 0 else 1
                val x = 2484 + index * 3
                if (node.location.y == 3435) {
                    sendMessage(player, "You can't do that from here.")
                    return true
                }
                if (USED_PIPES[index] > GameWorld.ticks) {
                    sendMessage(player, "The pipe is being used.")
                    return true
                }
                USED_PIPES[index] = GameWorld.ticks + 10
                player.lock()
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    -1,
                    Location.create(x, 3430, 0),
                    Location.create(x, 3433, 0),
                    Animation.create(10580),
                    10,
                    0.0,
                    null,
                )
                player.lock()
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    -1,
                    Location.create(x, 3433, 0),
                    Location.create(x, 3435, 0),
                    Animation.create(844),
                    10,
                    0.0,
                    null,
                    5,
                )
                player.lock()
                content.global.skill.agility.AgilityHandler.forceWalk(
                    player,
                    6,
                    Location.create(x, 3435, 0),
                    Location.create(x, 3437, 0),
                    Animation.create(10579),
                    20,
                    7.5,
                    null,
                    8,
                )
                return true
            }
        }
        return false
    }


    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        val node = n as Scenery
        when (node.id) {
            2295 -> return Location.create(2474, 3436, 0)
            2286 -> {
                var x = node.location.x
                if (x < n.getLocation().x) {
                    x = n.getLocation().x
                } else if (x > n.getLocation().x + 1) {
                    x = n.getLocation().x + 1
                }
                return Location.create(x, n.getLocation().y - 1, 0)
            }

            4058, 154 -> if (n.getLocation().y == 3431) {
                return n.getLocation().transform(0, -1, 0)
            }
        }
        return null
    }

    private fun trackLaps(player: Player, passedObstacle: Boolean, obstacleId: Int) {
        val lastObstacle = getLastObstacle(player)

        if (lastObstacle == obstacleId) {
            return
        }

        if (passedObstacle) {
            setLastObstacle(player, obstacleId)

            val currentPoints = getAttribute(player, GameAttributes.GNOME_STRONGHOLD_COURSE_LAPS, 0)
            val pass = currentPoints + 1
            val perfectLaps = pass / 6

            setAttribute(player, GameAttributes.GNOME_STRONGHOLD_COURSE_LAPS, pass)
            if (pass % 6 == 0) {
                setAttribute(player, GameAttributes.GNOME_STRONGHOLD_PERFECT_LAPS, perfectLaps)
                player.debug("[GnomeStronghold] ${DARK_GREEN}Perfect lap+</col> | Total amount: [$perfectLaps]")
            }

            if (perfectLaps >= 250 && player.attributes[GameAttributes.GNOME_STRONGHOLD_COURSE_REWARD] != true) {
                setAttribute(player, GameAttributes.GNOME_STRONGHOLD_COURSE_REWARD, true)
                sendMessage(
                    player,
                    core.tools.RED + "You've completed 250 perfect laps! Speak to a gnome trainer for your reward."
                )
            }
        }
    }

    private fun getLastObstacle(player: Player): Int {
        return getAttribute(player, GameAttributes.GNOME_STRONGHOLD_LAST_OBSTACLE, -1)
    }

    private fun setLastObstacle(player: Player, obstacleId: Int) {
        setAttribute(player, GameAttributes.GNOME_STRONGHOLD_LAST_OBSTACLE, obstacleId)
    }

}
