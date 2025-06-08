package content.global.skill.agility.shortcuts

import content.global.skill.agility.AgilityShortcut
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Sounds

/**
 * Represents the various Pipe shortcuts.
 */
@Initializable
class PipeShortcut : AgilityShortcut {

    constructor() : super(intArrayOf(), 0, 0.0, "")

    constructor(id: Int, level: Int, experience: Double) : super(intArrayOf(id), level, experience, "squeeze-through")

    override fun newInstance(arg: Any?): Plugin<Any> {
        configure(PipeShortcut(YANILLE_DUNGEON, 49, 0.0))
        configure(PipeShortcut(BRIMHAVEN_DRAGONS, 22, 8.5))
        configure(PipeShortcut(BRIMHAVEN_GIANTS, 34, 10.0))
        configure(PipeShortcut(TAVERLEY_DUNGEON, 70, 10.0))
        configure(PipeShortcut(BARBARIAN_OUTPOST, 35, 10.0))
        configure(PipeShortcut(EDGEVILLE_DUNGEON, 51, 10.0))
        return this
    }

    override fun run(player: Player, scenery: Scenery, option: String, failed: Boolean) {
        val steps = when (scenery.id) {
            YANILLE_DUNGEON -> 8
            TAVERLEY_DUNGEON -> 7
            BRIMHAVEN_DRAGONS, BRIMHAVEN_GIANTS -> 7
            BARBARIAN_OUTPOST -> {
                if (player.location.x != 2552) {
                    sendMessage(player, "I can't get into this pipe at that angle.")
                    return
                }
                5
            }
            EDGEVILLE_DUNGEON -> {
                if (player.location.y != 9906) {
                    sendMessage(player, "I can't get into this pipe at that angle.")
                    return
                }
                5
            }
            else -> return
        }

        val duration = 5

        val startAnim = if (scenery.id == BARBARIAN_OUTPOST)
            Animations.CLIMB_THROUGH_OBSTACLE_10580
        else
            Animations.CLIMB_INTO_OBSTACLE_10578

        lock(player, duration)

        forceMove(
            player,
            player.location,
            pipeDestination(player, scenery, steps),
            30,
            30 * duration,
            null,
            startAnim
        )

        val endAnim = animationDuration(Animation(startAnim))
        player.animate(Animation(Animations.CRAWL_844), endAnim)

        queueScript(player, duration, QueueStrength.SOFT) {
            player.animate(Animation(Animations.CLIMB_OUT_OF_OBSTACLE_10579))
            playAudio(player, Sounds.SQUEEZE_OUT_2490)
            stopExecuting(player)
        }
    }

    companion object {
        private const val YANILLE_DUNGEON = 2290
        private const val BRIMHAVEN_DRAGONS = 5099
        private const val BRIMHAVEN_GIANTS = 5100
        private const val TAVERLEY_DUNGEON = 9293
        private const val BARBARIAN_OUTPOST = 20210
        private const val EDGEVILLE_DUNGEON = 29370
    }
}