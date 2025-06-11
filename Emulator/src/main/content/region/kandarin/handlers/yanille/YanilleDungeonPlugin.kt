package content.region.kandarin.handlers.yanille

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.api.event.applyPoison
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items

@Initializable
class YanilleDungeonPlugin : OptionHandler() {

    private val sinisterChestContent = arrayOf(
        Item(Items.GRIMY_HARRALANDER_205, 2),
        Item(Items.GRIMY_RANARR_207, 3),
        Item(Items.GRIMY_IRIT_209),
        Item(Items.GRIMY_AVANTOE_211),
        Item(Items.GRIMY_KWUARM_213),
        Item(Items.GRIMY_TORSTOL_219)
    )

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.STAIRCASE_1728).handlers["option:climb-down"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.STAIRCASE_1729).handlers["option:climb-up"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.STAIRCASE_2316).handlers["option:climb-up"] = this

        SceneryDefinition.forId(org.rs.consts.Scenery.PILE_OF_RUBBLE_2318).handlers["option:climb-up"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.PILE_OF_RUBBLE_2317).handlers["option:climb-down"] = this

        SceneryDefinition.forId(org.rs.consts.Scenery.BALANCING_LEDGE_35969).handlers["option:walk-across"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.BALANCING_LEDGE_2303).handlers["option:walk-across"] = this

        SceneryDefinition.forId(org.rs.consts.Scenery.CLOSED_CHEST_377).handlers["option:open"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OPEN_CHEST_378).handlers["option:search"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OPEN_CHEST_378).handlers["option:shut"] = this

        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (node !is Scenery) return false

        when (option) {
            "climb-down" -> {
                when (node.getId()) {
                    org.rs.consts.Scenery.STAIRCASE_1728 -> {
                        teleport(player, Location(2620, 9565, 0))
                        return true
                    }

                    org.rs.consts.Scenery.PILE_OF_RUBBLE_2317 -> {
                        return handlePileOfRubble(player, node, climbUp = false)
                    }
                }
            }

            "climb-up" -> {
                when (node.getId()) {
                    org.rs.consts.Scenery.STAIRCASE_1729 -> {
                        teleport(player, Location(2620, 9496, 0))
                        return true
                    }

                    org.rs.consts.Scenery.STAIRCASE_2316 -> {
                        teleport(player, Location(2569, 9525, 0))
                        return true
                    }

                    org.rs.consts.Scenery.PILE_OF_RUBBLE_2318 -> {
                        return handlePileOfRubble(player, node, climbUp = true)
                    }
                }
            }

            "walk-across" -> {
                if (node.getId() == org.rs.consts.Scenery.BALANCING_LEDGE_35969 || node.getId() == org.rs.consts.Scenery.BALANCING_LEDGE_2303) {
                    return handleBalancingLedge(player, node)
                }
            }

            "open" -> {
                if (node.getId() == org.rs.consts.Scenery.CLOSED_CHEST_377) {
                    return handleSinisterChestOpen(player, node)
                }
            }
        }

        return false
    }

    private fun handlePileOfRubble(player: Player, node: Scenery, climbUp: Boolean): Boolean {
        if (getStatLevel(player, Skills.AGILITY) < 67) {
            sendMessage(player, "You need an agility level of at least 67 in order to do this.")
            return true
        }

        lock(player, 3)

        queueScript(player, 1, QueueStrength.SOFT) {
            val destination = if (climbUp) Location(2614, 9504, 0) else Location(2617, 9571, 0)
            ClimbActionHandler.climb(
                player, if (climbUp) ClimbActionHandler.CLIMB_UP else ClimbActionHandler.CLIMB_DOWN, destination
            )
            sendMessage(player, "You climb ${if (climbUp) "up" else "down"} the pile of rubble...")
            rewardXP(player, Skills.AGILITY, 5.5)
            return@queueScript stopExecuting(player)
        }

        return true
    }

    private fun handleBalancingLedge(player: Player, node: Scenery): Boolean {
        if (getStatLevel(player, Skills.AGILITY) < 40) {
            sendMessage(player, "You need an agility level of at least 40 in order to do this.")
            return true
        }

        val dir = Direction.getLogicalDirection(player.location, node.location)
        val diff = if (player.location.y == 9512) 0 else 1

        val failChance = AgilityHandler.hasFailed(player, 40, 0.01)

        if (failChance) {
            lock(player, 3)
            GameWorld.Pulser.submit(object : Pulse(2, player) {
                override fun pulse(): Boolean {
                    AgilityHandler.fail(
                        player,
                        1,
                        Location(2572, 9570, 0),
                        Animation.create(761 - diff),
                        RandomFunction.random(1, 3),
                        "You lost your balance!"
                    )
                    runTask(player, 2) {
                        animate(player, Animations.ON_BACK_GET_UP_BRUSH_SELF_5056, true)
                    }
                    return true
                }
            })
        } else {
            val end = node.location.transform(dir.stepX * 7, dir.stepY * 7, 0)
            AgilityHandler.walk(player, -1, player.location, end, Animation.create(157 - diff), 22.5, null)
        }

        return true
    }

    private fun handleSinisterChestOpen(player: Player, node: Scenery): Boolean {
        if (!inInventory(player, Items.SINISTER_KEY_993)) {
            sendMessage(player, "The chest is locked.")
            return true
        }

        if (freeSlots(player) == 0) {
            sendMessage(player, "You don't have enough inventory space.")
            return true
        }

        lock(player, 1)

        if (removeItem(player, Items.SINISTER_KEY_993)) {
            sendMessage(player, "You unlock the chest with your key... A foul gas seeps from the chest.")
            applyPoison(player, player, 2)

            sinisterChestContent.forEach { item ->
                addItemOrDrop(player, item.id, item.amount)
            }

            SceneryBuilder.replace(node, node.transform(node.id + 1), 5)
        }

        return true
    }
}
