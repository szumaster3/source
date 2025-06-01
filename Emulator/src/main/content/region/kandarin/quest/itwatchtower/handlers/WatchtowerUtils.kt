package content.region.kandarin.quest.itwatchtower.handlers

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Graphics

object WatchtowerUtils {

    @JvmStatic
    fun handleGatePassage(player: Player, loc: Location, openGate: Boolean) {
        lock(player, 4)
        teleport(player, loc, TeleportManager.TeleportType.INSTANT)

        if (openGate) {
            val scenery = getScenery(2504, 3063, 0)
            scenery?.asScenery()?.let { DoorActionHandler.handleAutowalkDoor(player, it) }
        } else {
            sendMessage(player, "The guard pushes you back down the hill.")

            val max = player.skills.maximumLifepoints
            val damage = (max * 0.05).toInt().coerceAtLeast(1)
            impact(player, damage, ImpactHandler.HitsplatType.NORMAL)

            sendGraphics(Graphics.STUN_BIRDIES_ABOVE_HEAD_80, player.location)
            sendChat(player, "Urrrrrgh!")
        }
    }
}