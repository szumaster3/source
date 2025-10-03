package content.region.misthalin.varrock.quest.fluffs.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class FluffNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = FluffNPC(id, location)

    override fun isHidden(player: Player): Boolean {
        if (player.getQuestRepository().getQuest(Quests.GERTRUDES_CAT).getStage(player) < 20) {
            return true
        }
        return player.getAttribute(
            "hidefluff",
            0L,
        ) > System.currentTimeMillis()
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.GERTRUDES_CAT_2997)
    }
}
