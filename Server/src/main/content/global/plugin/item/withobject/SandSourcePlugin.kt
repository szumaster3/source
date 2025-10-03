package content.global.plugin.item.withobject

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Scenery

/**
 * Handles the logic for filling containers (buckets and sandbags) with sand from sandpits or sand piles.
 */
class SandSourcePlugin : InteractionListener {

    companion object {
        private val SANDPITS = intArrayOf(Scenery.SAND_PIT_2645, Scenery.SAND_PIT_4373, Scenery.SANDPIT_10814)
        private val SAND_PILES = intArrayOf(Scenery.SAND_2977, Scenery.SAND_2978, Scenery.SAND_2979)
        private val CONTAINER_IDS = intArrayOf(Items.BUCKET_1925, Items.EMPTY_SACK_5418)
    }

    override fun defineListeners() {

        /*
         * Handles using containers (buckets, sacks) on sandpits.
         */

        onUseWith(IntType.SCENERY, CONTAINER_IDS, *SANDPITS, *SAND_PILES) { player, used, with ->
            val isSandPile = with.id in SAND_PILES
            fillSand(player, used.id, with, isSandPile)
            return@onUseWith true
        }

        /*
         * Handles interaction with sand piles.
         */

        on(SAND_PILES, IntType.SCENERY, "look") { player, node ->
            val examine = sceneryDefinition(node.id).examine.toString()
            sendDialogue(player, examine)
            return@on true
        }
    }

    /**
     * Fills the given container with sand.
     */
    private fun fillSand(player: Player, containerId: Int, with: Node, isSandPile: Boolean) {
        val emptyContainersAmount = amountInInventory(player, containerId)
        var animationTrigger = 0

        val (animation, filledItem) = when (containerId) {
            Items.BUCKET_1925 -> Animations.FILL_BUCKET_SAND_895 to Items.BUCKET_OF_SAND_1783
            Items.EMPTY_SACK_5418 -> Animations.ENLIGHTENED_SANDBAG_5155 to Items.SANDBAG_9943
            else -> return
        }

        runTask(player, 2, emptyContainersAmount) {
            if (removeItem(player, Item(containerId, 1), Container.INVENTORY)) {
                if (animationTrigger % 2 == 0) {
                    animate(player, animation)
                }
                sendMessage(
                    player, "You fill the ${if (containerId == Items.BUCKET_1925) "bucket" else "sandbag"} with sand."
                )
                addItem(player, filledItem)

                if (isSandPile) {
                    handleSandPile(player, with)
                }
                animationTrigger++
            }
        }
    }

    /**
     * Handles the state change of a sand pile after collecting sand.
     */
    private fun handleSandPile(player: Player, with: Node) {
        if (with.id == Scenery.SAND_2979) {
            removeScenery(with.asScenery())
            submitWorldPulse(object : Pulse(75) {
                override fun pulse(): Boolean {
                    addScenery(
                        if (inBorders(player, getRegionBorders(11310))) with.id - 2 else with.id - 1,
                        with.location,
                        with.direction.ordinal,
                    )
                    return true
                }
            })
        } else {
            replaceScenery(with.asScenery(), with.id + 1, 75)
        }
    }
}
