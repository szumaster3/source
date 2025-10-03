package content.minigame.templetrekking.monsters

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs

class SwampSnakeNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = SwampSnakeNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.SWAMP_SNAKE_3599, NPCs.SWAMP_SNAKE_3600, NPCs.SWAMP_SNAKE_3601, NPCs.SWAMP_SNAKE_3602)

    companion object;

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
