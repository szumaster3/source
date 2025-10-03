package content.global.skill.slayer

import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.plugin.Initializable
import shared.consts.Scenery

@Initializable
class SlayerTowerPlugin : InteractionListener {

    companion object {
        private val LOCATIONS = arrayOf(Location(3430, 3534, 0), Location(3426, 3534, 0))
        private val sceneryIDs = intArrayOf(Scenery.DOOR_4490, Scenery.DOOR_4487, Scenery.DOOR_4492)
        private const val OPEN_ID = Scenery.STATUE_5117
        private const val CLOSED_ID = Scenery.STATUE_5116
    }

    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "open", "close") { player, node ->
            when (node.id) {
                Scenery.DOOR_4490, Scenery.DOOR_4487 ->
                    handleAutowalkDoor(player, node.asScenery()).also {
                        switchStatue()
                    }
            }
            return@on true
        }
    }

    private fun switchStatue() {
        for (l in LOCATIONS) {
            val `object` = getObject(l)
            if (`object` != null) {
                val id = if (`object`.id == OPEN_ID) CLOSED_ID else OPEN_ID
                SceneryBuilder.replace(`object`, `object`.transform(id))
            }
        }
    }
}
