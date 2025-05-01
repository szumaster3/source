package content.global.ame.grave

import core.api.MapArea
import core.api.anyInInventory
import core.api.removeAll
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.tools.colorize

class Graveyard : MapArea {

    override fun toString(): String {
        return "graveyard"
    }
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(1921, 4993, 1934, 5006))
    }

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if (entity !is Player) return
        val player = entity.asPlayer()
        GravediggerListener.init(player)
        player.debug(colorize("[%DYGRAVEDIGGER</col>]: Event launched. Check attribute sets using command %R::checkgraves</col>."))
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        super.areaLeave(entity, logout)
        if (entity is Player) {
            val player = entity.asPlayer()
            if (anyInInventory(player, *GravediggerListener.COFFIN)) {
                for (itemId in GravediggerListener.COFFIN) {
                    removeAll(player, itemId)
                }
            }
        }
    }
}