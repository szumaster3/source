package content.region.misthalin.varrock.quest.dragon.npc

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

class MazeSkeletonNPC : AbstractNPC {
    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = MazeSkeletonNPC(id, location)

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer.location.withinDistance(LOCATION)) {
            if (killer is Player) {
                if (RandomFunction.random(0, 5) == 2) {
                    GroundItemManager.create(DragonSlayer.YELLOW_KEY, getLocation(), killer)
                }
            }
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SKELETON_90)

    companion object {
        private val LOCATION: Location = Location.create(2927, 3253, 2)
    }
}
