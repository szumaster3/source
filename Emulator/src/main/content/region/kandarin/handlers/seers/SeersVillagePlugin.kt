package content.region.kandarin.handlers.seers

import core.api.*
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.shops.Shops.Companion.openId
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class SeersVillagePlugin : OptionHandler() {

    companion object {
        private const val COURTHOUSE_STAIRS = org.rs.consts.Scenery.STAIRS_26017
        private const val CRATE = org.rs.consts.Scenery.CRATE_6839
        private const val TICKET_MERCHANT = NPCs.TICKET_MERCHANT_694
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(COURTHOUSE_STAIRS).handlers["option:climb-down"] = this
        SceneryDefinition.forId(CRATE).handlers["option:buy"] = this
        NPCDefinition.forId(TICKET_MERCHANT).handlers["option:trade"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "climb-down" -> {
                if (node is Scenery && node.getId() == COURTHOUSE_STAIRS) {
                    sendMessage(player, "Court is not in session.")
                    return true
                }
            }
            "buy" -> {
                if (node is Scenery && node.getId() == CRATE) {
                    openId(player, 93)
                    return true
                }
            }
            "trade" -> {
                if (node is NPC && node.getId() == TICKET_MERCHANT) {
                    openInterface(player, Components.RANGING_GUILD_TICKET_EXCHANGE_278)
                    return true
                }
            }
        }
        return false
    }
}