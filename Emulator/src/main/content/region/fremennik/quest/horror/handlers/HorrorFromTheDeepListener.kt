package content.region.fremennik.quest.horror.handlers

import content.data.GameAttributes
import content.data.QuestItem
import content.region.fremennik.quest.horror.HorrorFromTheDeep
import content.region.fremennik.quest.horror.dialogue.JossikLighthouseDialogue
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class HorrorFromTheDeepListener : InteractionListener {

    companion object {
        private val STRANGE_WALL = intArrayOf(Scenery.STRANGE_WALL_4544, Scenery.STRANGE_WALL_4543)
        private val STRANGE_WALL_DOOR = intArrayOf(Scenery.STRANGE_WALL_4545, Scenery.STRANGE_WALL_4546)
        private val STRANGE_WALL_ITEMS = intArrayOf(Items.BRONZE_ARROW_882, Items.BRONZE_SWORD_1277, Items.AIR_RUNE_556, Items.FIRE_RUNE_554, Items.EARTH_RUNE_557, Items.WATER_RUNE_555)
        private val TOOLS = intArrayOf(Items.SWAMP_TAR_1939, Items.TINDERBOX_590, Items.MOLTEN_GLASS_1775)
    }

    override fun defineListeners() {
        /*
         * Handles lighthouse bookcase.
         */

        on(Scenery.BOOKCASE_4617, IntType.SCENERY, "search") { player, _ ->
            if (isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)) {
                openDialogue(player, QuestItem(Items.MANUAL_3847))
                return@on true
            } else {
                sendDialogue(player, "There are three books here that look important... What would you like to do?")
                addDialogueAction(player) { _, button ->
                    if (button > 0) sendDialogueOptions(player, "Select an option", "Take the Lighthouse Manual", "Take the ancient Diary", "Take Jossik's Journal", "Take all three books")
                    addDialogueAction(player) { _, button ->
                        val book = arrayOf(Item(Items.MANUAL_3847), Item(Items.DIARY_3846), Item(Items.JOURNAL_3845))
                        val bookIDs = book.toList()
                        if (freeSlots(player) < (if (button == 5) bookIDs.size else 1)) {
                            sendDialogue(player, "You do not have enough room to take ${if (bookIDs.size > 1) "all three" else "that"}.")
                            return@addDialogueAction
                        }
                        when (button) {
                            2 -> player.inventory.add(book[0])
                            3 -> player.inventory.add(book[1])
                            4 -> player.inventory.add(book[2])
                            5 -> player.inventory.add(*book)
                        }
                    }
                }
            }
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
            val fixLighthouse = getVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_THIRD_FIX)
            val completeQuest = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)

            if(!completeQuest && fixLighthouse == 0) {
                sendPlayerDialogue(player, "I have no reason to go down there.", FaceAnim.HALF_THINKING)
                return@on true
            }

            lock(player, 1)
            animate(player, Animations.MULTI_BEND_OVER_827)
            teleport(player, if(completeQuest) Location(2519, 9994, 1) else Location(2519, 4618, 1))
            return@on true
        }

        /*
         * Handles the interaction with lighthouse door.
         */

        on(Scenery.DOORWAY_4577, IntType.SCENERY, "walk-through") { player, node ->
            val questProgress       = getVarbit(player, Vars.VARBIT_QUEST_HORROR_FROM_THE_DEEP_PROGRESS_34)
            val lightHouseUnlocked  = getVarbit(player, HorrorFromTheDeep.LIGHTHOUSE_UNLOCKED)
            val bridgeLeftRepaired  = getVarbit(player, HorrorFromTheDeep.BRIDGE_LEFT_REPAIRED)
            val bridgeRightRepaired = getVarbit(player, HorrorFromTheDeep.BRIDGE_RIGHT_REPAIRED)
            val questComplete       = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)

            if(questProgress == 0) {
                sendDialogue(player, "You can't see any way to open the door.")
                return@on true
            }

            if(lightHouseUnlocked == 0 || bridgeLeftRepaired == 0 || bridgeRightRepaired == 0) {
                sendNPCDialogue(player, NPCs.LARRISSA_1336, "Please adventurer... We are both curious as to what has happened in that lighthouse, but you need to fix the bridge for me!", FaceAnim.SAD)
                return@on true
            }

            if(bridgeLeftRepaired == 1 && bridgeRightRepaired == 2 || lightHouseUnlocked == 1 || questComplete) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                return@on true
            }

            val outsideLighthouse = player.location.y < 3636
            val teleportLocation  = if (!outsideLighthouse) Location(2509, 3635, 0) else Location(2445, 4596, 0)
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            queueScript(player, 2, QueueStrength.SOFT) { stage : Int ->
                when(stage) {
                    0 -> {
                        sendMessage(player, "You unlock the Lighthouse front door.")
                        setVarbit(player, HorrorFromTheDeep.LIGHTHOUSE_UNLOCKED, 1, true)
                        return@queueScript delayScript(player, 3)
                    }
                    1 -> {
                        teleport(player, teleportLocation)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        /*
         * Handles the light mechanism fix.
         */

        onUseWith(IntType.SCENERY, TOOLS, Scenery.LIGHTING_MECHANISM_4588) { player, used, with ->
            val toolID = used.id
            val (firstFix, secondFix, thirdFix) = arrayOf(
                getVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_FIRST_FIX),
                getVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_SECOND_FIX),
                getVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_THIRD_FIX)
            )

            if (thirdFix == 1) {
                sendMessage(player, "You've already repaired the lighthouse.")
                return@onUseWith true
            }

            when (toolID) {
                TOOLS[0] -> if (firstFix != 1) {
                    removeItem(player, Item(toolID), Container.INVENTORY)
                    setVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_FIRST_FIX, 1, true)
                    sendMessage(player, "You use the swamp tar to make the torch flammable again.")
                }
                TOOLS[1] -> if (firstFix == 1 && secondFix == 0) {
                    setVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_SECOND_FIX, 1, true)
                    sendMessage(player, "You light the torch with your tinderbox.")
                }
                TOOLS[2] -> if (secondFix == 1 && thirdFix == 0) {
                    removeItem(player, Item(Items.MOLTEN_GLASS_1775), Container.INVENTORY)
                    setVarbit(player, HorrorFromTheDeep.LIGHTING_MECHANISM_THIRD_FIX, 1, true)
                    sendMessage(player, "You use the molten glass to repair the lens.")
                    sendMessage(player, "You have managed to repair the lighthouse torch!")
                    replaceScenery(with.asScenery(), Scenery.LIGHTING_MECHANISM_4587, 80)
                }
                else -> sendMessage(player, "You've already repaired the lighthouse.")
            }
            return@onUseWith true
        }

        /*
         * Handles study the strange wall.
         */

        on(STRANGE_WALL, IntType.SCENERY, "study") { player, _ ->
            when (player.location.y) {
                4626, 10002 -> {
                    setAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_DISCOVER, true)
                    openInterface(player, Components.HORROR_METALDOOR_142).also {
                        val currentItem = getAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_PROGRESS, 0)
                        val currentAttributes =
                            listOf(
                                Items.FIRE_RUNE_554     to 2,
                                Items.AIR_RUNE_556      to 3,
                                Items.EARTH_RUNE_557    to 4,
                                Items.WATER_RUNE_555    to 5,
                                Items.BRONZE_ARROW_882  to 6,
                                Items.BRONZE_SWORD_1277 to 7
                            )

                        currentAttributes.forEach { (attribute, componentIndex) ->
                            setComponentVisibility(
                                player,
                                Components.HORROR_METALDOOR_142,
                                componentIndex,
                                currentItem != attribute,
                            )
                        }

                        if (getAttribute(player, GameAttributes.QUEST_HFTD_UNLOCK_DOOR, 0) > 5) {
                            closeInterface(player)
                            sendMessage(player, "You hear the sound of something moving within the wall.")
                            playAudio(player, Sounds.STRANGEDOOR_SOUND_1627, 1)
                            setVarbit(player, HorrorFromTheDeep.STRANGE_DOOR_UNLOCKED, 1, true)
                        }
                    }
                }
                else -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles using the required items on the strange wall.
         */

        onUseWith(IntType.SCENERY, STRANGE_WALL_ITEMS, Scenery.STRANGE_WALL_4543, Scenery.STRANGE_WALL_4544) { player, used, _ ->
            val currentItem = getAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_PROGRESS, 0)
            val item = getItemName(used.id).lowercase()

            if(currentItem == used.id) {
                sendDialogue(player, "You already placed a $item into the slot in the wall.")
                return@onUseWith false
            }

            sendDialogue(player, "I don't think I'll get that back if I put it in there.")
            addDialogueAction(player) { _, button ->
                if (button > 0) {
                    setTitle(player, 2)
                    sendDialogueOptions(player, "Really place the rune into the door?", "Yes", "No")
                    addDialogueAction(player) { _, _ ->
                        if (button == 2) {
                            removeItem(player, Item(used.id, 1), Container.INVENTORY)
                            sendMessage(player, "You place a $item into the slot in the wall.")
                            setAttribute(player, GameAttributes.QUEST_HFTD_STRANGE_WALL_PROGRESS, used.id)
                            player.incrementAttribute(GameAttributes.QUEST_HFTD_UNLOCK_DOOR)
                        }
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles opening the strange door.
         */

        on(STRANGE_WALL_DOOR, IntType.SCENERY, "open") { player, node ->
            val doorUnlocked = getVarbit(player, HorrorFromTheDeep.STRANGE_DOOR_UNLOCKED)
            val questComplete = isQuestComplete(player, Quests.HORROR_FROM_THE_DEEP)

            playAudio(player, Sounds.STRANGEDOOR_OPEN_1626)
            playAudio(player, Sounds.STRANGEDOOR_CLOSE_1625, 2)

            if(doorUnlocked == 1 || questComplete) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                return@on true
            }

            when (player.location.y) {
                4626, 10002 -> sendMessage(player, "You cannot see any way to move this part of the wall....")
                4627, 10003 -> sendMessage(player, "You cannot see anything unusual about the wall from this side.")
            }
            return@on true
        }

        /*
         * Handles bridge repair.
         */

        onUseWith(IntType.SCENERY, Items.PLANK_960, Scenery.BROKEN_BRIDGE_4615, Scenery.BROKEN_BRIDGE_4616) { player, _, bridge ->
            val isLeft = bridge.id == Scenery.BROKEN_BRIDGE_4615
            val leftRepaired = getVarbit(player, HorrorFromTheDeep.BRIDGE_LEFT_REPAIRED) == 1
            val rightRepaired = getVarbit(player, HorrorFromTheDeep.BRIDGE_RIGHT_REPAIRED) == 1

            if ((isLeft && leftRepaired) || (!isLeft && rightRepaired)) {
                sendDialogue(player, "You have already fixed the bridge.")
                return@onUseWith false
            }

            if (!inInventory(player, Items.HAMMER_2347)) {
                sendDialogue(player, "You need a hammer to force the nails in with.")
                return@onUseWith false
            }

            if (amountInInventory(player, Items.STEEL_NAILS_1539) < 30) {
                sendDialogue(player, "You need 30 steel nails to attach the plank with.")
                return@onUseWith false
            }

            if (removeItem(player, Item(Items.STEEL_NAILS_1539, 30)) && removeItem(player, Item(Items.PLANK_960, 1))) {
                lock(player, 3)
                animate(player, Animations.SMITH_HAMMER_898)

                if (isLeft) {
                    sendDialogue(player, "You create half a makeshift walkway out of the plank.")
                    setVarbit(player, HorrorFromTheDeep.BRIDGE_LEFT_REPAIRED, 1, true)
                } else if (leftRepaired) {
                    sendDialogue(player, "You have now made a makeshift walkway over the bridge.")
                    setVarbit(player, HorrorFromTheDeep.BRIDGE_RIGHT_REPAIRED, 1, true)
                } else {
                    sendMessage(player, "I might be able to make it to the other side.")
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

            when (bridgeUnlock) {
                1 -> {
                    player.animate(Animation(Animations.JUMP_BRIDGE_769), 1)
                    forceWalk(player, destination.transform(1, 0, 0), "like travolta")
                    teleport(player, destination, TeleportManager.TeleportType.INSTANT, 2)
                    runTask(player, 3) { forceWalk(player, destination.transform(1, 0, 0), "") }
                }
                2 -> {
                    forceMove(player, player.location, destination, 0, cycle, Direction.EAST, Animations.AGILITY_START_3276)
                    runTask(player, 6) { forceWalk(player, destination.transform(1, 0, 0), "like jeffrey") }
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
            runTask(player, 6) { forceWalk(player, Location.create(2595, 3608, 0), "jeffrey only") }
            return@on true
        }
    }
}
