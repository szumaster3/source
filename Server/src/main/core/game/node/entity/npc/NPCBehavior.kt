package core.game.node.entity.npc

import core.api.ContentInterface
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier
import core.game.world.map.path.Pathfinder

/**
 * Defines NPC behavior, allowing custom interactions and AI.
 * @property ids NPC ids this behavior applies to.
 */
open class NPCBehavior(vararg val ids: Int = intArrayOf()) : ContentInterface {
    companion object {
        private val idMap = HashMap<Int, NPCBehavior>()
        private val defaultBehavior = NPCBehavior()

        @JvmStatic
        fun forId(id: Int): NPCBehavior = idMap[id] ?: defaultBehavior

        fun register(ids: IntArray, behavior: NPCBehavior) {
            ids.forEach { idMap[it] = behavior }
        }
    }

    object StandardClipMaskSupplier : ClipMaskSupplier {
        override fun getClippingFlag(z: Int, x: Int, y: Int) = RegionManager.getClippingFlag(z, x, y)
    }

    open fun tick(self: NPC) = true

    open fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {}

    open fun afterDamageReceived(self: NPC, attacker: Entity, state: BattleState) {}

    open fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {}

    open fun onRemoval(self: NPC) {}

    open fun onCreation(self: NPC) {}

    open fun onRespawn(self: NPC) {}

    open fun onDeathStarted(self: NPC, killer: Entity) {}

    open fun onDeathFinished(self: NPC, killer: Entity) {}

    open fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {}

    open fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        if (attacker is Player && !self.definition.hasAction("attack")) return false
        return true
    }

    open fun shouldIgnoreMultiRestrictions(self: NPC, victim: Entity) = false

    open fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler) = original

    open fun getPathfinderOverride(self: NPC): Pathfinder? = null

    open fun getClippingSupplier(self: NPC): ClipMaskSupplier? = StandardClipMaskSupplier

    open fun getXpMultiplier(self: NPC, attacker: Entity) = 1.0
}
