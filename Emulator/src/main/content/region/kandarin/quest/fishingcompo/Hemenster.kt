package content.region.kandarin.quest.fishingcompo

import content.data.GameAttributes
import core.api.MapArea
import core.api.findLocalNPC
import core.api.getAttribute
import core.api.removeAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.NPCs

class Hemenster : MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        ZoneBorders(2625, 3411, 2643, 3448)
    )

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            val p = entity.asPlayer()
            if (getAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)) {
                removeAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST)
            }
        }
    }

    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            val p = entity.asPlayer()

            /*
             * Transform fishing spot.
             */

            if (getAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false) && getAttribute(p, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                val npc = findLocalNPC(p, NPCs.FISHING_SPOT_309)
                npc?.let {
                    it.transform(NPCs.FISHING_SPOT_233)
                }
            }
        }
    }

}