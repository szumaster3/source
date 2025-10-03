package content.global.ame.freakyforest

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.system.task.Pulse
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

/**
 * Handles interaction for Freaky forester random event.
 * @author szu
 */
class FreakyForesterPlugin : InteractionListener, MapArea {
    private val exitPortal = Scenery.PORTAL_8972
    private val freakNpc = NPCs.FREAKY_FORESTER_2458
    private val pheasants = intArrayOf(NPCs.PHEASANT_2459, NPCs.PHEASANT_2460, NPCs.PHEASANT_2461, NPCs.PHEASANT_2462)

    override fun defineListeners() {
        on(freakNpc, IntType.NPC, "talk-to") { player, node ->
            if (inBorders(player, FreakyForesterUtils.freakArea)) {
                if (getAttribute(player, GameAttributes.RE_FREAK_TASK, -1) == -1) {
                    FreakyForesterUtils.giveFreakTask(
                        player,
                    )
                }
                openDialogue(player, FreakyForesterDialogue(), node.asNpc())
            } else {
                sendDialogue(player, "Freaky Forester is not interested in talking.")
            }
            return@on true
        }

        on(pheasants, IntType.NPC, "attack") { player, node ->
            if (getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false)) {
                sendDialogue(player, "You don't need to attack any more pheasants. You're allowed to leave.")
            } else if (inInventory(player, Items.RAW_PHEASANT_6178) ||
                inInventory(player, Items.RAW_PHEASANT_6179) ||
                getAttribute(player, GameAttributes.RE_FREAK_KILLS, false)
            ) {
                sendDialogue(player, "You don't need to attack any more pheasants.")
            } else {
                player.attack(node.asNpc())
            }
            return@on true
        }

        on(exitPortal, IntType.SCENERY, "enter") { player, _ ->
            if (getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false)) {
                FreakyForesterUtils.cleanup(player)
                submitWorldPulse(
                    object : Pulse(2) {
                        override fun pulse(): Boolean {
                            FreakyForesterUtils.reward(player)
                            return true
                        }
                    },
                )
                return@on true
            } else {
                sendMessage(player, "A supernatural force prevents you from leaving.")
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2587, 4758, 2616, 4788))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    override fun areaEnter(entity: Entity) {
        entity.locks.lockTeleport(1000000)
    }
}
