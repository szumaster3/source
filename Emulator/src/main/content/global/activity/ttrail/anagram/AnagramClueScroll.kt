package content.global.activity.ttrail.anagram

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import content.global.activity.ttrail.TreasureTrailManager
import content.global.activity.ttrail.challenge.ChallengeClueScroll
import content.global.activity.ttrail.puzzle.PuzzleBox
import core.api.*
import core.api.ui.setInterfaceText
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Abstract class representing an anagram clue scroll in a treasure trail.
 */
abstract class AnagramClueScroll(
    name: String?,
    clueId: Int,
    val anagram: String?,
    val npcId: Int,
    level: ClueLevel?,
    val challenge: Int? = null
) : ClueScrollPlugin(name, clueId, level, Components.TRAIL_MAP09_345) {

    /**
     * Handles player interaction with the NPC for this clue.
     */
    override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
        val player = e!!.asPlayer()
        val npc = target!!.asNpc()
        val anagramClue = getClueForNpc(player, npc) ?: return false

        val givePuzzle = listOf(true, false).random()

        if (givePuzzle && anagramClue.challenge != null) {
            val puzzle = PuzzleBox.fromItemId(anagramClue.challenge)
            if (puzzle != null) {
                val hasPuzzle = player.inventory.contains(puzzle.item.id, 1)
                val isComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzle.key)

                if (!hasPuzzle) {
                    if (!player.inventory.remove(Item(anagramClue.clueId, 1))) return false
                    player.setAttribute("anagram_clue_active", anagramClue.clueId)
                    player.inventory.add(puzzle.item)
                    val messages = listOf(
                        "Oh, I have a puzzle for you to solve.",
                        "Oh, I've been expecting you.",
                        "The solving of this puzzle could be the key to your treasure."
                    )
                    sendNPCDialogue(player, npcId, messages.random(), if (npc.id == NPCs.GNOME_COACH_2802 || npc.id == NPCs.GNOME_BALL_REFEREE_635) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING)
                    addDialogueAction(player) { p, btn ->
                        if (btn > 0) sendItemDialogue(p, puzzle.item, "${npc.name} has given you a puzzle box!")
                    }
                } else if (isComplete) {
                    if (!player.inventory.remove(puzzle.item)) return false
                    removeAttributes(
                        player,
                        "${puzzle.key}:puzzle:done",
                        "${puzzle.key}:puzzle:data",
                        "anagram_clue_active"
                    )
                    sendNPCDialogue(player, npc.id, "Well done, traveller.", FaceAnim.HALF_GUILTY)

                    addDialogueAction(player) { p, btn ->
                        if (btn > 0) {
                            val manager = TreasureTrailManager.getInstance(p)
                            val clueScroll = getClueScrolls()[anagramClue.clueId]
                            clueScroll?.reward(p)
                            if (manager.isCompleted) {
                                sendItemDialogue(p, Items.CASKET_405, "You've found a casket!")
                                manager.clearTrail()
                            } else {
                                val newClue = getClue(clueScroll?.level)
                                if (newClue != null) {
                                    sendItemDialogue(p, newClue, "You receive another clue scroll.")
                                    p.inventory.add(newClue)
                                }
                            }
                        }
                    }
                }
                return true
            }
        } else {
            val challengeClue = ChallengeClueScroll.getClueForNpc(player, npc)
            if (challengeClue != null) {
                if (!player.inventory.remove(Item(anagramClue.clueId, 1))) return false
                player.setAttribute("anagram_clue_active", anagramClue.clueId)
                player.inventory.add(Item(challengeClue.clueId, 1))

                sendNPCDialogue(player, npc.id, "I have a challenge for you.", FaceAnim.HALF_ASKING)
                addDialogueAction(player) { p, btn ->
                    if (btn > 0) sendItemDialogue(
                        p,
                        Item(challengeClue.clueId, 1),
                        "${npc.name} has given you a challenge scroll!"
                    )
                }
                return true
            }
        }
        return false
    }

    /**
     * Shows clue text to the player.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        repeat(8) { index ->
            setInterfaceText(player, "", interfaceId, index + 1)
        }

        super.read(player)
        setInterfaceText(
            player,
            "<br><br><br><br>This anagram reveals<br>who to speak to next:<br><br><br>$anagram",
            interfaceId,
            1
        )
    }

    companion object {
        /**
         * Finds the first anagram clue scroll in the player inventory that is associated with the given NPC.
         *
         * @param player The player whose inventory will be searched.
         * @param npc The NPC to match the clue scroll against.
         * @return The matching [AnagramClueScroll] if found; otherwise, `null`.
         */
        fun getClueForNpc(player: Player, npc: NPC): AnagramClueScroll? {
            // Inventory
            val fromInventory = player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { getClueScrolls()[it.id] as? AnagramClueScroll }
                .firstOrNull { it.npcId == npc.id }

            if (fromInventory != null) return fromInventory

            // Attributes
            val clueId = player.getAttribute("anagram_clue_active", -1)
            val fromAttribute = getClueScrolls()[clueId]
            return if (fromAttribute is AnagramClueScroll && fromAttribute.npcId == npc.id) {
                fromAttribute
            } else null
        }

        /**
         * Handles giving the player either a puzzle box or a challenge clue scroll when interacting with an NPC.
         *
         * @param player The player interacting with the NPC.
         * @param npc The NPC involved in the interaction.
         * @return `true` if a clue or puzzle was given or completed; `false` otherwise.
         */
        fun handleClue(player: Player, npc: NPC): Boolean {
            val anagramClue = getClueForNpc(player, npc) ?: return false

            // Determine facial expression for NPCs.
            val facialExpression = if (npc.id in intArrayOf(
                    NPCs.UGLUG_NAR_2039,
                    NPCs.GNOME_COACH_2802,
                    NPCs.GNOME_BALL_REFEREE_635
                )
            ) FaceAnim.OLD_DEFAULT else FaceAnim.HALF_ASKING

            // Check inventory space first.
            if (freeSlots(player) == 0) {
                sendNPCDialogue(
                    player,
                    npc.id,
                    "Your inventory is full, make some room first.",
                    facialExpression
                )
                return false
            }

            // Decide randomly whether to give puzzle or challenge clue.
            val givePuzzle = kotlin.random.Random.nextBoolean()

            // Handle Puzzle Box logic.
            if (givePuzzle && anagramClue.challenge != null) {
                val puzzle = PuzzleBox.fromItemId(anagramClue.challenge)
                if (puzzle != null) {
                    val hasPuzzle = player.inventory.contains(puzzle.item.id, 1)
                    val isComplete = PuzzleBox.hasCompletePuzzleBox(player, puzzle.key)

                    if (!hasPuzzle) {
                        // Give puzzle box.
                        if (!player.inventory.remove(Item(anagramClue.clueId, 1))) return false
                        player.setAttribute("anagram_clue_active", anagramClue.clueId)
                        player.inventory.add(puzzle.item)

                        val message = if (npc.id == NPCs.RAMARA_DU_CROISSANT_3827)
                            "I've ze puzzle for you to solve."
                        else if (npc.id == NPCs.UGLUG_NAR_2039) {
                            "You want puzzle?"
                        } else {
                            "Oh, I have a puzzle for you to solve."
                        }

                        sendNPCDialogue(player, npc.id, message, facialExpression)

                        addDialogueAction(player) { p, btn ->
                            if (btn > 0) sendItemDialogue(p, puzzle.item, "${npc.name} has given you a puzzle box!")
                        }

                    } else if (isComplete) {
                        // Puzzle completed, remove and reward.
                        if (!player.inventory.remove(puzzle.item)) return false

                        removeAttributes(
                            player,
                            "${puzzle.key}:puzzle:done",
                            "${puzzle.key}:puzzle:data",
                            "anagram_clue_active"
                        )
                        val randomMessage = arrayOf("Here is your reward!", "Well done, traveller.").random()
                        val message = if (npc.id == NPCs.RAMARA_DU_CROISSANT_3827)
                            "Zat's wonderful!"
                        else if (npc.id == NPCs.UGLUG_NAR_2039) {
                            "Dere you go!"
                        } else {
                            randomMessage
                        }
                        sendNPCDialogue(player, npc.id, message, facialExpression)

                        addDialogueAction(player) { p, btn ->
                            if (btn > 0) {
                                val manager = TreasureTrailManager.getInstance(p)
                                val clueScroll = getClueScrolls()[anagramClue.clueId]
                                clueScroll?.reward(p)

                                if (manager.isCompleted) {
                                    sendItemDialogue(p, Items.CASKET_405, "You've found a casket!")
                                    manager.clearTrail()
                                } else {
                                    val newClue = getClue(clueScroll?.level)
                                    if (newClue != null) {
                                        sendItemDialogue(p, newClue, "You receive another clue scroll.")
                                        p.inventory.add(newClue)
                                    }
                                }
                            }
                        }
                    }
                    return true
                }
            }

            // Handle Challenge Clue Scroll logic.
            val challengeClue = ChallengeClueScroll.getClueForNpc(player, npc)
            if (challengeClue != null && !player.inventory.contains(challengeClue.clueId, 1)) {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> {
                                    npc(facialExpression, "Ah! Here you go!")
                                    stage = 1
                                }
                                1 -> {
                                    player("What?")
                                    stage++
                                }
                                2 -> {
                                    npc(facialExpression, "I need you to answer this for me.")
                                    stage++
                                }
                                3 -> {
                                    end()
                                    player.setAttribute("anagram_clue_active", challengeClue.clueId)
                                    sendItemDialogue(player, Item(challengeClue.clueId, 1), "${npc.name} has given you a challenge scroll!")
                                    player.inventory.add(Item(challengeClue.clueId, 1))
                                }
                            }
                        }
                    }
                )
                return true
            }

            return false
        }
    }
}