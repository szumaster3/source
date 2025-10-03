package content.region.misthalin.edgeville.plugin

import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Scenery

class EdgevillePlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles enter to safety dungeon.
         */

        on(Scenery.POSTER_29586, IntType.SCENERY, "pull-back") { player, _ ->
            sendDialogue(player, "There appears to be a tunnel behind this poster.")
            addDialogueAction(player) { _, _ ->
                teleport(player, Location(3140, 4230, 2))
            }
            return@on true
        }

        on(Scenery.TRAPDOOR_12267, IntType.SCENERY, "open") { player, _ ->
            animate(player, Animations.OPEN_CHEST_536)
            setVarbit(player, 1888, 1)
            return@on true
        }

        on(Scenery.OPEN_TRAPDOOR_12268, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, 1888, 0)
            return@on true
        }

        on(Scenery.OPEN_TRAPDOOR_12268, IntType.SCENERY, "go-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(3077, 9893, 0))
            return@on true
        }

        on(Scenery.CELLAR_STAIRS_12265, IntType.SCENERY, "climb") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(3078, 3493, 0))
            return@on true
        }

        on(12266, IntType.SCENERY, "open", "close") { player, _ ->
            if (getUsedOption(player) == "open") {
                setVarp(player, 680, 1 shl 22, false)
            } else {
                setVarp(player, 680, 0)
            }
            return@on true
        }

        on(
            intArrayOf(Scenery.ROSES_9261, Scenery.ROSES_9262, Scenery.ROSES_30806),
            IntType.SCENERY,
            "take-seed",
        ) { player, _ ->
            sendMessage(player, "There doesn't seem to be any seeds on this rosebush.")
            return@on true
        }

        on(Scenery.TRAPDOOR_26933, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            sendMessage(player, "The trapdoor opens...")
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        on(Scenery.TRAPDOOR_26934, IntType.SCENERY, "close", "climb-down") { player, node ->
            if (getUsedOption(player) == "close") {
                animate(player, 535)
                sendMessage(player, "You close the trapdoor.")
                replaceScenery(node.asScenery(), node.id - 1, -1)
            } else {
                sendMessage(player, "You climb down through the trapdoor...")
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-down")
            }
            return@on true
        }
    }
}
