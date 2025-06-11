package content.global.skill.construction.decoration.combatroom

import core.api.animationCycles
import core.api.forceMove
import core.api.inEquipment
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CombatRingPlugin : OptionHandler() {

    private val combatRings = intArrayOf(
        Scenery.BOXING_RING_13129,
        Scenery.FENCING_RING_13133,
        Scenery.COMBAT_RING_13137,
    )

    override fun newInstance(arg: Any?): OptionHandler {
        combatRings.forEach {
            SceneryDefinition.forId(it).handlers["option:climb-over"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (option != "climb-over" || node.id !in combatRings) return false

        val direction = when (node.direction) {
            Direction.WEST -> Direction.SOUTH
            Direction.SOUTH -> Direction.EAST
            Direction.EAST -> Direction.NORTH
            else -> Direction.WEST
        }
        val destination = player.location.transform(direction, 1)
        val animation = getAnimation(player)

        player.walkingQueue.reset()
        forceMove(player, player.location, destination, 0, animationCycles(animation), direction, animation)

        return true
    }

    private fun getAnimation(player: Player): Int =
        when {
            inEquipment(player, Items.BOXING_GLOVES_7671) -> Animations.HUMAN_JUMP_RING_RED_GLOVES_3689
            inEquipment(player, Items.BOXING_GLOVES_7673) -> Animations.JUMP_OVER_BOXING_RING_FENCE_WITH_BLUE_GLOVES_3690
            else -> Animations.HUMAN_JUMP_BOXING_RING_3688
        }

}
