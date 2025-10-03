package core.game.system.timer.impl

import com.google.gson.JsonObject
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag

class Frozen :
    PersistTimer(
        runInterval = 1,
        identifier = "frozen",
        flags = arrayOf(TimerFlag.ClearOnDeath),
    ) {
    var shouldApplyImmunity = false

    override fun save(
        root: JsonObject,
        entity: Entity,
    ) {
        root.addProperty("ticksLeft", (nextExecution - getWorldTicks()))
        root.addProperty("applyImmunity", shouldApplyImmunity)
    }

    override fun parse(
        root: JsonObject,
        entity: Entity,
    ) {
        runInterval = root.get("ticksLeft")?.asInt ?: runInterval
        shouldApplyImmunity = root.get("applyImmunity")?.asBoolean ?: false
    }

    override fun onRegister(entity: Entity) {
        if (hasTimerActive<FrozenImmunity>(entity)) {
            removeTimer(entity, this)
            return
        }
        if (hasTimerActive<Frozen>(entity)) {
            removeTimer(entity, this)
            return
        }
    }

    override fun run(entity: Entity): Boolean {
        if (shouldApplyImmunity) {
            registerTimer(entity, spawnTimer<FrozenImmunity>(7))
        } else {
            (entity as? Player)?.debug("Can't apply immunity")
        }
        return false
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val inst = Frozen()
        inst.runInterval = args.getOrNull(0) as? Int ?: 10
        inst.shouldApplyImmunity = args.getOrNull(1) as? Boolean ?: false
        return inst
    }
}
