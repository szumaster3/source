package content.region.kandarin.quest.itwatchtower.handlers

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Graphics

object WatchtowerUtils {

    @JvmStatic
    fun handleGatePassage(player: Player, loc: Location, openGate: Boolean) {
        teleport(player, loc, TeleportManager.TeleportType.INSTANT)

        if (openGate) {
            val scenery = getScenery(2504, 3063, 0)
            scenery?.asScenery()?.let { DoorActionHandler.handleAutowalkDoor(player, it) }
        } else {
            lock(player, 9)
            openInterface(player, Components.FADE_TO_BLACK_115)
            val max = player.skills.maximumLifepoints
            val damage = (max * 0.05).toInt().coerceAtLeast(1)
            queueScript(player, 1, QueueStrength.SOFT) { stage : Int ->
                when(stage) {
                    0 -> {
                        sendMessage(player, "The guard pushes you back down the hill.")
                        openInterface(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript delayScript(player, 6)
                    }
                    1 -> {
                        impact(player, damage, ImpactHandler.HitsplatType.NORMAL)
                        sendGraphics(Graphics.STUN_BIRDIES_ABOVE_HEAD_80, player.location)
                        return@queueScript delayScript(player, 3)
                    }
                    2 -> {
                        sendChat(player, "Urrrrrgh!")
                        return@queueScript stopExecuting(player)
                    }
                    else ->  return@queueScript stopExecuting(player)
                }
            }
        }
    }
}