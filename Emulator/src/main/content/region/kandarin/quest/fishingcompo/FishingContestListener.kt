package content.region.kandarin.quest.fishingcompo

import content.data.GameAttributes
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.*
import core.game.node.entity.impl.PulseType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class FishingContestListener : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with tunnel stairs (White Wolf Mountain shortcut).
         */

        on(TUNNEL_STAIRS, IntType.SCENERY, "climb-down") { player, node ->
            if (!isQuestComplete(player, Quests.FISHING_CONTEST)) {
                when (node.id) {
                    Scenery.STAIRS_55 -> {
                        player.dialogueInterpreter.open(NPCs.VESTRI_3679, Repository.findNPC(NPCs.VESTRI_3679))
                    }

                    Scenery.STAIRS_57 -> {
                        player.dialogueInterpreter.open(NPCs.AUSTRI_232, Repository.findNPC(NPCs.AUSTRI_232))
                    }
                }
            } else {
                val destination = when (node.id) {
                    Scenery.STAIRS_55 -> Location(2820, 9882, 0)
                    Scenery.STAIRS_57 -> Location(2876, 9879, 0)
                    else -> return@on true
                }
                destination?.let { teleport(player, it) }
            }
            return@on true
        }

        /*
         * Handles interaction with Hemenster fence and McGrubor's gates.
         * Varbit ID: 2053
         */

        on(GATES, IntType.SCENERY, "open") { player, node ->
            when (node.id) {
                Scenery.GATE_47, Scenery.GATE_48 -> {
                    val shownPass = getVarbit(player, Vars.VARBIT_FISHING_CONTEST_PASS_SHOWN_2053)
                    when {
                        shownPass == 0 -> {
                            if (inInventory(player, Items.FISHING_PASS_27)) {
                                sendMessage(player, "You should give your pass to Morris.")
                            } else {
                                sendMessage(player, "You need a fishing pass to fish here.")
                            }
                        }

                        !inInventory(player, Items.FISHING_ROD_307) -> {
                            sendDialogueLines(
                                player,
                                "I should probably get a rod from",
                                "Grandpa Jack before starting."
                            )
                        }

                        else -> DoorActionHandler.autowalkFence(
                            player,
                            node.asScenery(),
                            Scenery.GATE_47,
                            Scenery.GATE_48
                        )
                    }
                }

                Scenery.GATE_52, Scenery.GATE_53 -> {
                    if (inBorders(player, 2647, 3468, 2652, 3469)) {
                        face(findNPC(NPCs.FORESTER_231)!!, player, 3)
                        sendNPCDialogue(
                            player,
                            NPCs.FORESTER_231,
                            "Hey! You can't come through here! This is private land!",
                            FaceAnim.ANGRY
                        )
                        sendMessage(
                            player,
                            "There might be a gap in the fence somewhere where he wouldn't see you sneak in."
                        )
                        sendMessage(player, "You should look around.")
                    } else {
                        sendDialogue(player, "This gate is locked.")
                    }
                }
            }
            return@on true
        }

        /*
         * Handles interaction with vines in McGrubor's Wood.
         */

        on(VINE_SCENERY, IntType.SCENERY, "check") { player, _ ->
            if (isQuestInProgress(player, Quests.FISHING_CONTEST, 1, 99)) {
                if (!inInventory(player, Items.SPADE_952, 1)) {
                    return@on true
                }

                queueScript(player, 1, QueueStrength.WEAK) {
                    sendMessage(player, "You dig in amongst the vines.")
                    animate(player, Animation(Animations.DIG_SPADE_830))
                    sendMessage(player, "You find a red vine worm.")
                    addItem(player, Items.RED_VINE_WORM_25, 1, Container.INVENTORY)
                    return@queueScript stopExecuting(player)
                }
            }
            return@on false
        }

        /*
         * Handles stash the garlic into pipe.
         */

        onUseWith(IntType.SCENERY, Items.GARLIC_1550, 41) { player, used, with ->
            val n = with as core.game.node.scenery.Scenery
            if(n.location == Location.create(2638, 3445, 0)) {
                sendItemDialogue(player, used.id, "You stash the garlic in the pipe.")
                player.inventory.remove(Item(Items.GARLIC_1550))
                setAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, true)
            }
            return@onUseWith true
        }

        /*
         * Handles search interaction with wall pipe.
         */

        on(Scenery.WALL_PIPE_41, IntType.SCENERY, "search") { player, _ ->
            if (getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                sendDialogue(player, "I shoved garlic up here.")
            } else {
                sendPlayerDialogue(player, "Ewww - it's a smelly sewage pipe.", FaceAnim.DISGUSTED)
            }
            return@on true
        }

        /*
         * Handles Bonzo NPC - pay option interaction.
         */

        on(NPCs.BONZO_225, IntType.NPC, "pay") { player, _ ->
            player.dialogueInterpreter.open(NPCs.BONZO_225, Repository.findNPC(NPCs.BONZO_225))
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Handle pipe.
         */
        setDest(IntType.SCENERY, intArrayOf(41), "search") { p, node ->
            val obj = node as core.game.node.scenery.Scenery
            return@setDest obj.location.transform(0, -1, 0)
        }

        setDest(IntType.SCENERY, 41) { p, node ->
            if(inInventory(p.asPlayer(), Items.GARLIC_1550)){
                return@setDest node.location
            }
            return@setDest node.location
        }
    }

    companion object {
        private val VINE_SCENERY = intArrayOf(
            Scenery.VINE_58,
            Scenery.VINE_2989,
            Scenery.VINE_2990,
            Scenery.VINE_2991,
            Scenery.VINE_2992,
            Scenery.VINE_2993,
            Scenery.VINE_2994,
            Scenery.VINE_2013
        )
        private val TUNNEL_STAIRS = intArrayOf(
            Scenery.STAIRS_55,
            Scenery.STAIRS_57
        )
        private val GATES = intArrayOf(
            Scenery.GATE_47,
            Scenery.GATE_48,
            Scenery.GATE_52,
            Scenery.GATE_53
        )
    }
}