package content.region.misthalin.quest.priest.handlers

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.world.map.Location
import org.rs.consts.*

class PriestInPerilListener : InteractionListener {

    companion object {
        const val MONUMENT_INTERFACE = Components.PRIESTPERIL_GRAVEMONUMENT_272
        val mausoleumMonumentIds = intArrayOf(
            Scenery.MONUMENT_3496,
            Scenery.MONUMENT_3498,
            Scenery.MONUMENT_3495,
            Scenery.MONUMENT_3497,
            Scenery.MONUMENT_3494,
            Scenery.MONUMENT_3493,
            Scenery.MONUMENT_3499,
        )
    }

    override fun defineListeners() {

        /*
         * Handles Paterdomus Temple doors interactions.
         */

        listOf(Scenery.LARGE_DOOR_30707, Scenery.LARGE_DOOR_30708).forEach { id ->
            on(id, IntType.SCENERY, "open", "knock-at") { player, node ->
                val option = getUsedOption(player)
                val questStage = getQuestStage(player, Quests.PRIEST_IN_PERIL)
                if (option != "open") {
                    when (questStage) {
                        10, 12, 13 -> openDialogue(player, 54584, node)
                        0 -> {
                            sendMessage(player, "You knock at the door...")
                            sendMessageWithDelay(player, "...but nothing interesting happens.", 1)
                        }
                    }
                }
                if (questStage < 12) {
                    sendMessage(player, "This door is securely locked from inside.")
                } else {
                    DoorActionHandler.handleDoor(player, node.asScenery())
                }
                return@on true
            }
        }

        val monumentData = mapOf(
            Scenery.MONUMENT_3496 to Triple("hammer", Items.GOLDEN_HAMMER_2949 to Items.HAMMER_2347, "Saradomin is the hammer that crushes evil everywhere."),
            Scenery.MONUMENT_3498 to Triple("needle", Items.GOLDEN_NEEDLE_2951 to Items.NEEDLE_1733, "Saradomin is the needle that binds our lives together."),
            Scenery.MONUMENT_3495 to Triple("pot", Items.GOLDEN_POT_2948 to Items.EMPTY_POT_1931, "Saradomin is the vessel that keeps our lives from harm."),
            Scenery.MONUMENT_3497 to Triple("feather", Items.GOLDEN_FEATHER_2950 to Items.FEATHER_314, "Saradomin is the delicate touch that brushes us with love."),
            Scenery.MONUMENT_3494 to Triple("candle", Items.GOLDEN_CANDLE_2947 to Items.CANDLE_36, "Saradomin is the light that shines throughout our lives."),
            Scenery.MONUMENT_3499 to Triple("key", Items.IRON_KEY_2945 to Items.GOLDEN_KEY_2944, "Saradomin is the key that unlocks the mysteries of life."),
            Scenery.MONUMENT_3493 to Triple("tinderbox", Items.GOLDEN_TINDERBOX_2946 to Items.TINDERBOX_590, "Saradomin is the spark that lights the fire in our hearts.")
        )

        /*
         * Handle monument interaction.
         */

        on(mausoleumMonumentIds, IntType.SCENERY, "study", "take-from") { player, node ->
            val option = getUsedOption(player)
            val data = monumentData[node.id] ?: return@on false
            val (attribute, items, message) = data

            if (option == "take-from") {
                impact(player, 2, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "A holy power prevents you stealing from the monument!")
                return@on true
            }
            openInterface(player, MONUMENT_INTERFACE)
            val item = if (!getAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:$attribute", false)) items.first else items.second
            sendItemZoomOnInterface(player, MONUMENT_INTERFACE, 4, item)
            sendAngleOnInterface(player, MONUMENT_INTERFACE, 4, 300, 200, 100)
            sendString(player, message, MONUMENT_INTERFACE, 17)
            return@on true
        }

        /*
         * Handle item swapping.
         */

        monumentData.forEach { (sceneryId, data) ->
            val (item, items, _) = data
            onUseWith(IntType.SCENERY, items.first, sceneryId) { player, used, _ ->
                if (!getAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:$item", false) && removeItem(player, used)) {
                    addItem(player, items.second)
                    val name = item.replaceFirstChar { it.uppercaseChar() }
                    sendMessage(player, "You swap the $item for the ${if (name == "Key" && used.id == Items.GOLDEN_KEY_2944) "iron" else "golden"} $item.")
                    setAttribute(player, "${GameAttributes.QUEST_PRIEST_IN_PERIL}:$item", true)
                }
                return@onUseWith true
            }
        }

        /*
         * Handles entrance to dungeon.
         */

        on(intArrayOf(Scenery.TRAPDOOR_30571, Scenery.TRAPDOOR_30573), IntType.SCENERY, "open") { player, node ->
            replaceScenery(node.asScenery(), Scenery.LADDER_30572, 80)
            sendMessage(player, "The trapdoor opens...")
            return@on true
        }

        /*
         * Handles entrance to dungeon.
         */

        on(Scenery.LADDER_30572, IntType.SCENERY, "climb-down", "close") { player, _ ->
            if (getUsedOption(player) == "climb-down") {
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                sendMessage(player, "You climb down through the trapdoor...")
                if (player.location.y < 3506) {
                    queueScript(player, 2, QueueStrength.NORMAL) {
                        teleport(player, Location.create(3440, 9887, 0))
                    }
                } else {
                    queueScript(player, 2, QueueStrength.NORMAL) {
                        teleport(player, Location.create(3405, 9906, 0))
                    }
                }
            }
            if (getUsedOption(player) == "close") {
                if (player.location.y < 3506) {
                    removeScenery(core.game.node.scenery.Scenery(Scenery.LADDER_30572, Location(3422, 3484, 0)))
                    addScenery(Scenery.TRAPDOOR_30573, Location.create(3422, 3484, 0), rotation = 3)
                } else {
                    removeScenery(core.game.node.scenery.Scenery(Scenery.LADDER_30572, Location(3405, 3507, 0)))
                    addScenery(Scenery.TRAPDOOR_30573, Location(3405, 3507, 0))
                }
            }
            return@on true
        }

        /*
         * Handles exit from the dungeon.
         */

        on(Scenery.LADDER_30575, IntType.SCENERY, "climb-up") { player, _ ->
            animate(player, Animations.HUMAN_CLIMB_STAIRS_828)
            queueScript(player, 2, QueueStrength.NORMAL) {
                teleport(player, Location(3405, 3506, 0))
            }
            return@on true
        }

        /*
         * Handles searching the well.
         */

        on(Scenery.WELL_3485, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(player, "You look down the well and see the filthy polluted water of the river", "Salve moving slowly along.")
            return@on true
        }

        /*
         * Handles using bucket on the well.
         */

        onUseWith(IntType.SCENERY, Items.BUCKET_1925, Scenery.WELL_3485) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            addItem(player, Items.BUCKET_OF_WATER_2953)
            sendMessage(player, "You fill the bucket from the well.")
            return@onUseWith true
        }

        /*
         * Handles using the quest key to pass through mausoleum cell doors.
         */

        onUseWith(IntType.SCENERY, Items.IRON_KEY_2945, Scenery.CELL_DOOR_3463) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            setQuestStage(player, Quests.PRIEST_IN_PERIL, 15)
            sendNPCDialogue(player, NPCs.DREZEL_7690, "Oh! Thank you! You have found the key!", FaceAnim.HALF_GUILTY)
            sendMessage(player, "You have unlocked the cell door.")
            return@onUseWith true
        }

        /*
         * Handles using the saradomin water on coffin to save Drezel from vampire.
         */

        onUseWith(IntType.SCENERY, Items.BUCKET_OF_WATER_2954, Scenery.MORYTANIA_COFFIN_30728) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            addItem(player, Items.BUCKET_1925)
            animate(player, Animations.PRIEST_BUCKET_1077)
            playAudio(player, Sounds.HOLY_WATER_POUR_1733)
            setQuestStage(player, Quests.PRIEST_IN_PERIL, 16)
            sendMessage(player, "You pour the blessed water over the coffin...")
            return@onUseWith true
        }

        /*
         * Handles some doors.
         */

        on(Scenery.GATE_3445, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PRIEST_IN_PERIL) < 17) {
                sendMessage(player, "The door is locked shut.")
            } else {
                DoorActionHandler.handleDoor(player, node.asScenery())
            }
            return@on true
        }

        /*
         * Handles entrance to Canifis.
         */

        on(Scenery.HOLY_BARRIER_3443, IntType.SCENERY, "pass-through") { player, _ ->
            if (!isQuestComplete(player, Quests.PRIEST_IN_PERIL)) {
                sendMessage(player, "A magic force prevents you from passing through.")
            } else {
                teleport(player, Location.create(3423, 3484, 0))
                sendMessage(player, "You pass through the holy barrier.")
            }
            return@on true
        }

        /*
         * Handles opening the cell doors.
         */

        on(Scenery.CELL_DOOR_3463, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PRIEST_IN_PERIL) < 15) {
                sendMessage(player, "The door is securely locked shut.")
            } else {
                DoorActionHandler.handleDoor(player, node.asScenery())
            }
            return@on true
        }

        /*
         * Handles talk with Drezel before escape the prison.
         */

        on(Scenery.CELL_DOOR_3463, IntType.SCENERY, "talk-through") { player, _ ->
            openDialogue(player, NPCs.DREZEL_7690)
            return@on true
        }

        /*
         * Handles opening the coffin.
         */

        on(Scenery.MORYTANIA_COFFIN_30728, IntType.SCENERY, "open") { player, _ ->
            player.dialogueInterpreter.sendDialogues(
                player,
                null,
                "It sounds like there's something alive inside it. I don't",
                "think it would be a very good idea to open it..."
            )
            return@on true
        }

        /*
         * handles Temple guardian NPC.
         */

        on(NPCs.TEMPLE_GUARDIAN_7711, IntType.NPC, "attack") { player, node ->
            if (getQuestStage(player, Quests.PRIEST_IN_PERIL) > 10) {
                node.asNpc().attack(player)
                return@on true
            }
            when (getQuestStage(player, Quests.PRIEST_IN_PERIL)) {
                10 -> sendMessage(player, "You have no reason to attack a helpless dog!")
                12 -> sendMessage(player, "You've already killed that dog!")
                13 -> {
                    player.properties.combatPulse.stop()
                    sendMessage(player, "I'd better not make the King mad at me again!")
                }
            }
            return@on true
        }
    }
}
