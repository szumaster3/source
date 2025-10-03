package content.region.morytania.swamp.quest.druidspirit.npc

import core.api.poofClear
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class FillimanTarlockNPC : AbstractNPC {
    var spawnedTicks = 0

    constructor() : super(NPCs.FILLIMAN_TARLOCK_1050, null, true)
    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC = FillimanTarlockNPC(id, location)

    init {
        isNeverWalks = true
        isWalks = false
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (spawnedTicks++ > 100) poofClear(this)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FILLIMAN_TARLOCK_1050)
}
