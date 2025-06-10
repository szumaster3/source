package content.region.kandarin.handlers.feldip

import content.region.kandarin.dialogue.ooglog.GrimechinDialogue
import core.api.openDialogue
import core.game.interaction.OptionHandler
import core.game.node.entity.player.Player
import core.game.node.Node
import core.cache.def.impl.SceneryDefinition
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Scenery

@Initializable
class OoglogPlugin : OptionHandler() {

    companion object {
        private const val GRIMECHIN_ID = Scenery.GRIMECHIN_29106
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        SceneryDefinition.forId(GRIMECHIN_ID).handlers["option:talk-to"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (node.id == GRIMECHIN_ID && option.equals("talk-to", ignoreCase = true)) {
            openDialogue(player, GrimechinDialogue())
            return true
        }
        return false
    }
}
