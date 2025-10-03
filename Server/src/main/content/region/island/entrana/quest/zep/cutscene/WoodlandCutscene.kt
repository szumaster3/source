package content.region.island.entrana.quest.zep.cutscene

import core.api.location
import core.game.activity.Cutscene
import core.game.node.entity.player.Player

class WoodlandCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(location(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(7244)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                end()
                timedUpdate(1)
            }
        }
    }
}
