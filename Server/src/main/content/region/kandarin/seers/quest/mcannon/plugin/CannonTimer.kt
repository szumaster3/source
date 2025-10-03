package content.region.kandarin.seers.quest.mcannon.plugin

import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.RSTimer
import core.tools.colorize

class CannonTimer : RSTimer(1, "dmc:timer") {
    lateinit var dmcHandler: content.region.kandarin.seers.quest.mcannon.plugin.DMCHandler
    var ticksUntilDecay = 2500
    var isFiring = false

    override fun run(entity: Entity): Boolean {
        if (entity !is Player) {
            return false
        }
        if (!dmcHandler.cannon.isActive) {
            return false
        }
        if (isFiring) {
            isFiring = dmcHandler.rotate()
        }
        if (--ticksUntilDecay == 500) {
            sendMessage(entity, colorize("%RYour cannon is about to decay."))
        } else if (ticksUntilDecay == 0) {
            dmcHandler.explode(true)
        }
        return ticksUntilDecay > 0 && dmcHandler.cannon.isActive
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val t = retrieveInstance() as CannonTimer
        t.dmcHandler = args[0] as content.region.kandarin.seers.quest.mcannon.plugin.DMCHandler
        return t
    }
}
