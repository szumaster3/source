package content.region.kandarin.gnome.quest.grandtree.plugin

import content.global.plugin.iface.ScrollInterface
import content.region.kandarin.gnome.quest.grandtree.cutscene.GloughsPetCutscene
import content.region.kandarin.gnome.quest.grandtree.dialogue.ForemanDialogue
import content.region.kandarin.gnome.quest.grandtree.dialogue.KingNarnodeDialogue
import content.region.kandarin.gnome.quest.grandtree.dialogue.KingNarnodeUnderGroundDialogue
import content.region.kandarin.gnome.quest.grandtree.dialogue.ShipyardWorkerDialogueFile
import content.region.karamja.quest.mm.dialogue.KingNarnodeMMDialogue
import core.api.*
import core.api.getQuestStage
import core.api.hasRequirement
import core.api.isQuestComplete
import core.api.setQuestStage
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class TheGrandTreePlugin : InteractionListener {
    fun unlockTUZODoor(player: Player) {
        if (getAttribute(player, TheGrandTreeUtils.TWIG_0, false) &&
            getAttribute(player, TheGrandTreeUtils.TWIG_1, false) &&
            getAttribute(player, TheGrandTreeUtils.TWIG_2, false) &&
            getAttribute(player, TheGrandTreeUtils.TWIG_3, false)
        ) {
            sendDialogue(player, "With a grinding of machinery, a trapdoor snaps open!")
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(NPCs.CHARLIE_673), "talk-to") { player, _ ->
            return@setDest player.location
        }
    }

    override fun defineListeners() {

        on(NPCs.FOREMAN_674, IntType.NPC, "talk-to") { player, npc ->
            if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                openDialogue(player, ForemanDialogue(), npc)
            } else {
                sendDialogue(player, "The foreman is too busy to talk.")
            }
            return@on true
        }

        on(NPCs.KING_NARNODE_SHAREEN_670, IntType.NPC, "talk-to") { player, npc ->
            val aboveground = 9782
            when {
                getQuestStage(player, Quests.THE_GRAND_TREE) == 100 ->
                    openDialogue(
                        player,
                        KingNarnodeMMDialogue(),
                        npc,
                    )

                player.location.getRegionId() == aboveground -> openDialogue(player, KingNarnodeDialogue(), npc)
                player.location.getRegionId() != aboveground -> openDialogue(player, KingNarnodeUnderGroundDialogue(), npc)
            }
            return@on true
        }

        on(TheGrandTreeUtils.HAZELMERE_SCROLL, IntType.ITEM, "read") { player, node ->
            scrollHandler(player, node.asItem())
            return@on true
        }

        on(TheGrandTreeUtils.LUMBER_ORDER_SCROLL, IntType.ITEM, "read") { player, node ->
            scrollHandler(player, node.asItem())
            return@on true
        }

        on(TheGrandTreeUtils.INVASION_PLANS_SCROLL, IntType.ITEM, "read") { player, _ ->
            ScrollInterface.scrollSetup(
                player,
                Components.MESSAGESCROLL_220,
                TheGrandTreeUtils.INVASION_PLANS_SCROLL_CONTENT,
            )
            return@on true
        }

        on(2444, IntType.SCENERY, "open") { player, node ->
            var loc = Location(2487, 3464, 2)
            if (node.location == loc && !isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                if (getAttribute(player, TheGrandTreeUtils.TWIG_0, false) &&
                    getAttribute(player, TheGrandTreeUtils.TWIG_1, false) &&
                    getAttribute(player, TheGrandTreeUtils.TWIG_2, false) &&
                    getAttribute(player, TheGrandTreeUtils.TWIG_3, false)
                ) {
                    player.animator.animate(Animation(827))
                    GloughsPetCutscene(player).start()
                }
            } else {
                sendDialogue(player, "The trapdoor won't open.")
            }
            return@on true
        }

        on(2446, IntType.SCENERY, "open") { player, node ->
            if (node.location == Location(2463, 3497, 0) && isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                player.animator.animate(Animation(828))
                teleport(player, Location(2464, 9897, 0))
            }
            return@on true
        }

        on(2436, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The chest is locked.")
            return@on true
        }

        onUseWith(IntType.SCENERY, 788, 2436) { player, _, _ ->
            if (!inInventory(player, Items.INVASION_PLANS_794)) {
                SceneryBuilder.replace(
                    Scenery(2436, Location(2482, 3462, 1)),
                    Scenery(2437, Location(2482, 3462, 1)),
                    2,
                )
                player.animator.animate(Animation(Animations.TAKE_FROM_CHEST_538))
                sendItemDialogue(player, Items.INVASION_PLANS_794, "You found a scroll!")
                addItemOrDrop(player, Items.INVASION_PLANS_794)
                if (getQuestStage(player, Quests.THE_GRAND_TREE) < 60) {
                    setQuestStage(player, Quests.THE_GRAND_TREE, 60)
                }
            } else {
                sendDialogue(player, "The chest is locked.")
            }
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_789, 2440) { player, used, with ->
            setAttribute(player, TheGrandTreeUtils.TWIG_0, true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_790, 2441) { player, used, with ->
            setAttribute(player, TheGrandTreeUtils.TWIG_1, true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_791, 2442) { player, used, with ->
            setAttribute(player, TheGrandTreeUtils.TWIG_2, true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.TWIGS_792, 2443) { player, used, with ->
            setAttribute(player, TheGrandTreeUtils.TWIG_3, true)
            removeItem(player, used.asItem())
            GroundItemManager.create(used.asItem(), with.location, player)
            unlockTUZODoor(player)
            return@onUseWith true
        }

        on(Items.TWIGS_789, IntType.GROUND_ITEM, "take") { player, _ ->
            setAttribute(player, TheGrandTreeUtils.TWIG_0, false)
            return@on true
        }
        on(Items.TWIGS_790, IntType.GROUND_ITEM, "take") { player, _ ->
            setAttribute(player, TheGrandTreeUtils.TWIG_1, false)
            return@on true
        }
        on(Items.TWIGS_791, IntType.GROUND_ITEM, "take") { player, _ ->
            setAttribute(player, TheGrandTreeUtils.TWIG_2, false)
            return@on true
        }
        on(Items.TWIGS_792, IntType.GROUND_ITEM, "take") { player, _ ->
            setAttribute(player, TheGrandTreeUtils.TWIG_3, false)
            return@on true
        }
        on(2435, IntType.SCENERY, "search") { player, _ ->
            if (getQuestStage(player, Quests.THE_GRAND_TREE) >= 47 &&
                !inInventory(
                    player,
                    Items.GLOUGHS_JOURNAL_785,
                )
            ) {
                sendItemDialogue(player, Items.GLOUGHS_JOURNAL_785, "You've found Glough's Journal!")
                addItemOrDrop(player, Items.GLOUGHS_JOURNAL_785)
            } else if (freeSlots(player) < 1) {
                sendDialogue(player, "You have no free space to hold any more items.")
            } else {
                sendDialogue(player, "The cupboard is empty.")
            }
            return@on true
        }

        on(32319, IntType.SCENERY, "search") { player, node ->
            if (getQuestStage(player, Quests.THE_GRAND_TREE) < 99 || player.hasItem(Item(Items.DACONIA_ROCK_793))) {
                return@on true
            }

            if (node.location ==
                TheGrandTreeUtils.ROOTS_LOCATION[
                    getAttribute(
                        player,
                        TheGrandTreeUtils.DRACONIA_ROCK,
                        1,
                    ),
                ]
            ) {
                sendItemDialogue(player, Item(Items.DACONIA_ROCK_793), "You've found a Daconia rock!")
                addItemOrDrop(player, Items.DACONIA_ROCK_793)
            }
            return@on true
        }

        on(TheGrandTreeUtils.KARAMJA_GATE, IntType.SCENERY, "open") { player, node ->

            val gatePair = when (node.id) {
                2438 -> Pair(2438, 2439)
                2439 -> Pair(2439, 2438)
                else -> null
            } ?: return@on false

            if (player.location.x > 2944 || isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                DoorActionHandler.autowalkFence(player, node.asScenery(), gatePair.first, gatePair.second)
            } else {
                openDialogue(player, ShipyardWorkerDialogueFile(), NPC(NPCs.SHIPYARD_WORKER_675))
            }

            return@on true
        }

        on(2451, IntType.SCENERY, "push") { player, roots ->
            if (hasRequirement(player, Quests.THE_GRAND_TREE)) {
                val outsideMine =
                    player.location == Location.create(2467, 9903, 0) ||
                            player.location ==
                            Location.create(
                                2468,
                                9903,
                                0,
                            )
                if (outsideMine) {
                    forceMove(player, player.location, player.location.transform(0, 2, 0), 25, 60, null, 819)
                } else {
                    forceMove(player, player.location, player.location.transform(0, -2, 0), 25, 60, null, 819)
                }
                animateScenery(roots.asScenery(), 452)
                player.animator.animate(Animation(Animations.PUSH_2572))
                playGlobalAudio(player.location, Sounds.TANGLEVINE_APPEAR_2316)
            }
            return@on true
        }
    }

    private fun scrollHandler(player: Player, item: Item) {
        val id = item.id

        openInterface(player, Components.BLANK_SCROLL_222).also {
            when (id) {
                TheGrandTreeUtils.HAZELMERE_SCROLL -> sendString(player, TheGrandTreeUtils.HAZELMERE_SCROLL_CONTENT.joinToString("<br>"), Components.BLANK_SCROLL_222, 5)
                TheGrandTreeUtils.LUMBER_ORDER_SCROLL -> sendString(player, TheGrandTreeUtils.LUMBER_ORDER_SCROLL_CONTENT.joinToString("<br>"), Components.BLANK_SCROLL_222, 1)
            }
        }
    }
}
