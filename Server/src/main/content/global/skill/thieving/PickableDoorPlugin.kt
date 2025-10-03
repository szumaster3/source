package content.global.skill.thieving

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

/**
 * Handles the interaction with pickable doors in the thieving skill.
 */
@Initializable
class PickableDoorPlugin : OptionHandler() {

    var door: PickableDoor? = null

    /**
     * Initializes the plugin by adding handlers for the pick-lock and open actions for various doors.
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (i in DOORS) {
            SceneryDefinition.forId(i).handlers["option:pick-lock"] = this
            SceneryDefinition.forId(i).handlers["option:open"] = this
        }

        pickableDoors.add(PickableDoor(arrayOf(Location.create(3014, 3182, 0)), 1, 0.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2672, 3308, 0)), 1, 3.8))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2672, 3301, 0)), 14, 15.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2610, 3316, 0)), 15, 15.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(3190, 3957, 0)), 32, 25.0, true))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2565, 3356, 0)), 46, 37.5))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2579, 3286, 1)), 61, 50.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2579, 3307, 1)), 61, 50.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(3018, 3187, 0)), 1, 0.0))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(2601, 9482, 0)), 82, 0.0, true))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(3044, 3956, 0)), 39, 35.0, true, true))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(3041, 3959, 0)), 39, 35.0, true, true))
        pickableDoors.add(PickableDoor(arrayOf(Location.create(3038, 3956, 0)), 39, 35.0, true, true))
        return this
    }

    /**
     * Handles the interaction for opening or picking the lock on a door.
     */
    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        door = forDoor(node.location)
        if (option == "open") {
            if (door == null) {
                sendMessage(player, "The door is locked.")
                return true
            }
            door!!.open(player, node as Scenery)
            return true
        }
        if (option == "pick-lock") {
            if (door == null) {
                sendMessage(player, "This door cannot be unlocked.")
                return true
            }
            door!!.pickLock(player, node as Scenery)
            return true
        }
        return false
    }

    /**
     * Gets the destination location of a door when it is interacted with.
     */
    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is Scenery) {
            val `object` = n
            if (`object`.definition.hasAction("pick-lock")) {
                return DoorActionHandler.getDestination((node as Entity), `object`)
            }
        }
        return null
    }

    /**
     * Finds a pickable door based on the given location.
     * @param location The location of the door to search for.
     * @return The [PickableDoor] object if found, or `null` if not found.
     */
    private fun forDoor(location: Location): PickableDoor? {
        for (door in pickableDoors) {
            for (l in door.locations) {
                if (l == location) {
                    return door
                }
            }
        }
        return null
    }

    /**
     * Represents a pickable door.
     *
     * @param locations The locations where this door exists.
     * @param level The thieving level required to interact with the door.
     * @param experience The experience rewarded for interacting with the door.
     * @param isLockpick Whether a lockpick is required to unlock the door.
     * @param flipped Whether the door is flipped for a specific interaction logic.
     */
    inner class PickableDoor(val locations: Array<Location>, val level: Int, val experience: Double, val isLockpick: Boolean = false, private val flipped: Boolean = false) {
        /**
         * Opens the door if the player is on the correct side of the door.
         * @param player The player opening the door.
         * @param door The door being opened.
         */
        fun open(player: Player, door: Scenery) {
            if (isInside(player, door) != flipped) {
                handleAutowalkDoor(player, door.asScenery())
                sendMessage(player, "You go through the door.")
            } else {
                sendMessage(player, "The door is locked.")
            }
        }

        /**
         * Attempts to pick the lock on the door. If successful, the door is opened.
         * @param player The player attempting to pick the lock.
         * @param door The door being unlocked.
         */
        fun pickLock(
            player: Player,
            door: Scenery,
        ) {
            val success = RandomFunction.random(12) >= 4
            if (isInside(player, door.asScenery()) != flipped) {
                sendMessage(player, "The door is already unlocked.")
                return
            }
            if (getStatLevel(player, Skills.THIEVING) < level) {
                sendMessage(player, "You attempt to pick the lock.")
                val hit = RandomFunction.random(10) < 5
                impact(player, RandomFunction.random(1, 3), ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, if (hit) "You have activated a trap on the lock." else "You fail to pick the lock.", 1)
                return
            }
            if (isLockpick && !inInventory(player, LOCK_PICK)) {
                sendMessage(player, "You need a lockpick in order to pick this lock.")
                return
            }
            if (success) {
                rewardXP(player, Skills.THIEVING, experience)
                handleAutowalkDoor(player, door.asScenery())
                escape(player)
            }
            sendMessage(player, "You attempt to pick the lock.")
            sendMessage(player, "You " + (if (success) "manage" else "fail") + " to pick the lock.", 1)
        }

        /**
         * Checks if the player is on the correct side of the door to interact with it.
         *
         * @param player The player interacting with the door.
         * @param door The door being interacted with.
         * @return True if the player is inside the door, otherwise false.
         */
        private fun isInside(
            player: Player,
            door: Scenery,
        ): Boolean {
            var inside = false
            val dir = Direction.getLogicalDirection(player.location, door.location)
            val direction = door.direction
            if (direction == Direction.SOUTH && dir == Direction.WEST) {
                inside = true
            } else if (direction == Direction.EAST && dir == Direction.SOUTH) {
                inside = true
            } else if (direction == Direction.NORTH && dir == Direction.EAST) {
                inside = true
            }
            return inside
        }
    }

    /**
     * Escapes the player from certain conditions like being in Shantay Jail.
     * @param player The player escaping.
     */
    private fun escape(player: Player) {
        if (getAttribute(player, "shantay-jail", false)) {
            removeAttribute(player, "shantay-jail")
            sendNPCDialogueWithDelay(
                player,
                2,
                NPCs.SHANTAY_836,
                "You should be in jail! But if you get into more trouble, you'll be back.",
            )
        }
    }

    companion object {
        private val LOCK_PICK = Items.LOCKPICK_1523
        private val pickableDoors: MutableList<PickableDoor> = ArrayList(20)
        private val DOORS = intArrayOf(2550, 2551, 2554, 2555, 2556, 2557, 2558, 2559, 5501, 7246, 9565, 13314, 13317, 13320, 13323, 13326, 13344, 13345, 13346, 13347, 13348, 13349, 15759, 34005, 34805, 34806, 34812, 40186, 42028)
    }
}
