package content.region.asgarnia.trollheim.plugin.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class TrollNPC : AbstractNPC {
    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location, true) {
        this.isAggressive = true
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = TrollNPC(id, location)

    override fun getIds(): IntArray = ID

    companion object {
        val ID = intArrayOf(
            NPCs.MOUNTAIN_TROLL_1106,
            NPCs.MOUNTAIN_TROLL_1107,
            NPCs.MOUNTAIN_TROLL_1108,
            NPCs.MOUNTAIN_TROLL_1109,
            NPCs.MOUNTAIN_TROLL_1110,
            NPCs.MOUNTAIN_TROLL_1111,
            NPCs.MOUNTAIN_TROLL_1112,
            NPCs.MOUNTAIN_TROLL_1138,
        )
    }
}
