package content.region.fremennik.lighthouse.quest.horror.npc

import core.api.location
import core.game.node.entity.player.Player
import shared.consts.NPCs

class DagannothSession(
    val player: Player,
) {
    private val dagannoth: DagannothMotherNPC =
        DagannothMotherNPC(NPCs.DAGANNOTH_MOTHER_1351, location(2520, 4645, 0), this)

    init {
        if (player.getExtension<Any?>(DagannothSession::class.java) != null) {
            player.removeExtension(DagannothSession::class.java)
        }
        player.addExtension(DagannothSession::class.java, this)
    }

    fun start() {
        dagannoth.init()
        player.unlock()
    }

    fun close() {
        dagannoth.clear()
        player.removeExtension(DagannothSession::class.java)
    }

    companion object {
        @JvmStatic
        fun create(player: Player): DagannothSession = DagannothSession(player)

        @JvmStatic
        fun getSession(player: Player): DagannothSession = player.getExtension(DagannothSession::class.java)
    }
}
