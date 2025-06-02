package content.global.skill.agility.shortcuts

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import org.rs.consts.*

class RopeSwingShortcut : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles use rope on branch.
         */

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.BRANCH_2326) { player, used, with ->
            if (!inInventory(player, used.id)) {
                sendMessage(player, "You need a rope to do that.")
                return@onUseWith true
            }

            val branch = with.asScenery()
            if (branch.isActive) {
                removeItem(player, used.asItem())
                faceLocation(player, branch.location)
                sendMessage(player, "You tie the rope to the tree...")
                playAudio(player, Sounds.TIGHTROPE_2495)
                animate(player, Animations.SUMMON_ROPE_SWING_775)
                SceneryBuilder.replace(branch, branch.transform(Scenery.ROPESWING_2325))
            }

            ropeSwing(player, branch, Location(2505, 3087, 0))
            return@onUseWith true
        }

        /*
         * Handles rope swing.
         */

        on(ROPE_SCENERY, IntType.SCENERY, "swing-on") { player, node ->
            if (!player.location.withinDistance(node.location, 2)) {
                sendMessage(player, "You cannot do that from here.")
                return@on true
            }

            val end = when (node.id) {
                2325 -> Location(2505, 3087, 0)
                2324 -> Location(2511, 3096, 0)
                else -> return@on true
            }

            ropeSwing(player, node, end)
            return@on true
        }
    }

    /**
     * Rope swing interaction.
     */
    private fun ropeSwing(player: Player, node: Node, destination: Location) {
        playAudio(player, Sounds.SWING_ACROSS_2494)
        animateScenery(node.asScenery(), ROPE_ANIMATION)

        val start = when (node.id) {
            2324 -> Location(2511, 3092, 0)
            else -> Location(2501, 3087, 0)
        }

        forceMove(player, start, destination, 0, 60, null, Animations.ROPE_SWING_751) {
            sendMessage(player, "You skillfully swing across.")
        }
    }

    companion object {
        private val ROPE_ANIMATION = 497
        private val ROPE_SCENERY = intArrayOf(Scenery.ROPESWING_2324, Scenery.ROPESWING_2325)
    }
}