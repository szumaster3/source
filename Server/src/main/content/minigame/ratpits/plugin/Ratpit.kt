package content.minigame.ratpits.plugin

import core.api.MapArea
import core.api.closeOverlay
import core.api.getRegionBorders
import core.api.openOverlay
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import shared.consts.Components

class Ratpit : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> =
        arrayOf(
            getRegionBorders(VARROCK_RAT_PITS_REGION),
            getRegionBorders(PORT_SARIM_RAT_PITS_REGION),
            KELDAGRIM_RAT_PITS_REGION,
            getRegionBorders(ARDOUGNE_RAT_PITS_REGION),
        )

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity is Player) {
            openOverlay(entity.asPlayer(), Components.RATCATCHER_OVERLAY_284)
        }
    }

    override fun areaLeave(
        entity: Entity,
        logout: Boolean,
    ) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            closeOverlay(entity.asPlayer())
        }
    }

    companion object {
        const val VARROCK_RAT_PITS_REGION = 11599
        const val PORT_SARIM_RAT_PITS_REGION = 11926
        val KELDAGRIM_RAT_PITS_REGION = ZoneBorders(1920, 4672,1983, 4735, 0, true)
        const val ARDOUGNE_RAT_PITS_REGION = 10646
    }
}
