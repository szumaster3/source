package content.global.plugin.item

import core.api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.interaction.OptionHandler
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Graphics
import shared.consts.Items

@Initializable
class SnowGlobePlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        ClassScanner.definePlugin(SnowGlobeInterface())
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean = true

    inner class SnowGlobeInterface : ComponentPlugin() {
        override fun newInstance(arg: Any?): Plugin<Any?> {
            ComponentDefinition.put(Components.SNOWGLOBE_INTERFACE_659, this)
            return this
        }

        override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
            when (button) {
                2 -> {
                    closeInterface(player)
                    animate(player, Animations.SNOWGLOBE_SNOW_FALL_SLOW_7538)
                    queueScript(player, 1, QueueStrength.WEAK) {
                        visualize(player, Animations.SNOWGLOBE_STOMP_7528, Graphics.SNOW_FALLING_FROM_SNOW_GLOBE_1284)
                        player.inventory.add(Item(Items.SNOWBALL_11951, freeSlots(player)))
                        return@queueScript stopExecuting(player)
                    }
                    return true
                }

                else -> {
                    animate(player, Animations.SNOWGLOBE_SNOW_FALL_FAST_7537)
                    return true
                }
            }
        }
    }
}
