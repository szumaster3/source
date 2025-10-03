package content.minigame.magearena

import content.minigame.magearena.npc.KolodionNPC
import content.minigame.magearena.npc.KolodionSession
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Plugin

/**
 * Handles the mage arena zone.
 * @author Vexia
 */
class MageArena : MapZone("mage arena", true, ZoneRestriction.RANDOM_EVENTS), Plugin<Any> {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any): Any? {
        return null
    }

    override fun enter(e: Entity): Boolean {
        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            val p = e
            if (!logout) {
                Pulser.submit(object : Pulse(1, e) {
                    override fun pulse(): Boolean {
                        if (!p.zoneMonitor.isInZone("mage arena")) {
                            if (hasSession(p)) {
                                getSession(p).close()
                            }
                        }
                        return true
                    }
                })
                return true
            }
            if (hasSession(p)) {
                getSession(p).close()
            }
            if (logout && hasSession(p)) {
                p.location = Location.create(2540, 4717, 0)
            }
        }
        return super.leave(e, logout)
    }

    override fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (style != CombatStyle.MAGIC) {
            return false
        }
        if (target is Player) {
            if (hasSession(target) && e !is KolodionNPC) {
                return false
            }
        }
        return super.continueAttack(e, target, style, message)
    }

    /**
     * Gets the session.
     * @param player the player.
     * @return the session.
     */
    fun getSession(player: Player?): KolodionSession {
        return KolodionSession.getSession(player!!)
    }

    /**
     * Checks if the player has a session.
     * @param player the player.
     * @return `True` if so.
     */
    fun hasSession(player: Player?): Boolean {
        return KolodionSession.getSession(player!!) != null
    }

    override fun configure() {
        register(ZoneBorders(3093, 3914, 3115, 3952)) // the main
        register(ZoneBorders(3084, 3923, 3086, 3942))
        register(ZoneBorders(3118, 3924, 3126, 3941))
        register(ZoneBorders(3082, 3921, 3096, 3942))
        var x = 3089
        var y = 3914
        var x2 = 3089
        var y2 = 3949
        for (i in 0..6) {
            register(ZoneBorders(x - i, y + i, x2 - i, y2 - i))
        }
        register(ZoneBorders(3119, 3942, 3123, 3945))
        register(ZoneBorders(3115, 3944, 3119, 3949))
        register(ZoneBorders(3119, 3942, 3119, 3952))
        register(ZoneBorders(3120, 3940, 2125, 3945))
        register(ZoneBorders(3114, 3944, 3120, 3949))
        register(ZoneBorders(3114, 3942, 3117, 3952))
        x = 3118
        y = 3914
        x2 = 3118
        y2 = 3950
        for (i in 0..6) {
            register(ZoneBorders(x + i, y + i, x2 + i, y2 - i))
        }
        register(ZoneBorders(3107, 3931, 3118, 3947))
        register(ZoneBorders(3108, 3919, 3123, 3932))
        register(ZoneBorders(3108, 3921, 3123, 3932))
        register(ZoneBorders(3104, 3933, 3117, 3948))
        register(ZoneBorders(3106, 3920, 3117, 3937))
        register(ZoneBorders(3115, 3928, 3120, 3942))
    }
}