package content.region.misthalin.varrock.quest.romeo.plugin

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class RomeoNPC : AbstractNPC {
    constructor() : super(0, null, true)

    private constructor(id: Int, location: Location) : super(id, location, true)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = RomeoNPC(id, location)

    override fun init() {
        isWalks = true
        super.init()
        isWalks = true
    }

    override fun tick() {
        super.tick()
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.ROMEO_639)
        private const val speakDelay = 0
    }
}
