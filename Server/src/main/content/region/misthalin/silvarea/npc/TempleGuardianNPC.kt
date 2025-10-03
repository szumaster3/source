package content.region.misthalin.silvarea.npc

import core.api.getQuestStage
import core.api.sendMessage
import core.api.setQuestStage
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class TempleGuardianNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = TempleGuardianNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.TEMPLE_GUARDIAN_7711)

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker
        if (player is Player) {
            if (state.style == CombatStyle.MELEE || state.style == CombatStyle.RANGE) {
                state.neutralizeHits()
                state.estimatedHit = state.maximumHit
            }
            if (state.style == CombatStyle.MAGIC) {
                sendMessage(player, "The dog doesn't seem to be affected.")
                if (state.estimatedHit > -1) {
                    state.estimatedHit = 0
                    return
                }
                if (state.secondaryHit > -1) {
                    state.secondaryHit = 0
                    return
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        val player = (killer as Player)
        if (getQuestStage(player, Quests.PRIEST_IN_PERIL) == 11) {
            setQuestStage(player, Quests.PRIEST_IN_PERIL, 12)
        }
    }
}
