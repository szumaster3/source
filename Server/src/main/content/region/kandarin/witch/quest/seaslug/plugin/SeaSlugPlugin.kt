package content.region.kandarin.witch.quest.seaslug.plugin

import content.region.kandarin.witch.quest.seaslug.cutscene.KennithCutscene
import content.region.kandarin.witch.quest.seaslug.dialogue.KennithDialogueFile
import core.api.*
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.Log
import shared.consts.*

class SeaSlugPlugin : InteractionListener {
    private fun getSmack(player: Player) {
        lock(player, 7)
        animate(player, LADDER_FAIL)
        runTask(player, 3) {
            sendMessage(player, "The fishermen approach you...")
            sendMessage(player, "and smack you on the head with a fishing rod!")
            impact(player, 4, HitsplatType.NORMAL)
            sendChat(player, "Ouch!", 1)
            // Between May and September, the player additionally says "Gah!".
        }
    }

    override fun defineListeners() {

        /*
         * Handles talk with Kennith behind boxes.
         */

        on(KENNITH, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, KennithDialogueFile())
            return@on true
        }

        /*
         * Handles creating dry stick.
         */

        onUseWith(IntType.ITEM, DAMP_STICK, BROKEN_GLASS) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                lock(player, 6)
                visualize(player, 4809, 791)
                playAudio(player, Sounds.SLUG_DRY_STICKS_3023)
                addItemOrDrop(player, DRY_STICK)
            }
            return@onUseWith true
        }

        /*
         * Handles rub together the dry stick.
         */

        on(DRY_STICK, IntType.ITEM, "rub-together") { player, _ ->
            if (inInventory(player, Items.LIT_TORCH_594)) {
                sendMessage(player, "You need to extinguish the torch first.")
                return@on true
            }
            if (!inInventory(player, UNLIT_TORCH)) {
                sendMessage(player, "You don't have the required items.")
                return@on true
            }

            if (getStatLevel(player, Skills.FIREMAKING) < 30) {
                sendMessage(player, "You rub together the dry sticks but nothing happens.")
                sendMessage(player, "You need a Firemaking level of 30 or above.")
                return@on true
            }

            if (removeItem(player, UNLIT_TORCH)) {
                sendMessage(player, "You rub together the dry sticks and the sticks catch alight.")
                sendMessage(player, "You place the smoulding twigs to your torch.", 1)
                sendMessage(player, "Your torch lights.", 1)
                addItemOrDrop(player, LIT_TORCH)
                playAudio(player, Sounds.SLUG_TORCH_LIT_3028)
                setQuestStage(player, Quests.SEA_SLUG, 20)
            }

            return@on true
        }

        /*
         * Handles using lit torch on sea slug.
         */

        onUseWith(IntType.NPC, LIT_TORCH, SEA_SLUG) { player, _, _ ->
            sendMessage(player, "The sea slug curls up into its shell.")
            return@onUseWith true
        }

        /*
         * Handles using lit torch on fisherman NPCs.
         */

        onUseWith(IntType.NPC, LIT_TORCH, *FISHERMAN) { player, _, _ ->
            sendMessage(player, "The fisherman covers his face.")
            sendMessage(player, "He seems afraid of your torch.")
            return@onUseWith true
        }

        /*
         * Handles interaction with wall.
         */

        on(WALL, IntType.SCENERY, "kick") { player, node ->
            lock(player, 4)
            animate(player, KICK_ANIM)
            playAudio(player, Sounds.SLUG_KICK_PANEL_3024)

            sendMessage(player, "You kick the loose panel.")

            if(getQuestStage(player, Quests.SEA_SLUG) < 20) {
                sendMessage(player, "But nothing interesting happens.")
            } else {
                sendMessage(player, "The wood is rotten and crumbles away...", 1)
                sendMessage(player, "...leaving an opening big enough for Kenneth to climb through.", 2)
                replaceScenery(node.asScenery(), BROKEN_WALL, -1)
                setQuestStage(player, Quests.SEA_SLUG, 25)
            }
            return@on true
        }

        /*
         * Handles rotating the crane.
         */

        on(CRANE, IntType.SCENERY, "rotate") { player, _ ->
            if (getQuestStage(player, Quests.SEA_SLUG) == 30) {
                playAudio(player, Sounds.SLUG_CRANE_TURN_3021)
                KennithCutscene(player).start()
            } else {
                sendMessage(player, "You rotate the crane around.")
            }
            return@on true
        }

        /*
         * Handles the roadblock interaction.
         */

        on(LADDER, IntType.SCENERY, "climb-up") { player, node ->
            sendMessage(player, "You attempt to climb up the ladder.")

            // Before speak with Kent.
            if (getQuestStage(player, Quests.SEA_SLUG) in 5..10) {
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
                return@on true
            }

            // After speak with Kent with/without torch.
            if (inInventory(player, Items.LIT_TORCH_594) && getQuestStage(player, Quests.SEA_SLUG) >= 20) {
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-up")
                sendMessage(player, "The fishermen seem afraid of your torch.", 1)

            } else {
                getSmack(player)
            }

            return@on true
        }

        /*
         * Handles interaction with sea slug.
         */

        on(SEA_SLUG, IntType.NPC, "take") { player, _ ->
            runTask(player, 1) {
                animate(player, 1114)
                playAudio(player, Sounds.SEASLUG_HIT_3019)
                sendMessage(player, "You pick up the sea slug.")
                sendMessage(player, "It sinks its teeth deep into your hand.")
                sendMessage(player, "You drop the sea slug.")
                impact(player, 3, HitsplatType.NORMAL)
                sendChat(player, "Ouch!")
            }
            return@on true
        }

        /*
         * Handles mix the swamp tar into raw swamp paste.
         */

        onUseWith(IntType.ITEM, POT_OF_FLOUR, SWAMP_TAR) { player, used, with ->
            if (!hasSpaceFor(player, Item(RAW_SWAMP_PASTE, 1))) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }

            if (removeItem(player, Item(with.id, 1), Container.INVENTORY)) {
                playAudio(player, Sounds.SLUG_SMEAR_PASTE_3026)
                sendMessage(player, "You mix the flour with the swamp tar.")
                sendMessage(player, "It mixes into a paste.")
                replaceSlot(player, used.index, Item(EMPTY_POT, 1))
                addItem(player, RAW_SWAMP_PASTE, 1)
            } else {
                log(this.javaClass, Log.ERR, "Failed to remove item [id=${with.id}] from player [id=${player.id}, name=${player.name}] inventory.")
            }
            return@onUseWith true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, intArrayOf(KENNITH), "talk-to") { _, _ ->
            return@setDest Location.create(2764, 3286, 1)
        }
        setDest(IntType.SCENERY, intArrayOf(CRANE), "rotate") { _, _ ->
            return@setDest Location.create(2772, 3291, 1)
        }
    }

    companion object {
        const val KENNITH = NPCs.KENNITH_4863

        const val LADDER = Scenery.LADDER_18324
        const val CRANE = Scenery.CRANE_18327
        const val WALL = Scenery.BADLY_REPAIRED_WALL_18381
        const val BROKEN_WALL = Scenery.BROKEN_WALL_18380

        const val DRY_STICK = Items.DRY_STICKS_1468
        const val DAMP_STICK = Items.DAMP_STICKS_1467
        const val BROKEN_GLASS = Items.BROKEN_GLASS_1469
        const val UNLIT_TORCH = Items.UNLIT_TORCH_596
        const val LIT_TORCH = Items.LIT_TORCH_594
        const val SEA_SLUG = NPCs.SEA_SLUG_1006

        const val KICK_ANIM = Animations.SEA_SLUG_KICK_WALL_4804
        const val LADDER_FAIL = Animations.SEA_SLUG_PLATFORM_LADDER_4785

        private const val EMPTY_POT = Items.EMPTY_POT_1931
        private const val POT_OF_FLOUR = Items.POT_OF_FLOUR_1933
        private const val SWAMP_TAR = Items.SWAMP_TAR_1939
        private const val RAW_SWAMP_PASTE = Items.RAW_SWAMP_PASTE_1940

        val FISHERMAN = intArrayOf(NPCs.FISHERMAN_702, NPCs.FISHERMAN_703, NPCs.FISHERMAN_704)
    }
}
