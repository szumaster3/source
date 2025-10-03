package content.region.kandarin.feldip.jiggig.quest.zogre.plugin

import content.region.kandarin.feldip.jiggig.quest.zogre.dialogue.*
import content.region.kandarin.feldip.jiggig.quest.zogre.npc.ZavisticRarveNPC.Companion.spawnWizard
import core.api.*
import core.api.openNpcShop
import core.api.removeGroundItem
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.BLUE
import shared.consts.*

class ZogreFleshEatersPlugin : InteractionListener {
    companion object {
        const val SITHIK = Scenery.SITHIK_INTS_6888
        const val SITHIK_OGRE = Scenery.SITHIK_INTS_6889
        const val CUPBOARD = Scenery.CUPBOARD_6876
        const val WARDROBE = Scenery.WARDROBE_6877
        const val DRAWERS = Scenery.DRAWERS_6875
    }

    override fun defineListeners() {
        on(intArrayOf(Scenery.CRUSHED_BARRICADE_6881, Scenery.CRUSHED_BARRICADE_6882), IntType.SCENERY, "climb-over") { player, _ ->
            submitIndividualPulse(
                player,
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 ->
                                forceMove(
                                    player,
                                    player.location,
                                    player.location.transform(
                                        if (player.location.x < 2457) Direction.EAST else Direction.WEST,
                                        2,
                                    ),
                                    20,
                                    60,
                                    null,
                                    10980,
                                )

                            1 -> {
                                resetAnimator(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.GRISH_2038) { player, used, _ ->
            when (used.id) {
                Items.DRAGON_INN_TANKARD_4811 -> openDialogue(player, GrishDialogue())
                Items.BLACK_PRISM_4808 -> openDialogue(player, GrishBlackPrismDialogueFile())
                Items.TORN_PAGE_4809 -> openDialogue(player, GrishTornPageDialogueFile())
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.BARTENDER_739) { player, used, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) >= 3) {
                when (used.id) {
                    Items.DRAGON_INN_TANKARD_4811 -> openDialogue(player, BartenderDialogue())
                    Items.BLACK_PRISM_4808 -> openDialogue(player, BartenderBlackPrismDialogueFile())
                    Items.TORN_PAGE_4809 -> openDialogue(player, BartenderTornPageDialogueFile())
                    ZogreUtils.UNREALIST_PORTRAIT -> openDialogue(player, BartenderWrongPortraitDialogueFile())
                    ZogreUtils.REALIST_PORTRAIT -> openDialogue(player, BartenderCorrectPortraitDialogueFile())
                    else -> sendMessage(player, "Nothing interesting happens.")
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, ZogreUtils.QUEST_ITEMS, NPCs.ZAVISTIC_RARVE_2059) { player, used, _ ->
            when (used.id) {
                Items.BLACK_PRISM_4808 -> {
                    if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13) {
                        openDialogue(player, ZavisticRarveSellBlackPrismDialogue())
                    } else {
                        openDialogue(player, ZavisticRarveBlackPrismDialogue())
                    }
                }

                else -> sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(Scenery.BELL_6847, IntType.SCENERY, "ring") { player, _ ->
            if (getAttribute(player, ZogreUtils.NPC_ACTIVE, false)) {
                sendMessage(player, "You can't do that right now.")
                return@on true
            }

            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) in 4..12) {
                playGlobalAudio(player.location, Sounds.ZOGRE_BELL_1959)
                spawnWizard(player)
            }
            return@on true
        }

        on(Items.SIGNED_PORTRAIT_4816, IntType.ITEM, "look-at") { player, _ ->
            sendItemDialogue(
                player,
                Items.SIGNED_PORTRAIT_4816,
                "You see an image of Sithik with a message underneath$BLUE'I, the bartender of the Dragon Inn, do swear that this is the true likeness of the wizzy who was talking to Brentle Vahn, my customer the other day.'",
            )
            return@on true
        }

        on(NPCs.UGLUG_NAR_2039, IntType.NPC, "trade") { player, _ ->
            if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 7) {
                sendNPCDialogue(
                    player,
                    NPCs.UGLUG_NAR_2039,
                    "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.",
                    FaceAnim.OLD_DEFAULT,
                )
            } else {
                openNpcShop(player, NPCs.UGLUG_NAR_2039)
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.RELICYMS_BALM3_4844, NPCs.UGLUG_NAR_2039) { player, _, _ ->
            if ((getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) < 7)) {
                sendNPCDialogue(
                    player,
                    NPCs.UGLUG_NAR_2039,
                    "Me's not got no glug-glugs to sell, yous bring me da sickies glug-glug den me's open da stufsies for ya.",
                    FaceAnim.OLD_DEFAULT,
                )
            } else {
                openDialogue(player, UglugNarDialogue())
            }
            return@onUseWith true
        }

        on(SITHIK, IntType.SCENERY, "talk-to") { player, _ ->
            val dialogue = when {
                getAttribute(player, ZogreUtils.SITHIK_DIALOGUE_UNLOCK, false) -> SithikQuestDialogueFile()
                inInventory(player, ZogreUtils.STRANGE_POTION) -> SithikIntsStrangePotionDialogueFile()
                getAttribute(player, ZogreUtils.TALK_WITH_ZAVISTIC_DONE, false) -> SithikDialogue()
                getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 0 -> SithikPermissionDialogueFile()
                else -> SithikDialogue()
            }
            openDialogue(player, dialogue)
            return@on true
        }

        on(SITHIK_OGRE, IntType.SCENERY, "talk-to") { player, _ ->
            val dialogue = if (
                getVarbit(player, Vars.VARBIT_QUEST_SITHIK_OGRE_TRANSFORMATION_495) == 1 &&
                getAttribute(player, ZogreUtils.TALK_WITH_SITHIK_OGRE_DONE, false)
            ) {
                SithikTalkAgainAfterTransformDialogueFile()
            } else {
                SithikIntsAfterTransformDialogueFile()
            }
            openDialogue(player, dialogue)
            return@on true
        }

        listOf(CUPBOARD to ZogreUtils.NECROMANCY_BOOK, WARDROBE to ZogreUtils.HAM_BOOK).forEach { (furniture, item) ->
            on(furniture, IntType.SCENERY, "search") { player, _ ->
                if (!getAttribute(player, ZogreUtils.ASK_SITHIK_AGAIN, false)) {
                    sendMessage(player, "You search but find nothing.")
                } else if (!inInventory(player, item) && freeSlots(player) < 1) {
                    sendDialogue(player, "You see an item but don't have enough inventory space.")
                } else {
                    val name = if (item == ZogreUtils.HAM_BOOK) "a book on Philosophy" else "a book on Necromancy"
                    sendItemDialogue(player, item, "You find $name.")
                    addItemOrDrop(player, item, 1)
                }
                return@on true
            }
        }

        on(DRAWERS, IntType.SCENERY, "search") { player, _ ->
            val hasCharcoal = inInventory(player, Items.CHARCOAL_973)
            val hasPapyrus = inInventory(player, Items.PAPYRUS_970)
            val hasPortraitBook = inInventory(player, ZogreUtils.PORTRAI_BOOK)
            val needsSpace = { needed: Int -> freeSlots(player) < needed }

            if (!getAttribute(player, ZogreUtils.ASK_SITHIK_AGAIN, false)) {
                sendDialogue(player, "You search but find nothing.")
                return@on true
            }

            fun giveItemOrMessage(item: Int, message: String) {
                if (needsSpace(1)) {
                    sendDialogue(player, "You need free inventory space.")
                } else {
                    sendItemDialogue(player, item, message)
                    addItemOrDrop(player, item, 1)
                }
            }

            when {
                hasCharcoal && hasPapyrus -> {
                    if (needsSpace(1)) {
                        sendDialogue(player, "You need free inventory space.")
                    } else if (!hasPortraitBook) {
                        sendItemDialogue(player, ZogreUtils.PORTRAI_BOOK, "You find a book on portraiture.")
                        addItemOrDrop(player, ZogreUtils.PORTRAI_BOOK, 1)
                    } else {
                        sendDialogue(player, "You search but find nothing.")
                    }
                }

                hasCharcoal -> giveItemOrMessage(Items.PAPYRUS_970, "You find some papyrus.")
                hasPapyrus -> giveItemOrMessage(Items.CHARCOAL_973, "You find some charcoal.")
                else -> {
                    if (needsSpace(3)) {
                        sendDialogue(player, "You need 3 free inventory spaces.")
                    } else {
                        sendDoubleItemDialogue(player, Items.CHARCOAL_973, Items.PAPYRUS_970, "You find some charcoal and papyrus.")
                        addItemOrDrop(player, Items.CHARCOAL_973, 1)
                        addItemOrDrop(player, Items.PAPYRUS_970, 1)
                    }
                }
            }
            return@on true
        }

        onUseWith(IntType.ITEM, Items.TORN_PAGE_4809, ZogreUtils.NECROMANCY_BOOK) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendDoubleItemDialogue(
                    player,
                    ZogreUtils.NECROMANCY_BOOK,
                    Items.TORN_PAGE_4809,
                    "The torn page matches the book.",
                )
                setAttribute(player, "/save:${ZogreUtils.TORN_PAGE_ON_NECRO_BOOK}", true)
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, ZogreUtils.QUEST_ITEMS, SITHIK) { player, used, _ ->
            val item = used.id

            if (item == Items.PAPYRUS_970) {
                if (!inInventory(player, Items.CHARCOAL_973)) {
                    sendDialogue(player, "You have no charcoal with which to sketch this subject.")
                } else {
                    animate(player, Animations.HUMAN_PAINT_ON_ITEM_909)
                    openDialogue(player, SithikIntsPortraitDialogueFile())
                }
                return@onUseWith true
            }

            val dialogueFile = when (item) {
                ZogreUtils.PORTRAI_BOOK -> SithikIntsPortraitureBookDialogueFile()
                ZogreUtils.HAM_BOOK -> SithikIntsHamBookDialogueFile()
                ZogreUtils.NECROMANCY_BOOK -> if (getAttribute(player, ZogreUtils.TORN_PAGE_ON_NECRO_BOOK, false)) SithikIntsNecromancyBookDialogueFile() else null
                Items.TORN_PAGE_4809 -> SithikIntsTornPageDialogueFile()
                Items.BLACK_PRISM_4808 -> SithikIntsBlackPrismDialogueFile()
                Items.DRAGON_INN_TANKARD_4811 -> SithikIntsDragonTankardDialogueFile()
                ZogreUtils.REALIST_PORTRAIT, ZogreUtils.UNREALIST_PORTRAIT -> SithikIntsUsedPortraitDialogueFile()
                ZogreUtils.SIGNED_PORTRAIT -> SithikIntsSignedPortraitDialogueFile()
                ZogreUtils.STRANGE_POTION -> SithikIntsStrangePotionDialogueFile()
                else -> null
            }

            if (dialogueFile != null) {
                openDialogue(player, dialogueFile)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(Items.CUP_OF_TEA_4838, IntType.GROUND_ITEM, "take") { player, node ->
            val cup = node as GroundItem
            if (cup.location == Location(2593, 3103, 1)) {
                sendNPCDialogue(
                    player,
                    NPCs.SITHIK_INTS_2061,
                    "Hey! What do you think you're doing? Leave my tea alone!",
                    FaceAnim.ANNOYED,
                )
            } else if (freeSlots(player) < 1) {
                sendMessage(player, "You don't have enough inventory space.")
            } else {
                removeGroundItem(cup)
                addItem(player, Items.CUP_OF_TEA_4838)
            }
            return@on true
        }

        onUseWith(IntType.GROUND_ITEM, ZogreUtils.STRANGE_POTION, Items.CUP_OF_TEA_4838) { player, used, _ ->
            lock(player, 2)
            animate(player, 537)
            replaceSlot(player, used.asItem().index, Item(Items.SAMPLE_BOTTLE_3377))
            sendItemDialogue(player, ZogreUtils.STRANGE_POTION, "You pour some of the potion into the cup.")
            setAttribute(player, "/save:${ZogreUtils.SITHIK_TURN_INTO_OGRE}", true)
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.BELL_6847), "ring") { _, _ ->
            return@setDest Location(2598, 3086, 0)
        }
    }
}
