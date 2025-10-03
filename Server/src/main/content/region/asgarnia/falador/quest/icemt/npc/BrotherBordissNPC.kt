package content.region.asgarnia.falador.quest.icemt.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class BrotherBordissNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        BrotherBordissNPC(id, location)

    override fun getIds(): IntArray = ID

    companion object {
        private val ID = intArrayOf(NPCs.BROTHER_BORDISS_7724)
    }
}