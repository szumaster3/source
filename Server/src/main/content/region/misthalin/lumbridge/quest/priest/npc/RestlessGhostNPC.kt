package content.region.misthalin.lumbridge.quest.priest.npc

import core.api.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class RestlessGhostNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = RestlessGhostNPC(id, location)

    override fun init() {
        super.init()
        if (this.asNpc().id == NPCs.RESTLESS_GHOST_457) {
            this.isWalks = false
            this.isNeverWalks = true
        }
        properties.combatPulse.style = CombatStyle.MELEE
    }

    override fun tick() {
        super.tick()
        val pl = getAttribute<Player>("player", null)
        if (pl != null) {
            if (getAttribute("dead", false) || !getLocation().withinDistance(pl.location, 16)) {
                clear()
            }
        }
    }

    override fun isAttackable(
        entity: Entity,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        val player = (entity as Player)
        val pl = getAttribute<Player>("player", null)
        return pl != null && pl == player
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        removeAttribute("player")
    }

    override fun isHidden(player: Player): Boolean {
        val pl = getAttribute<Player>("player", null)
        if (this.respawnTick > ticks) {
            return true
        }
        return isQuestComplete(player, Quests.THE_RESTLESS_GHOST) || (pl != null && player !== pl)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SKELETON_459, NPCs.RESTLESS_GHOST_457)
}
