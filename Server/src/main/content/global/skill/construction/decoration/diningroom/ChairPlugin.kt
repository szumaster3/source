package content.global.skill.construction.decoration.diningroom

import core.api.forceMove
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the sit-on interaction for chairs and benches in the poh.
 */
@Initializable
class ChairPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (chair in Chairs.values()) {
            SceneryDefinition.forId(chair.objectId).handlers["option:sit-on"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node as Scenery
        val chair = Chairs.fromId(scenery.id) ?: return false

        var animID = chair.anim
        val sitAnimID = chair.sitAnim

        if (scenery.type == 11) animID++

        forceMove(player, player.location, scenery.location, 0, 30, null, sitAnimID)
        player.locks.lockInteractions(600_000)

        player.pulseManager.run(object : Pulse(2) {
            override fun pulse(): Boolean {
                player.animate(Animation.create(animID))
                return false
            }

            override fun stop() {
                super.stop()
                player.locks.unlockInteraction()
                player.animate(Animation.create(sitAnimID + 2))
            }
        })

        return true
    }
}
