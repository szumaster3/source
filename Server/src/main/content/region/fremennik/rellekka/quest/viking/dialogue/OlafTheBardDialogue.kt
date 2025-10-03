package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Olaf The Bard dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class OlafTheBardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        when {
            inInventory(player, Items.STURDY_BOOTS_3700, 1) -> {
                playerl(FaceAnim.HAPPY, "Hello Olaf. Do you have a beautiful love song written for me?")
                stage = 65
            }

            inInventory(player, Items.FREMENNIK_BALLAD_3699, 1) -> {
                playerl(FaceAnim.ASKING, "So you think this song is pretty good then?")
                stage = 70
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false) -> {
                playerl(FaceAnim.ASKING, "I've got a trade item; is it for you?")
                stage = 75
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 3 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find some custom sturdy boots, do you?")
                stage = 60
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 2 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a love ballad, do you?")
                stage = 50
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_OLAF_CONCERT, false) -> {
                playerl(FaceAnim.HAPPY, "So can I rely on your vote with the council of elders in my favour?")
                stage = 40
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_OLAF_START, false) -> {
                npc(FaceAnim.HAPPY, "I can't wait to see your performance.")
                stage = 1000
            }

            isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                npcl(FaceAnim.HAPPY, "Hello again to you, ${FremennikTrials.getFremennikName(player)}. Us bards should stick together, what can I do for you?")
                stage = 98
            }

            player.questRepository.hasStarted(Quests.THE_FREMENNIK_TRIALS) -> {
                npc(FaceAnim.HALF_ASKING, "Hello? Yes? You want something outerlander?")
            }

            else -> {
                playerl(FaceAnim.HAPPY, "Hello there. So you're a bard?")
                stage = 150
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Are you a member of the council?").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY,"Why, indeed I am, outerlander! My talents as an", "exemplary musician made it difficult for them not to", "accept me! Why do you wish to know this?").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "Well, I ask because I am currently doing the", "Fremennik trials so as to join you clan. I need seven", "of the twelve council of elders to vote for me.").also { stage++ }
            3 -> npc(FaceAnim.HALF_ASKING,"Ahhh... and you wish to earn my vote? I will gladly", "accept you as a Fremennik should you be able to prove", "yourself to have a little musical ability!").also { stage++ }
            4 -> player(FaceAnim.HALF_ASKING,"So how would I do that?").also { stage++ }
            5 -> npc(FaceAnim.ASKING,"Why, by playing in our longhall! All you need to do is", "impress the revellers there with a few verses of an epic", "of your own creation!").also { stage++ }
            6 -> npc(FaceAnim.HALF_ASKING, "So what say you outerlander? Are you up for the", "challenge?").also { stage++ }
            7 -> options("Yes", "No").also { stage++ }
            8 -> when (buttonId) {
                1 -> player(FaceAnim.HAPPY, "Sure! This certainly sounds pretty easy to accomplish.", "I'll have your vote in no time!").also { stage++ }
                2 -> player(FaceAnim.NEUTRAL, "No, that sounds like too much work.").also { stage = 1000 }
            }
            9 -> npc(FaceAnim.HAPPY,"That is great news outerlander! We always need more", "music lovers here!").also {
                stage++
                setAttribute(player, GameAttributes.QUEST_VIKING_OLAF_START, true)
            }
            10 -> player(FaceAnim.HALF_ASKING,"So how would I go about writing this epic?").also { stage++ }
            11 -> npc(FaceAnim.HALF_THINKING,"Well, first of all you are going to need an instrument.", "Like all true bards you are going to have to make this", "yourself.").also { stage++ }
            12 -> player(FaceAnim.HALF_ASKING,"How do I make an instrument?").also { stage++ }
            13 -> npc(FaceAnim.HALF_THINKING,"Well, it is a long and drawn-out process. Just east of", "this village there is an unusually musical tree that can", "be used to make very high quality instruments.").also { stage++ }
            14 -> npc(FaceAnim.NEUTRAL,"Cut a piece from it, and then carve it into a special", "shape that will allow you to string it. Using a knife as", "you would craft any other wooden object would be best", "for this.").also { stage++ }
            15 -> player(FaceAnim.HALF_ASKING,"Then what do I need to do?").also { stage++ }
            16 -> npc(FaceAnim.FRIENDLY,"Next you will need to string your lyre. There is a troll", "to the South-east who has some golden wool. I would", "not recommend using anything else to string your lyre", "with.").also { stage++ }
            17 -> player(FaceAnim.HALF_ASKING,"Anything else?").also { stage++ }
            18 -> npc(FaceAnim.HALF_THINKING,"Well, when you have crafted your lyre you will need", "the blessing of the Fossegrimen to tune your lyre to", "perfection before you even consider a public", "performance.").also { stage++ }
            19 -> player(FaceAnim.HALF_ASKING,"Who or what is Fossegrimen?").also { stage++ }
            20 -> npc(FaceAnim.FRIENDLY,"Fossegrimen is a lake spirit that lives just a little way", "South-west of this village. Make her an offering of fish,", "and you will then be ready for your performance.").also { stage++ }
            21 -> npc(FaceAnim.FRIENDLY,"Make sure you give her a suitable offering however. If", "the offering is found to be unworthy, then you may", "find yourself unable to play your lyre with any skill at", "all!").also { stage++ }
            22 -> player(FaceAnim.HALF_ASKING,"So what would be a worthy offering?").also { stage++ }
            23 -> npc(FaceAnim.FRIENDLY,"A raw shark, manta ray, or sea turtle should be", "sufficient as an offering.").also { stage++ }
            24 -> player(FaceAnim.HALF_ASKING,"Ok, what else do I need to do?").also { stage++ }
            25 -> npc(FaceAnim.FRIENDLY,"When you have crafted your lyre and been blessed by", "Fossegrimen, then you will finally be ready to make", "your performance to the revellers at the longhall.").also { stage++ }
            26 -> npc(FaceAnim.FRIENDLY,"Head past the bouncers and onto the stage, and then", "begin to play. If all goes well, you should find the music", "spring to your mind and sing your own epic on the", "spot").also { stage++ }
            27 -> npc(FaceAnim.FRIENDLY,"I will observe both you and the audience, and if you", "show enough talent, I will happily vote in your favour at", "the council of elders.").also { stage++ }
            28 -> npc(FaceAnim.HALF_ASKING, "Is that clear enough, outerlander? Would you like me", "to repeat anything?").also { stage++ }
            29 -> player("No thanks. I think I've got it.").also { stage = 1000 }
            40 -> npcl(FaceAnim.HAPPY, "You have a truly poetic soul! Anyone who can compose such a beautiful epic, and then perform it so flawlessly can only bring good to our clan!").also { stage++ }
            41 -> playerl(FaceAnim.THINKING, "Erm... so that's a yes, then?").also { stage++ }
            42 -> npcl(FaceAnim.HAPPY, "Absolutely! We must collaborate together on a duet sometime, don't you think?").also {
                setAttribute(player, GameAttributes.QUEST_VIKING_OLAF_VOTE, true)
                setAttribute(player, GameAttributes.QUEST_VIKING_VOTES, getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) + 1)
                stage = 1000
            }
            50 -> npcl(FaceAnim.HAPPY, "Well, as official Fremennik bard, it falls within my remit to compose all music for the tribe. I am fully versed in all the various types of romantic music.").also { stage++ }
            51 -> playerl(FaceAnim.HAPPY, "Great! Can you write me one then?").also { stage++ }
            52 -> npcl(FaceAnim.THINKING, "Well... Normally I would be thrilled at the chance to show my skill as a poet in composing a seductively romantic ballad...").also { stage++ }
            53 -> playerl(FaceAnim.ANNOYED, "Let me guess; Here comes the 'but'.").also { stage++ }
            54 -> npcl(FaceAnim.SAD, "...but unfortunately I cannot concentrate fully upon my work recently.").also { stage++ }
            55 -> playerl(FaceAnim.ASKING, "Why is that then?").also { stage++ }
            56 -> npcl(FaceAnim.NEUTRAL, "It is these old worn out shoes of mine... As a bard I am expected to wander the lands, singing of the glorious battles of our warriors.").also { stage++ }
            57 -> npcl(FaceAnim.NEUTRAL, "If you can find me a pair of sturdy boots to replace these old worn out ones of mine, I will be happy to spend the time on composing you a romantic ballad.").also {
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = 1000
            }
            60 -> npcl(FaceAnim.ANNOYED, "I'm sorry outerlander... If I did, I would not trouble you to go and find them for me, would I?").also { stage = 1000 }
            65 -> npcl(FaceAnim.HAPPY, "That depends outerlander... Do you have some new boots for me? My feet get so tired roaming the land...").also { stage++ }
            66 -> playerl(FaceAnim.HAPPY, "As a matter of fact - I do!").also {
                removeItem(player, Items.STURDY_BOOTS_3700)
                addItemOrDrop(player, Items.FREMENNIK_BALLAD_3699, 1)
                stage++
            }
            67 -> npcl(FaceAnim.HAPPY, "Oh! Superb! Those are great! They're just what I was looking for! Here, take this song with my compliments! It is one of my finest works yet!").also { stage = 1000 }
            70 -> npcl(FaceAnim.HAPPY, "Ahhh.... outerlander... it is the most beautiful romantic ballad I have ever been inspired to write...").also { stage++ }
            71 -> npcl(FaceAnim.HAPPY, "Only a woman with a heart of icy stone could fail be to be moved by its beauty!").also { stage++ }
            72 -> playerl(FaceAnim.HAPPY, "Thanks! That sounds perfect!").also { stage = 1000 }
            75 -> npcl(FaceAnim.HAPPY, "Only if it's a new pair of boots.").also { stage = 1000 }
            98 -> options("I was wondering...", "I forget now...").also { stage++ }
            99 -> when (buttonId) {
                1 -> playerl(FaceAnim.ASKING, "I was wondering... Can I learn to play my lyre again?").also { stage++ }
                2 -> playerl(FaceAnim.THINKING, "I forget now...").also { stage = 1000 }
            }
            100 -> npcl(FaceAnim.HAPPY, "Well that is an interesting question. Let me let you into a little secret. If you make another offering to the Fossegrimen you will learn a secret melody.").also { stage++ }
            101 -> playerl(FaceAnim.ASKING, "What kind of melody?").also { stage++ }
            102 -> npcl(FaceAnim.HAPPY, "It is the song of Rellekka. When you play it, it will bring you back to this town where you are in this world.").also { stage++ }
            103 -> npcl(FaceAnim.HAPPY, "We often go adventuring with bards for precisely this reason. No matter where we have ended up we can return safe and sound to home.").also { stage++ }
            104 -> playerl(FaceAnim.HAPPY, "Thanks, Olaf!").also { stage = 1000 }
            150 -> npcl(FaceAnim.HAPPY, "I am afraid I cannot speak to outlanders. Besides, I am busy composing an epic.").also { stage = 1000 }
            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OLAF_THE_BARD_1269)
}
