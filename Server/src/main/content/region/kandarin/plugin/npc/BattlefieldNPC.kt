package content.region.kandarin.plugin.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles Battlefield NPC behavior.
 */
@Initializable
class BattlefieldNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private var nextActionTime = 0L
    private var isGnome = false

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        BattlefieldNPC(id, location)

    override fun init() {
        super.init()
        isGnome = id in GNOMES
    }

    override fun tick() {
        super.tick()
        val currentTime = System.currentTimeMillis()
        if (currentTime < nextActionTime) return

        val victim = properties.combatPulse.victim

        if (victim == null) {
            findAndAttackEnemy()
        } else {
            if (RandomFunction.random(9) == 0) {
                chatDuringCombat()
            }
        }

        nextActionTime = currentTime + RandomFunction.random(3000, 5000)
    }

    private fun findAndAttackEnemy() {
        val target = getNearbyEnemies().firstOrNull() ?: return
        target.lock(5)
        properties.combatPulse.attack(target)
    }

    private fun chatDuringCombat() {
        val enemy = properties.combatPulse.victim ?: return
        if (enemy.location.getDistance(location) > 4) return

        val shouldChat = (enemy as? NPC)?.id?.let { getOpposingFaction().contains(it) } ?: true
        if (shouldChat) {
            sendChat(getRandomChat())
        }
    }

    private fun getNearbyEnemies(): List<NPC> {
        val opposingFaction = getOpposingFaction()
        return getLocalNpcs(this, 8).filter { npc ->
            npc != this && npc.id in opposingFaction && !npc.properties.combatPulse.isAttacking
        }
    }

    private fun getOpposingFaction(): IntArray = if (isGnome) TROOPERS else GNOMES

    private fun getRandomChat(): String =
        if (isGnome) GNOMES_FORCE_CHAT.random() else TROOPERS_FORCE_CHAT.random()

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray = ID

    companion object {
        private val ID =
            intArrayOf(
                NPCs.KHAZARD_TROOPER_2245,
                NPCs.KHAZARD_TROOPER_2246,
                NPCs.GNOME_67,
                NPCs.MOUNTED_TERRORBIRD_GNOME_1752,
                NPCs.GNOME_TROOP_2247,
                NPCs.GNOME_2249,
                NPCs.GNOME_2250,
                NPCs.GNOME_2251,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6109,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6110,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6111
            )
        private val GNOMES =
            intArrayOf(
                NPCs.GNOME_67,
                NPCs.MOUNTED_TERRORBIRD_GNOME_1752,
                NPCs.GNOME_TROOP_2247,
                NPCs.GNOME_2249,
                NPCs.GNOME_2250,
                NPCs.GNOME_2251,
                NPCs.TORTOISE_3808,
                NPCs.MOUNTED_TERRORBIRD_GNOME_6109
            )
        private val TROOPERS = intArrayOf(NPCs.KHAZARD_TROOPER_2245, NPCs.KHAZARD_TROOPER_2246)
        private val TROOPERS_FORCE_CHAT =
            arrayOf(
                "Get off of me, ya little pest!",
                "Die, you filthy goblin!",
                "Victory will be ours!",
                "We will crush you!"
            )
        private val GNOMES_FORCE_CHAT =
            arrayOf(
                "Stop right there, Khazard scum!",
                "Tally Ho!",
                "We are the true force of the battlefield!",
                "Feel the wrath of the goblins!"
            )
    }
}
