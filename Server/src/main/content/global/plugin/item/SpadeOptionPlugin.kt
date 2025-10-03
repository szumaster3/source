package content.global.plugin.item

import core.api.playAudio
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.global.action.DigSpadeHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import shared.consts.Sounds

/**
 * A plugin that handles the "Dig" option on the spade item.
 */
@Initializable
class SpadeOptionPlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any>? {
        ItemDefinition.forId(Items.SPADE_952).handlers["option:dig"] = this
        return null
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        playAudio(player, Sounds.DIGSPADE_1470)
        if (!DigSpadeHandler.dig(player)) {
            sendMessage(player, "You dig but find nothing.")
        }
        return true
    }

    override fun isWalk(): Boolean = false
}
