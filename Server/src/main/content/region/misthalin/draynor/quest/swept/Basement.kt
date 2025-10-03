package content.region.misthalin.draynor.quest.swept

import content.data.GameAttributes
import content.region.misthalin.draynor.quest.swept.plugin.SweptUtils
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Items

/**
 * Represents the Basement (Betty & Hetty) area for Swept Away quest.
 */
class Basement : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(bettyBasement, hettyBasement)

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.RANDOM_EVENTS)

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity !is Player) return

        val p = entity.asPlayer()
        if (inBorders(p, bettyBasement)) {
            SweptUtils.spawnBettyBasementNPCs()
        }

        registerLogoutListener(p, GameAttributes.QUEST_SWEPT_AWAY) { player ->
            SweptUtils.removeBettyNPCs()
            for (pen in SweptUtils.Pen.values()) {
                if (pen.itemId != -1 && inInventory(player, pen.itemId)) {
                    removeItem(player, pen.itemId)
                }
            }

            return@registerLogoutListener
        }

        setAttribute(p, GameAttributes.QUEST_SWEPT_AWAY_CREATURE_INTER, 0)
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (inBorders(entity, hettyBasement)) {
                if (inInventory(p, Items.NEWT_LABEL_14065)) {
                    removeAll(p, Items.NEWT_LABEL_14065)
                }
                if (inInventory(p, Items.TOAD_LABEL_14066)) {
                    removeAll(p, Items.TOAD_LABEL_14066)
                }
                if (inInventory(p, Items.NEWTS_AND_TOADS_LABEL_14067)) {
                    removeAll(p, Items.NEWTS_AND_TOADS_LABEL_14067)
                }

                sendMessage(p, "As you leave Hetty's basement, your labels mysteriously disappear.")
            }

            if (inBorders(p, bettyBasement)) {
                if (inInventory(p, Items.MAGIC_SLATE_14069)) {
                    removeItem(p, Items.MAGIC_SLATE_14069)
                }

                SweptUtils.removeBettyNPCs()
                clearLogoutListener(p, GameAttributes.QUEST_SWEPT_AWAY)
                removeAttribute(p, GameAttributes.QUEST_SWEPT_AWAY_BETTY_WAND)
                setVarbit(p, SweptUtils.VARBIT_LOTTIE_CHEST, 0)
                sendMessage(p, "As you leave Betty's basement, your magic slate mysteriously disappears.")
            }
        }
    }

    companion object {
        val bettyBasement = ZoneBorders(3244, 4500, 3217, 4525)
        val hettyBasement = ZoneBorders(3158, 4510, 3176, 4526)
    }
}
