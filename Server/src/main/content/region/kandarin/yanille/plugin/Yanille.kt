package content.region.kandarin.yanille.plugin

import core.api.MapArea
import core.api.getAttribute
import core.api.setVarbit
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import shared.consts.Vars

class Yanille : MapArea {

    override fun areaEnter(entity: Entity) {
        if (entity is Player && getAttribute(entity, "zfe:sithik-transformation", false)) {
            setVarbit(entity, Vars.VARBIT_QUEST_SITHIK_OGRE_TRANSFORMATION_495, 1, true)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2598, 3103, 2590, 3108, 1, true))
}
