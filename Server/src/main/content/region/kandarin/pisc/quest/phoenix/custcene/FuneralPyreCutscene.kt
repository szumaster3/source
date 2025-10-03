package content.region.kandarin.pisc.quest.phoenix.custcene

import content.region.kandarin.pisc.quest.phoenix.InPyreNeed
import core.api.sendGraphics
import core.api.setVarbit
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Vars

class FuneralPyreCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(3533, 5204, 0))
        loadRegion(REGION)
        addNPC(PHOENIX, 14, 19, Direction.NORTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 13, 20, 0)
                timedUpdate(2)
            }

            1 -> {
                rotateCamera(16, 23, 1)
                moveCamera(13, 16, 600, 1)
                setVarbit(player, 5774, 3, true)
                sendGraphics(1978, getNPC(PHOENIX)!!.location)
                timedUpdate(6)
            }

            2 -> {
                moveCamera(12, 18, 600, 1)
                rotateCamera(16, 13, 300, 1)
                timedUpdate(6)
            }

            3 -> {
                rotateCamera(14, 15, 300, 1)
                teleport(getNPC(PHOENIX)!!, 15, 14)
                // Inside
                getNPC(PHOENIX)!!.transform(NPCs.PHOENIX_8548)
                // Outside
                InPyreNeed.WOUNDED_PHOENIX_ID.transform(NPCs.PHOENIX_8548)
                InPyreNeed.WOUNDED_PHOENIX_ID.teleporter.send(
                    Location.create(3535, 5198, 0), TeleportManager.TeleportType.INSTANT
                )
                setVarbit(player, 5774, 4, true)
                timedUpdate(1)
            }

            4 -> {
                endWithoutFade {
                    resetCamera()
                    setVarbit(player, InPyreNeed.PROGRESS, 6, true)
                }
            }
        }
    }

    companion object {
        private const val PHOENIX = NPCs.WOUNDED_PHOENIX_8547
        private const val REGION = 14161
    }
}
