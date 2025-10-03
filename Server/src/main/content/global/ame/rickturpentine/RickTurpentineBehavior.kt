package content.global.ame.rickturpentine

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Represents the Rick Turpentine behavior.
 * @author Zerken
 */
class RickTurpentineBehavior : NPCBehavior(NPCs.RICK_TURPENTINE_2476) {
    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        state.estimatedHit = RandomFunction.getRandom(3)
    }
}
