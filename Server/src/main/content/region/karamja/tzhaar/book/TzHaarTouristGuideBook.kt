package content.region.karamja.tzhaar.book

import content.global.plugin.iface.BookInterface
import content.global.plugin.iface.BookLine
import content.global.plugin.iface.Page
import content.global.plugin.iface.PageSet
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import shared.consts.Components
import shared.consts.Items

class TzHaarTouristGuideBook : InteractionListener {

    companion object {
        private const val TITLE = "TzHaar Tourist Guide"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("Introduction", 38),
                    BookLine("", 39),
                    BookLine("So, you plan on taking a", 40),
                    BookLine("vacation to the wonderful", 41),
                    BookLine("TzHaar city, deep within the", 42),
                    BookLine("Karamja volcano? Then this", 43),
                    BookLine("book is perfect for you! You", 44),
                    BookLine("will learn some useful", 45),
                    BookLine("TzHaar phrases, some dos", 46),
                    BookLine("and don'ts among the", 47),
                    BookLine("TzHaar, how to best reach", 48),
                    BookLine("your destination, how to", 49),
                    BookLine("count in TzHaar, and about", 50),
                    BookLine("popular tourist attractions.", 51),
                ),
                Page(
                    BookLine("Index", 53),
                    BookLine(core.tools.RED + "Section 1:</col> Getting to the", 54),
                    BookLine("TzHaar City", 55),
                    BookLine(core.tools.RED + "Section 2:</col> Tourist Attractions", 56),
                    BookLine(core.tools.RED + "Section 3:</col> Dos and Don'ts", 57),
                    BookLine(core.tools.RED + "Section 4:</col> Useful Pharses", 58),
                    BookLine(core.tools.RED + "Section 5:</col> Counting in", 59),
                    BookLine("TzHaar", 60),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Section 1:", 38),
                    BookLine("Getting to the TzHaar City", 39),
                    BookLine("", 40),
                    BookLine("Finding your way to this", 41),
                    BookLine("over-tropical paradise is", 42),
                    BookLine("simple. The first thing you", 43),
                    BookLine("need to do is get to", 44),
                    BookLine("Karamja. Regular boat rides", 45),
                    BookLine("will take you directly from", 46),
                    BookLine("Port Sarim to the eastern", 47),
                    BookLine("peninsula of the island, or", 48),
                    BookLine("from East Ardougne to", 49),
                    BookLine("Brimhaven. From there, head", 50),
                    BookLine("straight for the Karamja", 51),
                    BookLine("volcano. Can't find your", 52),
                ),
                Page(
                    BookLine("way? Follow a very simple", 53),
                    BookLine("landmark: a cloud of beautiful", 54),
                    BookLine("black smoke - you can't miss", 55),
                    BookLine("it!", 56),
                    BookLine("", 57),
                    BookLine("Once you've reached the", 58),
                    BookLine("crater, a conveniently placed", 59),
                    BookLine("opening will take you into its", 60),
                    BookLine("caves. As soon as you get in,", 61),
                    BookLine("you will see the lava-", 62),
                    BookLine("encrusted tunnel entrance", 63),
                    BookLine("merely steps away. Enter", 64),
                    BookLine("and you will have reached", 65),
                    BookLine("your destination! Hours of", 66),
                    BookLine("fun with tourist attractions,", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("sight-seeing, and socialising", 38),
                    BookLine("with the locals and other", 39),
                    BookLine("tourists await you.", 40),
                    BookLine("Thousands of people from all", 41),
                    BookLine("over RuneScape visit this", 42),
                    BookLine("glorious city every day,", 43),
                    BookLine("so you will never be alone!", 44),
                    BookLine("", 45),
                    BookLine("Disclaimer: the author of", 46),
                    BookLine("this tourist guide takes no", 47),
                    BookLine("responsibility for any", 48),
                    BookLine("'mishaps' on the way to the", 49),
                    BookLine("TzHaar city, such as death", 50),
                    BookLine("by pirates or scorpions,", 51),
                    BookLine("skeletons or demons.", 52),
                ),
                Page(
                    BookLine("Section 2:", 53),
                    BookLine("Tourist Attractions", 54),
                    BookLine("", 55),
                    BookLine("Once inside the TzHaar city", 56),
                    BookLine("the attractions and sights to", 57),
                    BookLine("experience are nearly", 58),
                    BookLine("endless. Beautiful rivers flow", 59),
                    BookLine("straight through the city, and", 60),
                    BookLine("make for perfect scenery if", 61),
                    BookLine("you want to go for a walk", 62),
                    BookLine("with a special someone. You", 63),
                    BookLine("can also get a first-hand look", 64),
                    BookLine("at TzHaar craftsmen hard at", 65),
                    BookLine("work, forging wonderful tools", 66),
                    BookLine("out of purest obsidian. Or", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("why not admire the", 38),
                    BookLine("marvelous city guards?", 39),
                    BookLine("", 40),
                    BookLine("You might think, 'but what if", 41),
                    BookLine("I want to be more active?'", 42),
                    BookLine("Worry not! The TzHaar", 43),
                    BookLine("city also contains the two", 44),
                    BookLine("great attractions known as", 45),
                    BookLine("the Play Pit* and Play", 46),
                    BookLine("Cave*. In the Play Pit*,", 47),
                    BookLine("tourists from around", 48),
                    BookLine("RuneScape can compete", 49),
                    BookLine("against each other for fine", 50),
                    BookLine("prizes. The Play Cave* is a", 51),
                    BookLine("fantastic attraction where you", 52),
                ),
                Page(
                    BookLine("will be able to get up close", 53),
                    BookLine("and personal with the wildlife", 54),
                    BookLine("of the volcano, without the big", 55),
                    BookLine("annoying fences you find in", 56),
                    BookLine("places like the Ardougne Zoo.", 57),
                    BookLine("", 58),
                    BookLine("*The TzHaar names of these", 59),
                    BookLine("attractions are slightly", 60),
                    BookLine("different. Namely, Fight Cave", 61),
                    BookLine("and Fight Pit. The principal,", 62),
                    BookLine("however, remains the same!", 63),
                    BookLine("", 64),
                    BookLine("Disclaimer: The author of", 65),
                    BookLine("this tourist guide takes no", 66),
                    BookLine("responsibility for any", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("'mishaps' such as death by", 38),
                    BookLine("lava, TzHaar guards, other", 39),
                    BookLine("tourists or highly aggressive", 40),
                    BookLine("Play Cave monsters.", 41),
                ),
                Page(
                    BookLine("Section 3:", 53),
                    BookLine("Dos and Don'ts", 54),
                    BookLine("", 55),
                    BookLine("If you enter the Play Cave,", 56),
                    BookLine("do not feed the animals! (Or", 57),
                    BookLine("their taste for human flesh", 58),
                    BookLine("may grow even stronger.)", 59),
                    BookLine("", 60),
                    BookLine("If you enter the Play Pit,", 61),
                    BookLine("try to stay away from other", 62),
                    BookLine("tourists! (They are there to", 63),
                    BookLine("kill you.)", 64),
                    BookLine("", 65),
                    BookLine("If you take walks along the", 66),
                    BookLine("beautiful rivers, don't go for", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("a swim! (Lava is not good for", 38),
                    BookLine("your skin.)", 39),
                    BookLine("", 40),
                    BookLine("If you go to watch the city", 41),
                    BookLine("guards, don't insult or tease", 42),
                    BookLine("them! (Their clubs hit hard", 43),
                    BookLine("and won't stop hitting until", 44),
                    BookLine("you're dead.)", 45),
                ),
                Page(
                    BookLine("Section 4:", 53),
                    BookLine("Useful Pharses", 54),
                    BookLine("", 55),
                    BookLine("The TzHaar are usually", 56),
                    BookLine("happy to speak your own", 57),
                    BookLine("language, but sometimes it", 58),
                    BookLine("can prove useful to know", 59),
                    BookLine("parts of their language. This", 60),
                    BookLine("section of the tourist guide is", 61),
                    BookLine("here for you to quickly grasp", 62),
                    BookLine("the foundation of their", 63),
                    BookLine("beautiful language.", 64),
                    BookLine("", 65),
                    BookLine("The words are typically short", 66),
                    BookLine("and always end with a", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("consonant, even though it's", 38),
                    BookLine("not always fully pronounced.", 39),
                    BookLine("What the words mean", 40),
                    BookLine("depends on context, and", 41),
                    BookLine("don't always have a set", 42),
                    BookLine("translation.", 43),
                    BookLine("", 44),
                    BookLine("Ak - Forked, twin", 45),
                    BookLine("E - It", 46),
                    BookLine("Ek - Blade, knife", 47),
                    BookLine("Em - Hammer, mace", 48),
                    BookLine("Haar - Holy, sacred", 49),
                    BookLine("Hur - Builder, crafter,", 50),
                    BookLine("sculptor, small", 51),
                    BookLine("Im - Pickaxe", 52),
                ),
                Page(
                    BookLine("Jad - Elemental", 53),
                    BookLine("Jal - Foreign; not TzHaar", 54),
                    BookLine("Ket - Blunt, defender, guard,", 55),
                    BookLine("large", 56),
                    BookLine("Kih - Air, fly, wing", 57),
                    BookLine("Kl - Us, we", 58),
                    BookLine("Kot - Protect, save", 59),
                    BookLine("Kul - Token, value", 60),
                    BookLine("Mej - Mage, magic, priest;", 61),
                    BookLine("also loosely translated as", 62),
                    BookLine("parent", 63),
                    BookLine("Om - Hammer, maul", 64),
                    BookLine("Tal - Rod, staff", 65),
                    BookLine("Tok - Hard, material, rock", 66),
                    BookLine("Tz - Burn, fire, hot, life", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Ul - Circle, ring", 38),
                    BookLine("Xil - Dangerous, hunter,", 39),
                    BookLine("killer, sharp", 40),
                    BookLine("Yt - Cold, dead, freeze, ice", 41),
                    BookLine("Zek - Attack, harm", 42),
                    BookLine("", 43),
                    BookLine("These basic words can then", 44),
                    BookLine("be combined into more", 45),
                    BookLine("complex words. Here follows", 46),
                    BookLine("a list of examples.", 47),
                    BookLine("", 48),
                    BookLine("JalYt - Human.", 49),
                    BookLine("TokKul - TzHaar currency,", 50),
                    BookLine("cut from the purest obsidian.", 51),
                    BookLine("TzHaar - The TzHaar", 52),
                ),
                Page(
                    BookLine("species as a whole.", 53),
                    BookLine("Tz-Kih - A species of fairly", 54),
                    BookLine("common fiery bats living", 55),
                    BookLine("within the volcano.", 56),
                    BookLine("", 57),
                    BookLine("TokTz - Obsidian. All of the", 58),
                    BookLine("obsidian they use is gained", 59),
                    BookLine("from their own volcano.", 60),
                    BookLine("Sometimes they even gain", 61),
                    BookLine("obsidian out of their dead, as", 62),
                    BookLine("a way of passing their life", 63),
                    BookLine("force on to new parts of", 64),
                    BookLine("TzHaar society.", 65),
                    BookLine("", 66),
                    BookLine("Tok-Ket - A block of stone.", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("The kind typically used in", 38),
                    BookLine("TzHaar constructions are", 39),
                    BookLine("sourced from their own", 40),
                    BookLine("mines.", 41),
                    BookLine("", 42),
                    BookLine("TokJal-Hurt - A plank. The", 43),
                    BookLine("kind typically used in", 44),
                    BookLine("TzHaar constructions are", 45),
                    BookLine("oak planks, treated by the", 46),
                    BookLine("TzHaar in a specific manner", 47),
                    BookLine("to make them resistant to", 48),
                    BookLine("fire.", 49),
                    BookLine("", 50),
                    BookLine("TokYt-Hurt - Metal. The", 51),
                    BookLine("kind typically used in", 52),
                ),
                Page(
                    BookLine("TzHaar construction is", 53),
                    BookLine("mithril or adamantite, forged", 54),
                    BookLine("in their magma furnaces.", 55),
                    BookLine("", 56),
                    BookLine("TokTz-Xil-Ek - A knife made", 57),
                    BookLine("of obsidian.", 58),
                    BookLine("TokYt-Xil-Ek - A knife made", 59),
                    BookLine("of metal.", 60),
                    BookLine("Tok-Xil-Ek - A knife made of", 61),
                    BookLine("any material.", 62),
                    BookLine("", 63),
                    BookLine("TokTz-Ket-Em - A maul or", 64),
                    BookLine("club of obsidian.", 65),
                    BookLine("TokYt-Ket-Em - A maul or", 66),
                    BookLine("club of metal.", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Tok-Ket-Em - A maul or", 38),
                    BookLine("club of any material.", 39),
                    BookLine("", 40),
                    BookLine("TokTz-Xil-Im - A pickaxe of", 41),
                    BookLine("obsidian.", 42),
                    BookLine("TokYt-Xil-Im - A pickaxe of", 43),
                    BookLine("metal.", 44),
                    BookLine("Tok-Xil-Im - A pickaxe of", 45),
                    BookLine("any kind.", 46),
                    BookLine("", 47),
                    BookLine("TokTz-Ket-Om - A hammer", 48),
                    BookLine("or mace of obsidian.", 49),
                    BookLine("TokYt-Ket-Om - A hammer", 50),
                    BookLine("or mace of metal.", 51),
                    BookLine("Tok-Ket-Om - A hammer or", 52),
                ),
                Page(
                    BookLine("mace of any material.", 53),
                ),
            ),
            PageSet(
                Page(
                    BookLine("Section 5:", 38),
                    BookLine("Counting in TzHaar", 39),
                    BookLine("", 40),
                    BookLine("The TzHaar count in a", 41),
                    BookLine("slightly different way from", 42),
                    BookLine("humans. The basic for their", 43),
                    BookLine("counting system comes from", 44),
                    BookLine("their anatomy: They have", 45),
                    BookLine("four arms with three fingers", 46),
                    BookLine("on each, and this is reflected", 47),
                    BookLine("both by the base of twelve as", 48),
                    BookLine("well as how the numbers are", 49),
                    BookLine("written. The human counting", 50),
                    BookLine("system has a base of ten,", 51),
                    BookLine("which means that we count", 52),
                ),
                Page(
                    BookLine("from 0 to 9 before adding", 53),
                    BookLine("on a second digit, making 10.", 54),
                    BookLine("The TzHaar count from 1 to", 55),
                    BookLine("12 before adding on the", 56),
                    BookLine("second digit. 0 is nothing to", 57),
                    BookLine("a TzHaar, and thus not", 58),
                    BookLine("included.", 59),
                    BookLine("", 60),
                    BookLine("All TzHaar numbers are", 61),
                    BookLine("collections of lines. The", 62),
                    BookLine("numbers are based on their", 63),
                    BookLine("four arms and three fingers", 64),
                    BookLine("on each arm, and what is", 65),
                    BookLine("necessary in order to count", 66),
                    BookLine("to a certain number is what", 67),
                ),
            ),
            PageSet(
                Page(
                    BookLine("the lines represent. For", 38),
                    BookLine("every hand that a TzHaar", 39),
                    BookLine("needs to use, one horizontal", 40),
                    BookLine("line is added. For every", 41),
                    BookLine("finger on the last hand that", 42),
                    BookLine("needs to be used, one vertical", 43),
                    BookLine("line is added. However, if all", 44),
                    BookLine("four hands are necessary,", 45),
                    BookLine("they make an X shape", 46),
                    BookLine("instead of four horizontal", 47),
                    BookLine("lines.", 48),
                    BookLine("", 49),
                    BookLine("For an example, number 5", 50),
                    BookLine("in TzHaar would need two", 51),
                    BookLine("hands and two fingers on the", 52),
                ),
                Page(
                    BookLine("last hand. So this number", 53),
                    BookLine("would be written with two", 54),
                    BookLine("horizontal and two vertical", 55),
                    BookLine("lines.", 56),
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_2_27, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.TZHAAR_TOURIST_GUIDE_13244, IntType.ITEM, "read") { player, _ ->
            openDialogue(player, TouristGuideDialogue())
            return@on true
        }
    }

    inner class TouristGuideDialogue : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendDialogueLines(player!!, "You open a book and see that there is note stuck to the inside of", "the cover.")
                    stage = 1
                }
                1 -> {
                    setTitle(player!!, 2)
                    options("Read the book.", "Read the note.")
                    stage = 2
                }
                2 -> {
                    when(buttonID) {
                        1 -> end().also { BookInterface.openBook(player!!, BookInterface.FANCY_BOOK_2_27, ::display) }
                        2 -> end().also { openInterface(player!!, Components.TZHAAR_NUMBERS_739) }
                    }
                }
            }
        }
    }
}