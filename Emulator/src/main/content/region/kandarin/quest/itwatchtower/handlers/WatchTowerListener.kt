package content.region.kandarin.quest.itwatchtower.handlers

import core.game.dialogue.SequenceDialogue.npcLine
import core.game.dialogue.SequenceDialogue.playerLine
import core.game.dialogue.SequenceDialogue.sendSequenceDialogue
import content.region.kandarin.quest.itwatchtower.dialogue.BattlementDialogue
import content.region.kandarin.quest.itwatchtower.dialogue.OgreCityGateDialogue
import core.api.*
import core.api.quest.isQuestComplete
import core.api.ui.closeDialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogueLine
import core.game.dialogue.SequenceDialogue.options
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.*

class WatchTowerListener : InteractionListener {

    companion object {
        val OGRE_CITY_GATE = intArrayOf(Scenery.CITY_GATE_2788, Scenery.CITY_GATE_2789)
    }

    override fun defineListeners() {
        val bushes =
            mapOf(
                Scenery.BUSH_2798 to null,
                Scenery.BUSH_2799 to Pair(Items.FINGERNAILS_2384, "What's this? Disgusting! Some fingernails. They may be a clue though... I'd better take them."),
                Scenery.BUSH_2800 to Pair(Items.DAMAGED_DAGGER_2387, "Aha, a dagger! I wonder if this is evidence..."),
                Scenery.BUSH_2801 to Pair(Items.TATTERED_EYE_PATCH_2388, "I've found an eye patch; I had better show this to the Watchtower Wizard."),
                Scenery.BUSH_2802 to Pair(Items.OLD_ROBE_2385, "Aha! A robe. This could be a clue..."),
                Scenery.BUSH_2803 to Pair(Items.UNUSUAL_ARMOUR_2386, "Here's some armour; it could be evidence..."),
            )

        /*
         * Handles searching the bushes for quest items.
         */

        bushes.forEach { (bush, item) ->
            on(bush, IntType.SCENERY, "search") { player, _ ->
                lock(player, 3)
                animate(player, 800)
                searchBush(player, item)
                return@on true
            }
        }

        /*
         * Handles enter to skavid caves.
         */

        on(Scenery.CAVE_ENTRANCE_2806, IntType.SCENERY, "enter") { player, _ ->
            sendSequenceDialogue(
                player,
                dialogueLine("If your light source goes out down there you'll be in trouble! Are", "you sure you want to go in without a tinderbox?"),
                options("Select an Option", "I'll be fine without a tinderbox.", "I'll come back with a tinderbox.") { selected ->
                    when (selected) {
                        1 -> {
                            teleport(player, Location.create(2530, 9467, 0), TeleportManager.TeleportType.INSTANT)
                            sendMessage(player, "You enter the cave...")
                        }
                        2 -> closeDialogue(player)
                    }
                }
            )
            return@on true
        }

        /*
         * Handles exit from north skavid caves.
         */

        on(Scenery.CAVE_EXIT_2818, IntType.SCENERY, "leave") { player, _ ->
            teleport(player, Location(2524, 3070, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles open the chest.
         */

        onUseWith(IntType.SCENERY, Items.TOBANS_KEY_2378, Scenery.CHEST_2790) { player, _, with ->
            if(freeSlots(player) == 0) {
                sendMessage(player, "Not enough space in your inventory.")
                return@onUseWith true
            }

            if(inInventory(player, Items.TOBANS_GOLD_2393)) {
                sendMessage(player, "The chest is empty.")
                return@onUseWith true
            }

            if(!inInventory(player, Items.TOBANS_KEY_2378)) {
                sendPlayerDialogue(player, "I think I need a key of some sort...")
                sendMessage(player, "The chest is locked.")
            } else {
                sendMessage(player, "You use the key Og gave you.")
                replaceScenery(with.asScenery(), 2828, 3)
                sendItemDialogue(player, Items.TOBANS_GOLD_2393, "You find a stash of gold inside.")
                addItem(player, Items.TOBANS_GOLD_2393, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles opening skavid map.
         */

        on(Items.SKAVID_MAP_2376, IntType.ITEM, "read") { player, _ ->
            openInterface(player, 479)
            return@on true
        }

        /*
         * Handles entrance to grew island.
         */

        on(Scenery.CAVE_ENTRANCE_2811, IntType.SCENERY, "enter") { player, _ ->
            sendMessage(player, "You enter the cave.")
            teleport(player, Location(2576, 3029, 0))
            return@on true
        }

        /*
         * Handles ladder from grew island.
         */

        on(Scenery.LADDER_2812, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "You climb down the ladder.")
            teleport(player, Location(2499, 2988, 0))
            return@on true
        }

        /*
         * Handles use any one relic part with another.
         */

        onUseWith(IntType.NPC, Items.RELIC_PART_1_2373, Items.RELIC_PART_2_2374, Items.RELIC_PART_3_2375) { player, _, _ ->
            sendMessage(player, "I think these fit together, but I can't seem to make them fit.")
            sendMessage(player, "I am going to need someone with experience to help me with this.")
            return@onUseWith true
        }

        /*
         * Handles attack option after quest for gorad.
         */

        on(NPCs.GORAD_856, IntType.NPC, "attack") { player, node ->
            if (isQuestComplete(player, Quests.WATCHTOWER)) {
                sendNPCDialogueLines(player, node.id, FaceAnim.OLD_DEFAULT, false, "Ho Ho! why would I want to fight a worm?", "Get lost!")
            } else {
                player.attack(node)
            }
            return@on true
        }

        /*
         * Handles move through west ogre city gates.
         */

        on(OGRE_CITY_GATE, IntType.SCENERY, "open") { player, node ->
            if(player.location.x < 2504 || isQuestComplete(player, Quests.WATCHTOWER)) {
                sendNPCDialogue(player, NPCs.OGRE_GUARD_859, "It's the small creature; you may pass.", FaceAnim.OLD_DEFAULT)
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, OgreCityGateDialogue())
            }
            return@on true
        }

        /*
         * Handles use the relic on ogre guards.
         */

        onUseWith(IntType.NPC, Items.OGRE_RELIC_2372, NPCs.OGRE_GUARD_859) { player, _, _ ->
            openDialogue(player, OgreCityGateDialogue())
            return@onUseWith true
        }

        /*
         * Handles climb over battlement.
         */

        on(Scenery.BATTLEMENT_2832, IntType.SCENERY, "climb-over") { player, _ ->
            if(!isQuestComplete(player, Quests.WATCHTOWER)) {
                openDialogue(player, BattlementDialogue())
            } else {
                val destination = if(player.location.x > 2506) player.location.transform(-2,0,0) else player.location.transform(2,0,0)
                forceMove(player, player.location, destination, 0, 90, null, Animations.CLIMB_OVER_THING_5038)
                sendMessage(player, "You climb over the low wall.")
            }
            return@on true
        }

        /*
         * Handles talking to ogres near battlement.
         */

        on(NPCs.OGRE_GUARD_860, IntType.NPC, "talk-to") { player, node ->
            if(isQuestComplete(player, Quests.WATCHTOWER)) {
                sendNPCDialogue(player, node.id, "It's that creature again. This time we will let it go.", FaceAnim.OLD_DEFAULT)
            } else {
                openDialogue(player, BattlementDialogue())
            }
            return@on true
        }

        /*
         * Handles give the rock cake to ogre guards near battlement.
         */

        onUseWith(IntType.NPC, Items.ROCK_CAKE_2379, NPCs.OGRE_GUARD_860) { player, _, with ->
            val npc = with.asNpc()
            if (!inInventory(player, Items.ROCK_CAKE_2379)) return@onUseWith true

            if(isQuestComplete(player, Quests.WATCHTOWER)) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith true
            }

            if (removeItem(player, Items.ROCK_CAKE_2379)) {
                sendMessage(player, "You give the guard a rock cake.")
            }

            sendSequenceDialogue(
                player,
                playerLine(FaceAnim.HALF_THINKING, "How about this?"),
                npcLine(npc, FaceAnim.OLD_DEFAULT, "Well, well, look at this. My favourite: rock cake! Okay,", "we will let it through."),
                onComplete = {
                    val startLocation = Location.create(2506, 3012, 0)
                    val endLocation = Location.create(2508, 3012, 0)
                    forceMove(player, startLocation, endLocation, 60, 150, null, Animations.CLIMB_OVER_THING_5038)
                    sendMessage(player, "You climb over the low wall.")
                }
            )
            return@onUseWith true
        }

        /*
         * Handles talking to ogre trader near rock cake stall.
         */

        on(NPCs.OGRE_TRADER_875, IntType.NPC, "talk-to", "trade") { player, node ->
            val npc = node.asNpc()
            sendNPCDialogue(player, npc.id, "Arr, small thing wants my food, does it? I'll teach you to deal with ogres!", FaceAnim.OLD_ANGRY1).also {
                npc.attack(player)
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.CAVE_NIGHTSHADE_2398, NPCs.ENCLAVE_GUARD_870) { _, _, _ ->
            return@onUseWith true
        }
    }

    private fun searchBush(player: Player, item: Pair<Int, String>?): Boolean {
        when {
            item == null -> sendPlayerDialogue(player, "Hmmm, nothing here.", FaceAnim.NEUTRAL)
            !inInventory(player, item.first) -> {
                sendPlayerDialogue(player, item.second, FaceAnim.NEUTRAL)
                addItem(player, item.first)
            }
            else -> sendPlayerDialogue(player, "I have already searched this place.", FaceAnim.NEUTRAL)
        }
        return true
    }
}
