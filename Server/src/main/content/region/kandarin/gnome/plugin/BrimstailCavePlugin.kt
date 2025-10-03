package content.region.kandarin.gnome.plugin

import content.global.travel.EssenceTeleport
import core.api.sendDialogue
import core.api.sendNPCDialogue
import core.api.teleport
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Scenery

class BrimstailCavePlugin : InteractionListener {
    companion object {
        private val CAVE_EXIT = intArrayOf(Scenery.TUNNEL_17222, Scenery.TUNNEL_17223)
        private const val CAVE_ENTER = Scenery.CAVE_ENTRANCE_17209
        private const val BRIMSTAIL = NPCs.BRIMSTAIL_171
        private const val TABLE = Scenery.TABLE_17235
        private const val ASPIDISTRA_PLANT = Scenery.ASPIDISTRA_PLANT_17238
    }

    override fun defineListeners() {
        on(CAVE_ENTER, IntType.SCENERY, "enter") { player, _ ->
            teleport(player, Location.create(2408, 9812, 0))
            return@on true
        }

        on(CAVE_EXIT, IntType.SCENERY, "exit") { player, _ ->
            teleport(player, Location(2402, 3419, 0))
            return@on true
        }

        on(TABLE, IntType.SCENERY, "take-bowl") { player, _ ->
            sendNPCDialogue(
                player,
                BRIMSTAIL,
                "Stop, I don't want you to spill water on my books!",
                FaceAnim.OLD_ANGRY1,
            )
            return@on true
        }

        on(ASPIDISTRA_PLANT, IntType.SCENERY, "search") { player, _ ->
            sendDialogue(player, "Gronda Gronda!")
            return@on true
        }

        on(BRIMSTAIL, IntType.NPC, "teleport") { player, node ->
            EssenceTeleport.teleport(node.asNpc(), player)
            return@on true
        }
    }
}
