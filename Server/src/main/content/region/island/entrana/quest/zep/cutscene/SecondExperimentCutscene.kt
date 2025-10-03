package content.region.island.entrana.quest.zep.cutscene

import core.api.location
import core.api.setQuestStage
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import shared.consts.NPCs
import shared.consts.Quests

class SecondExperimentCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(2808, 3355, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(11060)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                fadeFromBlack()
                teleport(player, 56, 27)
                player.faceLocation(location(184, 26, 0))
                addNPC(AUGUSTE, 57, 27, Direction.SOUTH_WEST)
                timedUpdate(5)
            }

            2 -> {
                playerDialogueUpdate(FaceAnim.FRIENDLY, "Well, that went down like a lead balloon.")
            }

            3 -> {
                end()
                setQuestStage(player, Quests.ENLIGHTENED_JOURNEY, 5)
            }
        }
    }

    companion object {
        private const val AUGUSTE = NPCs.AUGUSTE_5049
    }
}
