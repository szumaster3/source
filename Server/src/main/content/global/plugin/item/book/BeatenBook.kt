package content.global.plugin.item.book

import content.global.plugin.iface.BookInterface
import content.global.plugin.iface.BookLine
import content.global.plugin.iface.Page
import content.global.plugin.iface.PageSet
import content.region.kandarin.seers.quest.elemental_quest_2.plugin.EW2Utils
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import shared.consts.Items
import shared.consts.Quests

class BeatenBook : InteractionListener {
    /*
     * Begins the Elemental Workshop II quest.
     * It can be found in the Varrock Dig Site's
     * library south-east of Varrock.
     */

    companion object {
        private const val TITLE = "Book of the Elemental Helm"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("In this book I will try", 55),
                        BookLine("to cover all the explosive", 56),
                        BookLine("reactions I witnessed on", 57),
                        BookLine("my travels. Let us start", 58),
                        BookLine("with one of the more simple", 59),
                        BookLine("reactions, one I'm sure", 60),
                        BookLine("you have seen yourself.", 61),
                        BookLine("When mining, certain gasses", 62),
                        BookLine("are released. Once these", 63),
                        BookLine("gasses are mixed with the", 64),
                        BookLine("air it only takes a small", 65),
                    ),
                    Page(
                        BookLine("spark to ignite them.", 66),
                        BookLine("Unfortunately the result", 67),
                        BookLine("is uncontrollable. In", 68),
                        BookLine("the far east I found a", 69),
                        BookLine("recipe for a mix they called", 70),
                        BookLine("fire oil. This fire oil", 71),
                        BookLine("has an explosive capability", 72),
                        BookLine("but also it sticks to anything", 73),
                        BookLine("it touches and burns.", 74),
                        BookLine("Ingredients. Quick Lime.", 75),
                        BookLine("This is a highly", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("caustic compound and is", 55),
                        BookLine("created by the endothermic", 56),
                        BookLine("reaction of limestone.", 57),
                        BookLine("Brimstone. Known as", 58),
                        BookLine("yellow crystals. odourless,", 59),
                        BookLine("insoluble and brittle as", 60),
                        BookLine("a solid. But as gas quite", 61),
                        BookLine("toxic, preventing respiration.", 62),
                        BookLine("Naphtha. A colourless,", 63),
                        BookLine("flammable solvent.", 64),
                        BookLine("One of the more volatile", 65),
                    ),
                    Page(
                        BookLine("fractions of the fractional", 66),
                        BookLine("distillation of coal tar.", 67),
                        BookLine("Preparation. Great care must", 68),
                        BookLine("be taken when mixing", 69),
                        BookLine("this. The brimstone,", 70),
                        BookLine("naphtha and quicklime may", 71),
                        BookLine("be mixed at any time, but", 72),
                        BookLine("only light it at the last", 73),
                        BookLine("second. The tricky thing", 74),
                        BookLine("is a delivery method. A", 75),
                        BookLine("naked flame is required", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to ignite the concoction,", 55),
                        BookLine("the problem being that", 56),
                        BookLine("the resulting explosion", 57),
                        BookLine("will cover the area in", 58),
                        BookLine("hot sticky fire. The last", 59),
                        BookLine("bang in my list comes from", 60),
                        BookLine("a secret recipe digging", 61),
                        BookLine("compound based", 62),
                        BookLine("on nitroglycerine.", 63),
                        BookLine("I have seen the results", 64),
                        BookLine("but could not discover", 65),
                    ),
                    Page(
                        BookLine("the ingredients. I'm told", 66),
                        BookLine("that the secret of this", 67),
                        BookLine("dig compound can be found", 68),
                        BookLine("out by becoming", 69),
                        BookLine("an archaeologist.", 70),
                    ),
                ),
            )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(
            player,
            BookInterface.FANCY_BOOK_3_49,
            TITLE,
            CONTENTS,
        )
        return true
    }

    override fun defineListeners() {
        on(Items.BEATEN_BOOK_9717, IntType.ITEM, "read") { player, _ ->
            if (getAttribute(player, EW2Utils.foundBook, 0) == 1) {
                addItemOrDrop(player, Items.SCROLL_9721)
                sendMessage(player, "The book has two parts; an introduction and an instruction section.")
                sendMessage(player, "You flip the book open to the introduction and start reading.")
                player.dialogueInterpreter.sendItemMessage(
                    Items.SCROLL_9721,
                    "You find a slip of paper that has been used for a",
                    "bookmark. You put it in your pack to study later.",
                )
                setAttribute(player, EW2Utils.foundBook, 2)
                setVarbit(player, EW2Utils.EW2_KEY_VARBIT, 3, true)
                runTask(player, 2) {
                    BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
                    setQuestStage(player, Quests.ELEMENTAL_WORKSHOP_II, 2)
                }
            } else {
                BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            }
            return@on true
        }
    }
}
