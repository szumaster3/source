package content.region.morytania.phas.quest.hauntedmine.npc

import core.api.getRegionBorders
import core.api.inBorders
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import shared.consts.NPCs

class MineCartNPC : NPCBehavior(NPCs.MINE_CART_4980) {
    override fun onCreation(self: NPC) {
        if (inBorders(self, getRegionBorders(10822))) {
            val cartRoute =
                arrayOf(
                    Location.create(2727, 4489, 0),
                    Location.create(2727, 4509, 0),
                )
            self.configureMovementPath(*cartRoute)
            self.isWalks = true
            self.shouldPreventStacking(self)
        }
    }
}
