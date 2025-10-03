package core.game.system.timer.impl

import content.data.GameAttributes
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag

class Teleblock :
    PersistTimer(
        runInterval = 1,
        identifier = GameAttributes.TELEBLOCK_TIMER,
        flags = arrayOf(TimerFlag.ClearOnDeath),
    ) {
    override fun run(entity: Entity): Boolean = false

    override fun onRegister(entity: Entity) {
        if (entity !is Player) return
        sendMessage(entity, "You have been teleblocked.")
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val t = Teleblock()
        t.runInterval = args.getOrNull(0) as? Int ?: 100
        return t
    }
}
