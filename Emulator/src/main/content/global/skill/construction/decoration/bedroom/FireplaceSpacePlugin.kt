package content.global.skill.construction.decoration.bedroom

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import org.rs.consts.Animations
import org.rs.consts.Items

class FireplaceSpacePlugin : OptionHandler() {

    private val animationId = Animations.TINDERBOX_3658
    private val fireplaceSpaceFurniture = setOf(
        org.rs.consts.Scenery.CLAY_FIREPLACE_13609,
        org.rs.consts.Scenery.LIMESTONE_FIREPLACE_13611,
        org.rs.consts.Scenery.MARBLE_FIREPLACE_13613
    )

    override fun newInstance(arg: Any?): OptionHandler {
        fireplaceSpaceFurniture.forEach { id ->
            SceneryDefinition.forId(id).handlers["option:light"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (option != "light" || node.id !in fireplaceSpaceFurniture) return false

        val scenery = node as Scenery

        if (!player.inventory.contains(Items.LOGS_1511, 1) || !player.inventory.contains(Items.TINDERBOX_590, 1)) {
            sendMessage(player, "You need some logs and a tinderbox in order to light the fireplace.")
            return true
        }

        lock(player, 3)
        animate(player, animationId)

        queueScript(player, 2, QueueStrength.SOFT) {
            if (!node.isActive) {
                return@queueScript stopExecuting(player)
            }
            if (!removeItem(player, Item(Items.LOGS_1511, 1))) {
                sendMessage(player, "You need some logs in order to light the fireplace.")
                return@queueScript stopExecuting(player)
            }
            if (!removeItem(player, Item(Items.TINDERBOX_590, 1))) {
                sendMessage(player, "You need a tinderbox in order to light the fireplace.")
                return@queueScript stopExecuting(player)
            }
            rewardXP(player, Skills.FIREMAKING, 80.0)
            SceneryBuilder.replace(
                Scenery(scenery.id, scenery.location),
                Scenery(scenery.id + 1, scenery.location, scenery.rotation),
                1000
            )
            sendMessage(player, "You light the fireplace.")
            return@queueScript stopExecuting(player)
        }

        return true
    }
}
