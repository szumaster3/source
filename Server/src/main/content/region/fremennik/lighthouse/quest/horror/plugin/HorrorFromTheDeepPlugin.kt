package content.region.fremennik.lighthouse.quest.horror.plugin

import content.data.GameAttributes
import content.region.fremennik.lighthouse.quest.horror.dialogue.JossikLighthouseDialogue
import content.region.fremennik.lighthouse.quest.horror.dialogue.StrangeWallDialogue
import core.api.*
import core.api.openDoor
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class HorrorFromTheDeepPlugin : InteractionListener {
    private val brokenBridge = intArrayOf(Scenery.BROKEN_BRIDGE_4615, Scenery.BROKEN_BRIDGE_4616)
    private val strangeWalls = intArrayOf(Scenery.STRANGE_WALL_4544, Scenery.STRANGE_WALL_4543)
    private val strangeDoors = intArrayOf(Scenery.STRANGE_WALL_4545, Scenery.STRANGE_WALL_4546)
    private val requiredItems = intArrayOf(Items.BRONZE_ARROW_882, Items.BRONZE_SWORD_1277, Items.AIR_RUNE_556, Items.FIRE_RUNE_554, Items.EARTH_RUNE_557, Items.WATER_RUNE_555)

    override fun defineListeners() {
        /*
         * Handles lighthouse bookcase.
         */

        on(Scenery.BOOKCASE_4617, IntType.SCENERY, "search") { player, _ ->
            openDialogue(player, BookcaseDialogue())
            return@on true
        }

        /*
         * Handles interaction talk with Jossik.
         */

        on(NPCs.JOSSIK_1335, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, JossikLighthouseDialogue())
            return@on true
        }

        /*
         * Handles ladders down to dagannoth lair.
         */

        on(Scenery.IRON_LADDER_4383, IntType.SCENERY, "climb") { player, _ ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val teleportLocation =
                when {
                    isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> Location(2519, 9994, 1)
                    questStage >= 40 -> Location(2519, 4618, 1)
                    else -> null
                }

            if (teleportLocation != null) {
                animate(player, Animations.MULTI_BEND_OVER_827)
                queueScript(player, 1, QueueStrength.SOFT) {
                    teleport(player, teleportLocation)
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendPlayerDialogue(player, "I have no reason to go down there.", FaceAnim.HALF_THINKING)
            }
            return@on true
        }

        /*
         * Handles the interaction with lighthouse door.
         * The player, after crossing the doors, teleports to the second lighthouse.
         */

        on(Scenery.DOORWAY_4577, IntType.SCENERY, "walk-through") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)

            when {
                isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP) -> {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                questStage >= 20 -> {
                    val insideLighthouse = inBorders(player, 2508, 3634, 2510, 3635)
                    val teleportLocation = if (insideLighthouse) location(2445, 4596, 0) else location(2509, 3635, 0)

                    submitIndividualPulse(
                        player,
                        object : Pulse(2) {
                            override fun pulse(): Boolean {
                                if (insideLighthouse) {
                                    sendMessage(player, "You unlock the Lighthouse front door.")
                                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 30)
                                }
                                runTask(player, 3) { teleport(player, teleportLocation) }
                                return true
                            }
                        },
                    )
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                questStage < 20 -> {
                    sendNPCDialogue(
                        player,
                        NPCs.LARRISSA_1336,
                        "Please adventurer... We are both curious as to what has happened in that lighthouse, but you need to fix the bridge for me!",
                    )
                }

                else -> {
                    sendDialogue(player, "You can't see any way to open the door.")
                }
            }
            return@on true
        }

        /*
         * Handles the light mechanism fix.
         */

        onUseWith(IntType.SCENERY, Items.SWAMP_TAR_1939, Scenery.LIGHTING_MECHANISM_4588) { player, used, with ->
            val swampTarVarbit = Vars.VARBIT_HORROR_FROM_THE_DEEP_SWAMP_TAR_LIGHTING_MECHANISM_46
            if (getVarbit(player, swampTarVarbit) == 1) {
                sendMessage(player, "The torch is already flammable.")
            } else if (removeItem(player, used.id)) {
                sendMessage(player, "You use the swamp tar to make the torch flammable again.")
                setVarbit(player, swampTarVarbit, 1, true)
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.LIGHTING_MECHANISM_4588) { player, used, with ->
            val swampTarVarbit = Vars.VARBIT_HORROR_FROM_THE_DEEP_SWAMP_TAR_LIGHTING_MECHANISM_46
            val litVarbit = Vars.VARBIT_HORROR_FROM_THE_DEEP_LIT_LIGHTING_MECHANISM_48

            if (getVarbit(player, swampTarVarbit) == 0) return@onUseWith false
            if (getVarbit(player, litVarbit) == 1) {
                sendMessage(player, "You've already lit the torch.")
                return@onUseWith false
            }
            sendMessage(player, "You light the torch with your tinderbox.")
            setVarbit(player, litVarbit, 1, true)
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.MOLTEN_GLASS_1775, Scenery.LIGHTING_MECHANISM_4588) { player, used, with ->
            val repairedVarbit = Vars.VARBIT_HORROR_FROM_THE_DEEP_LIGHTING_MECHANISM_LENS_REPAIRED_47
            if (getVarbit(player, repairedVarbit) == 1) {
                sendMessage(player, "You have already repaired the lighthouse torch.")
                return@onUseWith false
            }
            if (removeItem(player, used.id)) {
                replaceScenery(with.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 80)
                setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 40)
                setVarbit(player, repairedVarbit, 1, true)
                sendMessage(player, "You use the molten glass to repair the lens.")
                sendMessage(player, "You have managed to repair the lighthouse torch!")
            }
            return@onUseWith true
        }

        /*
         * Handles study the strange wall.
         */

        on(strangeWalls, IntType.SCENERY, "study") { player, _ ->
            when (player.location.y) {
                4626, 10002 -> {
                    setAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER, true)
                    openInterface(player, Components.HORROR_METALDOOR_142)
                }
                else -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles using the required items on the strange wall.
         */

        onUseWith(IntType.SCENERY, requiredItems, *strangeWalls) { player, used, _ ->
            openDialogue(player, StrangeWallDialogue(items = used.id))
            return@onUseWith true
        }

        /*
         * Handles opening the strange door.
         */

        on(strangeDoors, IntType.SCENERY, "open") { player, node ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            if (questStage >= 50) {
                openDoor(player, node.asScenery())
                playAudio(player, Sounds.STRANGEDOOR_OPEN_1626)
                playAudio(player, Sounds.STRANGEDOOR_CLOSE_1625, 2)
                if (!isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, 55)
                    removeAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER)
                }
                return@on true
            }
            when (player.location.y) {
                4626, 10002 -> sendMessage(player, "You cannot see any way to move this part of the wall....")
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles fix the bridge (both sides).
         */

        onUseWith(IntType.SCENERY, Items.PLANK_960, *brokenBridge) { player, _, bridge ->
            val questStage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val bridgeUnlocked = getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0)

            val isFirstHalf = bridge.id == Scenery.BROKEN_BRIDGE_4615
            val requiredStage = if (isFirstHalf) 5..10 else 10..20
            val newUnlockValue = if (isFirstHalf) 1 else 2
            val newQuestStage = if (isFirstHalf) 10 else 20
            val message =
                if (isFirstHalf) "You create half a makeshift walkway out of the plank." else "You have now made a makeshift walkway over the bridge."

            when {
                questStage !in requiredStage -> {
                    sendDialogue(player, "That won't help fix the bridge.")
                    return@onUseWith false
                }

                isFirstHalf && bridgeUnlocked == 1 -> {
                    sendDialogue(player, "You have already fixed this half of the bridge.")
                    return@onUseWith false
                }

                !isFirstHalf && bridgeUnlocked < 1 -> {
                    sendMessage(player, "I might be able to make it to the other side.")
                    return@onUseWith false
                }

                !inInventory(player, Items.HAMMER_2347, 1) -> {
                    sendDialogue(player, "You need a hammer to force the nails in with.")
                    return@onUseWith false
                }

                amountInInventory(player, Items.STEEL_NAILS_1539) < 30 -> {
                    sendDialogue(player, "You need 30 steel nails to attach the plank with.")
                    return@onUseWith false
                }
            }

            if (removeItem(player, Item(Items.STEEL_NAILS_1539, 30)) && removeItem(player, Item(Items.PLANK_960, 1))) {
                lock(player, 1)
                animate(player, Animations.SMITH_HAMMER_898)
                queueScript(player, 3, QueueStrength.SOFT) {
                    animate(player, Animations.SMITH_HAMMER_898)
                    setAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, newUnlockValue)
                    setQuestStage(player, Quests.HORROR_FROM_THE_DEEP, newQuestStage)
                    sendDialogue(player, message)
                    sendMessage(player, message)
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles crossing the bridge from east.
         * val agilityLevel = getStatLevel(player, Skills.AGILITY)
         * val failChance = 1.0 - (agilityLevel + 1) / 100.0
         * val damage = (1..5).random()
         */

        on(Scenery.BROKEN_BRIDGE_4615, IntType.SCENERY, "cross") { player, _ ->
            val bridgeUnlock = getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0)
            val cycle = animationCycles(Animations.AGILITY_START_3276)
            val destination = Location(2598, 3608, 0)
            if (getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0) == 0) {
                sendMessage(player, "I might be able to make it to the other side.")
                return@on true
            }
            when (bridgeUnlock) {
                1 -> {
                    player.animate(Animation(Animations.JUMP_BRIDGE_769), 1)
                    forceWalk(player, destination.transform(1, 0, 0), "")
                    teleport(player, destination, TeleportManager.TeleportType.INSTANT, 2)
                    runTask(player, 3) { forceWalk(player, destination.transform(1, 0, 0), "") }
                }
                2 -> {
                    forceMove(player, player.location, destination, 0, cycle, Direction.EAST, Animations.AGILITY_START_3276)
                    runTask(player, 6) { forceWalk(player, destination.transform(1, 0, 0), "") }
                }
                else -> return@on false
            }

            return@on true
        }

        /*
         * Handles crossing the bridge from west.
         */

        on(Scenery.BROKEN_BRIDGE_4616, IntType.SCENERY, "cross") { player, _ ->
            if (getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_BRIDGE, 0) != 2) {
                sendMessage(player, "I might be able to make it to the other side.")
                return@on true
            }
            forceMove(player, player.location, Location(2596, 3608, 0), 0, 120, Direction.WEST, Animations.AGILITY_START_ALT_3277)
            runTask(player, 6) { forceWalk(player, Location.create(2595, 3608, 0), "") }
            return@on true
        }
    }

    inner class BookcaseDialogue : DialogueFile() {

        init {
            stage = 0
        }

        private val books = arrayOf(
            Item(Items.MANUAL_3847, 1),
            Item(Items.DIARY_3846, 1),
            Item(Items.JOURNAL_3845, 1)
        )

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogue(player!!, "There are three books here that look important... What would you like to do?")
                    stage = 1
                }
                1 -> {
                    options("Take the Lighthouse Manual", "Take the ancient Diary", "Take Jossik's Journal", "Take all three books")
                    stage = 2
                }
                2 -> {
                    val bookIDs = books.toList()
                    val freeSlots = freeSlots(player!!)

                    if (buttonID == 4 && freeSlots < bookIDs.size || buttonID in 1..3 && freeSlots < 1) {
                        sendDialogue(player!!, "You do not have enough room to take ${if (buttonID == 4) "all three" else "that"}.")
                        return
                    }

                    when(buttonID) {
                        1 -> player!!.inventory.add(books[0])
                        2 -> player!!.inventory.add(books[1])
                        3 -> player!!.inventory.add(books[2])
                        4 -> player!!.inventory.add(*books)
                    }
                    end()
                }
            }
        }
    }
}
