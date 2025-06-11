package content.region.kandarin.handlers.ardougne

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class NecromancerTowerListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles searching the bookcase at Necromaner Tower.
         */

        on(Scenery.BOOKCASE_6894, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.NECROMANCY_BOOK_4837)) {
                sendMessage(player, "You search the bookcase...")
                sendMessageWithDelay(player, "You find nothing of interest to you.", 1)
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }

            sendMessage(player, "...and find a book named 'On loan to the wizards' Guild...to be returned'.")
            addItem(player, Items.NECROMANCY_BOOK_4837)
            return@on true
        }

        /*
         * Handles using the Torn pages on Irwin NPCs.
         */

        onUseWith(IntType.NPC, Items.TORN_PAGE_4809, NPCs.IRWIN_FEASELBAUM_2066) { player, _, node ->
            val npc = node.asNpc()
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                dialogue(player) {
                    player(FaceAnim.HALF_ASKING, "I found this torn page, do you know anything about it?")
                    npc(npc, "Hey...I hope you didn't rip that page out of one of those books, they'll be trouble if it was.")
                    player("No, it wasn't actually, I got it from somewhere else, do you recognise it?")
                    npc(npc, "Hmm, well it certainly looks like text from one of the books. Have you checked at the wizards' guild in Yanille? We sometime loan books to them.")
                }
            } else {
                sendMessage(player, "He look a bit busy at the moment.")
            }
            return@onUseWith true
        }

        /*
         * Handles using the Necromancy book on Irwin NPCs.
         */

        onUseWith(IntType.NPC, Items.NECROMANCY_BOOK_4837, NPCs.IRWIN_FEASELBAUM_2066) { player, _, node ->
            val npc = node.asNpc()
            if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) > 1) {
                dialogue(player) {
                    player(FaceAnim.HALF_ASKING, "Hi, do you know anything about this book?")
                    npc(npc, "Hey...that book has been loaned to the wizards' guild in Yanille, I hope you're not returning it in a damaged condition!")
                    player("Well, I was just wondering where the book came from actually and try to get some more information about it.")
                    npc(npc, "Well, that book looks as if it's been through the wars...you're not going to return it in that condition. You take it back to the wizards at Yanille, they borrowed it, so it's their responsibility.")
                }
            } else {
                sendMessage(player, "He look a bit busy at the moment.")
            }
            return@onUseWith true
        }
    }
}
