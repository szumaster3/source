package content.global.skill.agility

import core.api.getStatLevel
import core.api.inEquipment
import core.api.sendMessage
import core.api.utils.Vector
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Base class for agility shortcuts.
 *
 * @property ids The shortcut scenery object IDs.
 * @property level Required agility level.
 * @property experience Experience reward.
 * @property canFail Whether the shortcut can fail.
 * @property failChance Chance to fail (0.0–1.0).
 * @property options Interaction options.
 *
 * @author Woah
 */
abstract class AgilityShortcut(
    val ids: IntArray?,
    val level: Int,
    val experience: Double,
    val canFail: Boolean = false,
    var failChance: Double = 0.0,
    vararg val options: String?
) : OptionHandler() {

    constructor(ids: IntArray?, level: Int, experience: Double, vararg options: String?) : this(
        ids, level, experience, false, 0.0, *options
    )

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(this)
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (!checkRequirements(player)) return true
        run(player, node.asScenery(), option, checkFail(player, node.asScenery(), option))
        return true
    }

    /**
     * Executes the shortcut logic.
     *
     * @param player The player using the shortcut.
     * @param scenery The obstacle scenery.
     * @param option The interaction option.
     * @param failed Whether the attempt failed.
     */
    abstract fun run(player: Player, scenery: Scenery, option: String, failed: Boolean)

    /**
     * Checks if the player meets requirements.
     */
    open fun checkRequirements(player: Player): Boolean {
        if (inEquipment(player, Items.SLED_4084, 1)) {
            sendMessage(player, "You can't do that on a sled.")
            return false
        }
        if (getStatLevel(player, Skills.AGILITY) < level) {
            sendMessage(player, "You need an agility level of at least $level to negotiate this obstacle.")
            return false
        }
        return true
    }

    /**
     * Check if the shortcut attempt failed based on the [failChance].
     *
     * @param player The player attempting the shortcut.
     * @param scenery The obstacle scenery.
     * @param option The chosen interaction option.
     * @return `True` if the attempt failed, `false` otherwise.
     */
    private fun checkFail(player: Player, scenery: Scenery, option: String): Boolean {
        if (!canFail) return false
        return AgilityHandler.hasFailed(player, level, failChance)
    }

    /**
     * Registers shortcut handlers to associated scenery objects.
     *
     * @param shortcut The agility shortcut instance.
     */
    fun configure(shortcut: AgilityShortcut) {
        shortcut.ids?.forEach { id ->
            val def = SceneryDefinition.forId(id)
            shortcut.options.filterNotNull().forEach { op ->
                def.handlers["option:$op"] = shortcut
            }
        }
    }

    /**
     * Returns the given direction rotated 90 degrees clockwise.
     *
     * @param direction The original direction.
     * @return The rotated direction.
     */
    protected fun getObjectDirection(direction: Direction): Direction {
        val x = direction.stepX
        val y = direction.stepY
        return Direction.getDirection(y, -x)
    }

    /**
     * Calculates the target location after moving a given number of steps through a pipe obstacle.
     *
     * @param player The player moving.
     * @param scenery The pipe obstacle.
     * @param steps Number of steps to move.
     * @return The destination location.
     */
    fun pipeDestination(player: Player, scenery: Scenery, steps: Int): Location {
        player.faceLocation(scenery.location)
        val origin = Vector(player.location)
        val direction = Vector.betweenLocs(player.location, scenery.location).normalized()
        return (origin + direction * steps).toLocation()
    }
}
