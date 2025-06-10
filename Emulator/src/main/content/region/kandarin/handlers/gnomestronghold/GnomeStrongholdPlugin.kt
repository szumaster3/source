package content.region.kandarin.handlers.gnomestronghold

import content.global.skill.agility.AgilityHandler
import content.region.kandarin.dialogue.stronghold.GnomeGateGuardDialogue
import core.api.inBorders
import core.api.openDialogue
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Quests

@Initializable
class GnomeStrongholdPlugin : OptionHandler() {

    companion object {
        val GRAND_TREE = ZoneBorders(2465, 3491, 2466, 3493)
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        SceneryDefinition.forId(190).handlers["option:open"] = this
        SceneryDefinition.forId(1967).handlers["option:open"] = this
        SceneryDefinition.forId(1968).handlers["option:open"] = this
        SceneryDefinition.forId(9316).handlers["option:climb"] = this
        SceneryDefinition.forId(9317).handlers["option:climb"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node as Scenery
        when (scenery.id) {
            9316, 9317 -> {
                val scale = player.location.y <= scenery.location.y
                val end = scenery.location.transform(if (scale) 3 else -3, if (scale) 6 else -6, 0)
                if (getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
                    sendDialogue(
                        player, "You feel eyes upon you. It wouldn't be a good idea to enter this way right now."
                    )
                    return true
                }
                if (!player.skills.hasLevel(Skills.AGILITY, 37)) {
                    sendMessage(player, "You must be level 37 agility or higher to climb down the rocks.")
                    return true
                }
                if (!scale) {
                    ForceMovement.run(player, player.location, end, Animation.create(740), Animation.create(740), Direction.SOUTH, 13).endAnimation = Animation.RESET
                } else {
                    ForceMovement.run(player, player.location, end, Animation.create(1148), Animation.create(1148), Direction.SOUTH, 13).endAnimation = Animation.RESET
                }
                return true
            }

            1967, 1968 -> {
                if (GRAND_TREE.insideBorder(player)) {
                    openTreeDoor(player, scenery)
                } else {
                    sendMessage(player, "I can't reach that.")
                }
                return true
            }

            190 -> {
                openGates(player, scenery)
                return true
            }
        }
        return true
    }

    private fun openTreeDoor(player: Player, scenery: Scenery) {
        if (scenery.charge == 88) return
        scenery.charge = 88
        val newId = if (scenery.id == 1967) 1969 else 1970
        SceneryBuilder.replace(scenery, scenery.transform(newId), 4)
        AgilityHandler.walk(
            player,
            -1,
            player.location,
            player.location.transform(0, if (player.location.y <= 3491) 2 else -2, 0),
            Animation(1426),
            0.0,
            null,
            false
        )

        GameWorld.Pulser.submit(object : Pulse(4) {
            override fun pulse(): Boolean {
                scenery.charge = 1000
                return true
            }
        })
    }

    private fun openGates(player: Player, scenery: Scenery) {
        if (inBorders(player, 2460, 3381, 2463, 3383) && getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
            openDialogue(player, GnomeGateGuardDialogue())
        } else {
            if (scenery.charge == 0) return
            scenery.charge = 0
            SceneryBuilder.replace(scenery, scenery.transform(191), 4)
            SceneryBuilder.add(Scenery(192, Location.create(2462, 3383, 0)), 4)
            var start = Location.create(2461, 3382, 0)
            var end = Location.create(2461, 3385, 0)
            if (player.location.y > scenery.location.y) {
                val temp = start
                start = end
                end = temp
            }
            Pathfinder.find(player, end).walk(player)
            GameWorld.Pulser.submit(object : Pulse(4) {
                override fun pulse(): Boolean {
                    scenery.charge = 1000
                    return true
                }
            })
        }
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is Scenery) {
            if (n.id == 190) {
                return if (node.location.y < n.location.y) {
                    Location.create(2461, 3382, 0)
                } else {
                    Location.create(2461, 3385, 0)
                }
            }
        }
        return null
    }
}
