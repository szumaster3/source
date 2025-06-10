package content.region.kandarin.handlers.gnomestronghold

import content.global.travel.EssenceTeleport
import core.api.sendDialogue
import core.api.sendNPCDialogue
import core.api.teleport
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import core.game.dialogue.FaceAnim

@Initializable
class BrimstailCavePlugin : OptionHandler() {

    companion object {
        private val CAVE_EXIT = intArrayOf(Scenery.TUNNEL_17222, Scenery.TUNNEL_17223)
        private const val CAVE_ENTER = Scenery.CAVE_ENTRANCE_17209
        private const val BRIMSTAIL = NPCs.BRIMSTAIL_171
        private const val TABLE = Scenery.TABLE_17235
        private const val ASPIDISTRA_PLANT = Scenery.ASPIDISTRA_PLANT_17238
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        // Scenery options
        SceneryDefinition.forId(CAVE_ENTER).handlers["option:enter"] = this
        CAVE_EXIT.forEach { SceneryDefinition.forId(it).handlers["option:exit"] = this }
        SceneryDefinition.forId(TABLE).handlers["option:take-bowl"] = this
        SceneryDefinition.forId(ASPIDISTRA_PLANT).handlers["option:search"] = this

        // NPC option
        NPCDefinition.forId(BRIMSTAIL).handlers["option:teleport"] = this

        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "enter" -> {
                teleport(player, Location.create(2408, 9812, 0))
                return true
            }
            "exit" -> {
                teleport(player, Location(2402, 3419, 0))
                return true
            }
            "take-bowl" -> {
                sendNPCDialogue(
                    player,
                    BRIMSTAIL,
                    "Stop, I don't want you to spill water on my books!",
                    FaceAnim.OLD_ANGRY1
                )
                return true
            }
            "search" -> {
                sendDialogue(player, "Gronda Gronda!")
                return true
            }
            "teleport" -> {
                EssenceTeleport.teleport(node as NPC, player)
                return true
            }
        }
        return false
    }

    override fun isWalk(): Boolean = false
}
