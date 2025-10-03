package content.global.skill.agility.courses.barbarian

import content.data.GameAttributes
import content.global.skill.agility.AgilityCourse
import content.global.skill.agility.AgilityHandler
import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds
import shared.consts.Scenery as Objects

/**
 * Handles the Barbarian Outpost Agility Course.
 */
@Initializable
class BarbarianOutpostCourse(player: Player? = null) : AgilityCourse(player, 6, 46.2) {

    companion object {
        /**
         * Represents scenery ids in course order.
         */
        private val COURSE_OBSTACLES = listOf(
            Objects.ROPESWING_2282,
            Objects.LOG_BALANCE_2294,
            Objects.OBSTACLE_NET_20211,
            Objects.BALANCING_LEDGE_2302,
            Objects.CRUMBLING_WALL_1948
        )

        /**
         * Represents the scorpions that damage the player.
         */
        private val SCORPIONS = listOf(
            NPCs.KHARID_SCORPION_385,
            NPCs.KHARID_SCORPION_386,
            NPCs.KHARID_SCORPION_387
        )
    }

    override fun configure() {
        val sceneryHandlers = listOf(
            Objects.GATE_2115 to "option:open",
            Objects.GATE_2116 to "option:open",
            Objects.ROPESWING_2282 to "option:swing-on",
            Objects.LOG_BALANCE_2294 to "option:walk-across",
            Objects.OBSTACLE_NET_20211 to "option:climb-over",
            Objects.BALANCING_LEDGE_2302 to "option:walk-across",
            Objects.CRUMBLING_WALL_1948 to "option:climb-over"
        )
        sceneryHandlers.forEach { (id, option) -> SceneryDefinition.forId(id).handlers[option] = this }
        ItemDefinition.forId(Items.BARCRAWL_CARD_455).handlers["option:read"] = this
        SCORPIONS.forEach { NPCDefinition.forId(it).handlers["option:pick-up"] = this }
    }

    override fun createInstance(player: Player): AgilityCourse = BarbarianOutpostCourse(player)

    /**
     * Handles player interaction with course objects.
     */
    override fun handle(player: Player, node: Node, option: String): Boolean {
        getCourse(player)
        return when (node.id) {
            Objects.GATE_2115, Objects.GATE_2116 -> { handleGate(player, node as Scenery); true }
            Objects.ROPESWING_2282 -> { handleRopeSwing(player, node as Scenery); true }
            Objects.LOG_BALANCE_2294 -> { handleLogBalance(player, node as Scenery); true }
            Objects.BALANCING_LEDGE_2302 -> { handleLedgeBalance(player, node as Scenery); true }
            Objects.OBSTACLE_NET_20211 -> { handleObstacleNet(player, node as Scenery); true }
            Objects.CRUMBLING_WALL_1948 -> { handleCrumblingWall(player, node as Scenery); true }
            Items.BARCRAWL_CARD_455 -> { BarcrawlManager.getInstance(player).read(); true }
            in SCORPIONS -> { impactScorpion(player); true }
            else -> false
        }
    }

    private fun handleGate(player: Player, gate: Scenery) {
        val barcrawl = BarcrawlManager.getInstance(player)
        if (!barcrawl.isFinished || barcrawl.isStarted()) {
            player.dialogueInterpreter.open(NPCs.BARBARIAN_GUARD_384)
        } else {
            DoorActionHandler.handleAutowalkDoor(player, gate)
        }
    }

    private fun handleObstacleNet(player: Player, net: Scenery) {
        if (player.location.x < net.location.x) return
        AgilityHandler.climb(
            player, 2, ClimbActionHandler.CLIMB_UP,
            player.location.transform(-2, 0, 1),
            8.0, "You climb the netting..."
        )
        trackLaps(player, true, net.id)
    }

    private fun handleCrumblingWall(player: Player, wall: Scenery) {
        if (player.location.x > wall.location.x) {
            sendMessage(player, "You cannot climb from this side.")
            return
        }
        val flag = when (wall.location) {
            Location(2536, 3553, 0) -> 4
            Location(2539, 3553, 0) -> 5
            else -> 6
        }
        sendMessage(player, "You climb the low wall...")
        AgilityHandler.forceWalk(
            player, flag,
            wall.location.transform(-2, 0, 0),
            wall.location.transform(2, 0, 0),
            Animation.create(Animations.CLIMB_OBJECT_839),
            10, 13.5, null
        )
        trackLaps(player, true, wall.id)
    }

    private fun impactScorpion(player: Player) {
        sendMessage(player, "The scorpion stings you!")
        impact(player, 3, HitsplatType.NORMAL)
    }

    private fun handleObstacle(
        player: Player,
        scenery: Scenery,
        failChance: Double,
        start: Location,
        endSuccess: Location,
        endFail: Location? = null,
        animation: Int,
        walkSpeed: Double,
        successMessage: String,
        failMessage: String,
        failAnimation: Int? = null,
        extraFailAction: ((Player) -> Unit)? = null
    ) {
        val failed = AgilityHandler.hasFailed(player, 1, failChance)

        if (failed) {
            sendMessage(player, failMessage)
            endFail?.let { AgilityHandler.walk(player, -1, start, it, Animation.create(animation), 0.0, null) }
            failAnimation?.let { player.animate(Animation.create(it)) }
            extraFailAction?.invoke(player)
        } else {
            AgilityHandler.forceWalk(player, 1, start, endSuccess, Animation.create(animation), walkSpeed.toInt(), 0.0, successMessage)
        }

        trackLaps(player, !failed, scenery.id)
    }

    private fun handleRopeSwing(player: Player, scenery: Scenery) {
        if (player.location.y < 3554) { sendMessage(player, "You cannot do that from here."); return }
        if (AgilityHandler.delay > GameWorld.ticks) { sendMessage(player, "The rope is being used."); return }
        animateScenery(player, scenery, 497, true)
        handleObstacle(
            player, scenery,
            failChance = 0.1,
            start = player.location,
            endSuccess = Location.create(2551, 3549, 0),
            animation = Animations.ROPE_SWING_751,
            walkSpeed = 22.0,
            successMessage = "You skillfully swing across.",
            failMessage = "You slip and fall to the pit below.",
            extraFailAction = { p -> AgilityHandler.fail(p, 0, Location.create(2549, 9951, 0), null, getHitAmount(p), "") }
        )
        AgilityHandler.delay = GameWorld.ticks + 2
        playAudio(player, Sounds.SWING_ACROSS_2494, 1)
    }

    private fun handleLogBalance(player: Player, scenery: Scenery) {
        sendMessage(player, "You walk carefully across the slippery log...")
        playAudio(player, Sounds.LOG_BALANCE_2470)
        handleObstacle(
            player, scenery,
            failChance = 0.5,
            start = Location.create(2551, 3546, 0),
            endSuccess = Location.create(2541, 3546, 0),
            endFail = Location.create(2545, 3546, 0),
            animation = 155,
            walkSpeed = 13.5,
            successMessage = "...You make it safely to the other side.",
            failMessage = "...You lose your footing and fall into the water.",
            extraFailAction = { GameWorld.Pulser.submit(getSwimPulse(player)) }
        )
    }

    private fun handleLedgeBalance(player: Player, scenery: Scenery) {
        sendMessage(player, "You put your foot on the ledge and try to edge across...")
        handleObstacle(
            player, scenery,
            failChance = 0.3,
            start = Location.create(2536, 3547, 1),
            endSuccess = Location.create(2532, 3547, 1),
            endFail = Location.create(2534, 3545, 0),
            animation = 157,
            walkSpeed = 22.0,
            successMessage = "You skillfully edge across the gap.",
            failMessage = "You slip and fall to the pit below.",
            failAnimation = Animations.FALL_FACE_DOWN_A_760
        )
    }

    private fun getSwimPulse(player: Player) = object : Pulse(1, player) {
        var counter = 0
        override fun pulse(): Boolean {
            counter++
            when (counter) {
                6 -> {
                    player.animate(Animation.create(771))
                    sendMessage(player, "...You lose your footing and fall into the water. Something in the water bites you.")
                }
                7 -> {
                    player.graphics(Graphics.create(shared.consts.Graphics.WATER_SPLASH_68))
                    player.properties.teleportLocation = Location.create(2545, 3545, 0)
                    player.impactHandler.manualHit(player, getHitAmount(player), HitsplatType.NORMAL)
                    AgilityHandler.walk(player, -1, Location.create(2545, 3545, 0), Location.create(2545, 3543, 0), Animation.create(152), 0.0, null)
                }
                11 -> {
                    player.properties.teleportLocation = Location.create(2545, 3542, 0)
                    return true
                }
            }
            return false
        }
    }

    private fun getLastObstacle(player: Player) = getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_LAST_OBSTACLE, -1)
    private fun setLastObstacle(player: Player, id: Int) = setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_LAST_OBSTACLE, id)

    private fun getCourseLaps(player: Player) = getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_LAPS, 0)
    private fun setCourseLaps(player: Player, laps: Int) = setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_LAPS, laps)

    private fun getPerfectLaps(player: Player) = getAttribute(player, GameAttributes.BARBARIAN_OUTPOST_PERFECT_LAPS, 0)
    private fun setPerfectLaps(player: Player, laps: Int) = setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_PERFECT_LAPS, laps)

    private fun hasCourseReward(player: Player) = player.attributes[GameAttributes.BARBARIAN_OUTPOST_COURSE_REWARD] == true
    private fun setCourseReward(player: Player, value: Boolean) = setAttribute(player, GameAttributes.BARBARIAN_OUTPOST_COURSE_REWARD, value)

    /**
     * Tracks progress through the course and awards laps.
     */
    private fun trackLaps(player: Player, success: Boolean, id: Int) {
        if (!success || getPerfectLaps(player) >= 250 || getLastObstacle(player) == id) return

        setLastObstacle(player, id)
        setCourseLaps(player, getCourseLaps(player) + 1)

        if (getCourseLaps(player) % COURSE_OBSTACLES.size == 0) setLastObstacle(player, -1)
        setPerfectLaps(player, getCourseLaps(player) / COURSE_OBSTACLES.size)

        if (getPerfectLaps(player) >= 250 && !hasCourseReward(player)) {
            setCourseReward(player, true)
            sendMessage(player, core.tools.RED + "You've completed 250 full laps! Speak to Gunnjorn to collect your reward.")
        }
    }
}
