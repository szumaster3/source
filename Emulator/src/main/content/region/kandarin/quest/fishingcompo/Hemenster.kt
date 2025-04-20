package content.region.kandarin.quest.fishingcompo

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

class Hemenster : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2625, 3411, 2643, 3448)
    )

    override fun areaEnter(entity: Entity) {
        super.areaEnter(entity)
        if(entity is Player) {
            // No offset
            replaceScenery(Scenery(41, Location.create(2638, 3446, 0), 4, 5), 41, -1, Location.create(2638, 3446, 0))
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (getAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)) {
                removeAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST)
            }
        }
    }
}