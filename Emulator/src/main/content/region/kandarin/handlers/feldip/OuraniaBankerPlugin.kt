package content.region.kandarin.handlers.feldip

import core.api.interaction.restrictForIronman
import core.api.openInterface
import core.api.setAttribute
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class OuraniaBankerPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        NPCDefinition.forId(NPCs.ENIOLA_6362).apply {
            handlers["option:bank"] = this@OuraniaBankerPlugin
            handlers["option:collect"] = this@OuraniaBankerPlugin
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        restrictForIronman(player, IronmanMode.ULTIMATE) {
            when (option) {
                "bank" -> setAttribute(player, "zmi:bankaction", "open")
                "collect" -> setAttribute(player, "zmi:bankaction", "collect")
                else -> return@restrictForIronman
            }
            openInterface(player, Components.BANK_CHARGE_ZMI_619)
        }
        return true
    }

    override fun isWalk(): Boolean = false
}
