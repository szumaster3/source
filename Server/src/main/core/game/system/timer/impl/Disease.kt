package core.game.system.timer.impl

import com.google.gson.JsonObject
import core.api.hasTimerActive
import core.api.playAudio
import core.api.removeTimer
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerFlag
import core.tools.RandomFunction
import shared.consts.Sounds

class Disease :
    PersistTimer(
        runInterval = 30,
        identifier = "disease",
        flags = arrayOf(TimerFlag.ClearOnDeath),
    ) {
    var hitsLeft = 25

    override fun save(
        root: JsonObject,
        entity: Entity,
    ) {
        root.addProperty("hitsLeft", hitsLeft)
    }

    override fun parse(
        root: JsonObject,
        entity: Entity,
    ) {
        hitsLeft = root.get("hitsLeft")?.asInt ?: 25
    }

    override fun onRegister(entity: Entity) {
        if (hasTimerActive<Disease>(entity)) {
            removeTimer(entity, this)
        } else if (entity is Player) {
            sendMessage(entity, "You have become diseased!")
        }
    }

    override fun run(entity: Entity): Boolean {
        if (entity is Player) {
            playAudio(entity, Sounds.DISEASE_HITSPLAT_2388)
        }

        val damage = RandomFunction.random(1, 5)

        // The disease hit is purely visual,
        // it doesn't deal any damage to the player.
        entity.impactHandler.visualHit(entity, damage, HitsplatType.DISEASE)

        var skillId = RandomFunction.random(24)
        if (skillId == 3) skillId--
        entity.skills.updateLevel(skillId, -damage, 0)
        if (--hitsLeft == 0 && entity is Player) {
            sendMessage(entity, "The disease has wore off.")
        }
        return hitsLeft > 0
    }

    override fun getTimer(vararg args: Any): RSTimer {
        val inst = Disease()
        inst.hitsLeft = args.getOrNull(0) as? Int ?: 25
        return inst
    }
}
