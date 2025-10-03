package content.region.kandarin.guild.ranging.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Tower archer NPC.
 */
@Initializable
class TowerArcherNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun getIds(): IntArray = intArrayOf(NPCs.TOWER_ARCHER_688, NPCs.TOWER_ARCHER_689, NPCs.TOWER_ARCHER_690, NPCs.TOWER_ARCHER_691)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC = TowerArcherNPC(id, location)

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }
}
