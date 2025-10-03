package content.minigame.templetrekking.monsters

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs

class GhastNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = GhastNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.JUVINATE_3576, NPCs.JUVINATE_3577, NPCs.JUVINATE_3578, NPCs.JUVINATE_7418)

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            clear()
            super.finalizeDeath(killer)
        }
    }
}
