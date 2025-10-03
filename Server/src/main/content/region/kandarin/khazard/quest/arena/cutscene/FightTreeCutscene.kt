package content.region.kandarin.khazard.quest.arena.cutscene

import content.region.kandarin.khazard.quest.arena.npc.BouncerNPC
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import shared.consts.NPCs

class FightTreeCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10289)
        addNPC(BOUNCER, 47, 26, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 43, 19)
                timedUpdate(1)
            }

            1 -> {
                moveCamera(43, 27, 300, 100)
                moveCamera(42, 27, 300, 100)
                rotateCamera(46, 27, 300, 100)
                timedUpdate(2)
            }

            2 -> {
                teleport(getNPC(BOUNCER)!!, 47, 26)
                DoorActionHandler.handleAutowalkDoor(getNPC(BOUNCER)!!, getObject(46, 26)!!)
                timedUpdate(2)
            }

            3 -> {
                move(getNPC(BOUNCER)!!, 45, 26)
                moveCamera(37, 27, 300, 5)
                rotateCamera(44, 27, 300, 5)
                timedUpdate(2)
            }

            4 -> {
                move(getNPC(BOUNCER)!!, 42, 26)
                timedUpdate(3)
            }

            5 -> {
                end {
                    BouncerNPC.spawnBouncer(player)
                }
            }
        }
    }

    companion object {
        private const val BOUNCER = NPCs.BOUNCER_269
    }
}
