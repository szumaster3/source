package content.region.kandarin.gnome.quest.makinghistory.plugin

import content.region.kandarin.gnome.quest.makinghistory.MHUtils
import content.region.kandarin.gnome.quest.makinghistory.book.HistoryoftheOutpost
import content.region.kandarin.gnome.quest.makinghistory.book.TheMysteriousAdventurer
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Items
import shared.consts.Scenery
import shared.consts.Sounds

class MakingHistoryPlugin : InteractionListener {

    override fun defineListeners() {
        on(Items.SCROLL_6758, IntType.ITEM, "read") { player, _ ->
            val outpostScroll =
                arrayOf(
                    "",
                    "<col=8A0808>Timeline of the Ardougne Outpost</col>",
                    "",
                    "",
                    "Start of Fifth age: Outpost built",
                    "",
                    "+ 65 Years: The dreaded Years of Tragedy'",
                    "",
                    "+ 68 Years: The Great Battle'",
                    "",
                    "+ 71 Years: Survivors of battle start a new line of",
                    "",
                    "kings of Ardougne and the Equal Trade Market.",
                    "",
                )
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, outpostScroll.joinToString("<br>"), Components.BLANK_SCROLL_222, 2)
            return@on true
        }

        on(Scenery.SHIELD_DISPLAY_10267, IntType.SCENERY, "study") { player, _ ->
            sendDialogue(player, "Inside the case is a giant shield, the plate reads: 'This shield comes from the Fourth age when people were fighting to become more settled and less nomadic'.")
            return@on true
        }

        onDig(Location(2440, 3145, 0)) { player ->
            if (inInventory(player, Items.ENCHANTED_KEY_6754) && getVarbit(player, MHUtils.ERIN_PROGRESS) >= 1) {
                sendDialogue(player, "You use the spade and find a chest. Wonder what's inside?")
                addItemOrDrop(player, Items.CHEST_6759)
                setVarbit(player, MHUtils.ERIN_PROGRESS, 2, true)
            }
            return@onDig
        }

        onUseWith(IntType.ITEM, Items.ENCHANTED_KEY_6754, Items.CHEST_6759) { player, _, _ ->
            if (removeItem(player, Item(Items.CHEST_6759))) {
                sendDialogueLines(player, "You look in the chest and find a journal, and then you throw away", "the chest.")
                addItemOrDrop(player, Items.JOURNAL_6755)
                setVarbit(player, MHUtils.ERIN_PROGRESS, 4, true)
                setAttribute(player, MHUtils.ATTRIBUTE_ERIN_PROGRESS, true)
            }
            return@onUseWith true
        }

        on(Items.ENCHANTED_KEY_6754, IntType.ITEM, "feel") { player, _ ->
            if (inBorders(player, 2438, 3143, 2442, 3147)) {
                sendMessage(player, "The key is steaming. It must be right below your feet.")
                playAudio(player, Sounds.HISTORY_KEY_STEAMING_1201)
                return@on true
            }

            val keyHints = listOf(
                getRegionBorders(10574) to ("It's very cold" to Sounds.HISTORY_KEY_COLD_1198),
                getRegionBorders(10546) to ("It's cold" to Sounds.HISTORY_KEY_COLD_1198),
                getRegionBorders(10290) to ("It's warm" to Sounds.HISTORY_KEY_WARM_1202),
                getRegionBorders(10289) to ("It's hot" to Sounds.HISTORY_KEY_HOT_1200),
                getRegionBorders(9776) to ("It's very hot" to Sounds.HISTORY_KEY_HOT_1200),
                getRegionBorders(9777) to ("Ouch! It's burning hot and warmer than last time" to Sounds.HISTORY_KEY_BURNING_1197),
            )

            val match = keyHints.firstOrNull { (region, _) -> inBorders(player, region) }

            if (match != null) {
                val (message, sound) = match.second
                sendMessage(player, message)
                playAudio(player, sound)
            } else {
                sendMessage(player, "It's freezing")
                playAudio(player, Sounds.HISTORY_KEY_FREEZING_1199)
            }

            return@on true
        }

        on(Scenery.BOOKCASE_10273, IntType.SCENERY, "study") { player, _ ->
            sendDialogueOptions(player, "There's a great variety of books. Which shall you choose?", "The History of the Outpost.", "The Times of Lathas.", "The Mysterious Adventurer.")
            addDialogueAction(player) { p, button ->
                closeDialogue(player)
                when (button) {
                    1 -> HistoryoftheOutpost.openBook(p)
                    2 -> openDialogue(player, TimeOfLathasBookcaseDialogue())
                    3 -> TheMysteriousAdventurer.openBook(p)
                }
                player.dialogueInterpreter.actions.clear()
            }
            return@on true
        }
    }

    inner class TimeOfLathasBookcaseDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> sendDialogue(player!!, "You pick up a new looking book: 'The Times of Lathas'.").also { stage++ }
                1 -> sendDialogue(player!!, "You skim over the heavy book. It talks about the heritage of the line of kings who carry the name Ardignas.").also { stage++ }
                2 -> sendDialogue(player!!, "They only came into power 68 years ago, but in which time there have been five kings, the current being King Lathas.").also { stage++ }
                3 -> end()
            }
        }
    }
}
