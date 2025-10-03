package content.region.kandarin.feldip.jiggig.quest.zogre.npc

import content.region.kandarin.feldip.jiggig.quest.zogre.plugin.ZogreUtils
import core.api.*
import core.api.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

@Initializable
class SlashBashNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var despawnTime = 0
    private val player: Player? = null

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = SlashBashNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.SLASH_BASH_2060)

    override fun handleTickActions() {
        super.handleTickActions()
        if (player != null) {
            if (player.location.getDistance(getLocation()) > 10 || !player.isActive || despawnTime++ > 1030) {
                removeAttribute(player, ZogreUtils.SLASH_BASH_ACTIVE)
                poofClear(this)
            }
        }
    }

    companion object {
        const val OGRE_ARTIFACT = Items.OGRE_ARTEFACT_4818

        @JvmStatic
        fun spawnSlashBash(player: Player) {
            val boss = SlashBashNPC(NPCs.SLASH_BASH_2060)
            val spawnGraphics =
                core.game.world.update.flag.context.Graphics(Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86)
            boss.location = Location.getRandomLocation(Location(2480, 9445, 0), 2, true)
            boss.isWalks = true
            boss.isAggressive = true
            boss.isActive = false

            if (boss.asNpc() != null && boss.isActive) {
                boss.properties.teleportLocation = boss.properties.spawnLocation
            }
            boss.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(2, boss) {
                    override fun pulse(): Boolean {
                        registerLogoutListener(player, "slash-bash") { _ ->
                            boss.clear()
                        }
                        setAttribute(player, ZogreUtils.SLASH_BASH_ACTIVE, true)
                        sendMessage(player, "Something stirs behind you!")
                        visualize(boss, -1, spawnGraphics)
                        boss.init()
                        registerHintIcon(player, boss)
                        boss.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker
        if (player is Player) {
            if (state.spell != null && state.spell.spellId == 22 || inEquipment(player, Items.COMP_OGRE_BOW_4827)) {
                state.estimatedHit = (state.estimatedHit * 0.5).toInt()
                if (state.secondaryHit > 0) {
                    state.secondaryHit = (state.secondaryHit * 0.5).toInt()
                }
                return
            } else {
                state.estimatedHit = (state.estimatedHit * 0.25).toInt()
                if (state.secondaryHit > 0) {
                    state.secondaryHit = (state.secondaryHit * 0.25).toInt()
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val player = killer.asPlayer()
            produceGroundItem(player, OGRE_ARTIFACT, 1, this.location)
            produceGroundItem(player, Items.OURG_BONES_4834, RandomFunction.random(1, 3), this.location)
            produceGroundItem(player, Items.ZOGRE_BONES_4812, RandomFunction.random(1, 2), this.location)
            setVarbit(player!!, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487, 12, true)
            removeAttribute(player, ZogreUtils.SLASH_BASH_ACTIVE)
        }
        clearHintIcon(killer!!.asPlayer())
        clear()
        super.finalizeDeath(killer)
    }
}