package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.finishQuest
import core.api.isQuestComplete
import core.api.startQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import kotlin.random.Random

/**
 * Represents the Chieftan Brundt dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class ChieftanBrundtDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        when {
            inInventory(player, Items.TRACKING_MAP_3701, 1) -> {
                playerl(FaceAnim.HAPPY, "I got Sigli's hunting map for you.")
                stage = 515
            }

            inInventory(player, Items.FISCAL_STATEMENT_3708, 1) -> {
                playerl(FaceAnim.ASKING, "So cutting sales tax isn't going to ruin your economy here or anything?")
                stage = 520
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false) -> {
                playerl(FaceAnim.ASKING, "I've got this trade item. Is it for you?")
                stage = 525
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 5 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a map to unspoiled hunting grounds, do you?")
                stage = 510
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 4 -> {
                playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a guarantee of a reduction on sales taxes, do you?")
                stage = 500
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) >= 7 -> {
                npcl(FaceAnim.HAPPY, "Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
                stage = 545
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) in 3..6 -> {
                npcl(FaceAnim.HAPPY, "Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
                stage = 540
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) == 1 -> {
                npcl(FaceAnim.HAPPY, "Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
                stage = 535
            }

            getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, -1) == 0 -> {
                npcl(FaceAnim.HAPPY, "Greetings again outerlander! How goes your attempts to gain votes with the council of elders?")
                stage = 530
            }

            isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                npcl(FaceAnim.HAPPY, "Hello again, ${if (player?.isMale == true) "brother" else "sister"} ${FremennikTrials.getFremennikName(player!!)}. I hope your travels have brought you wealth and joy! What compels you to visit me on this day?")
                stage = 600
            }

            else -> npc("Greetings outlander!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("What is this place?", "Why will no-one talk to me?", "Do you have any quests?", "Nice hat!").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.HAPPY, "What is this place?").also { stage++ }
                2 -> playerl(FaceAnim.ASKING, "Why will no-one talk to me?").also { stage = 5 }
                3 -> player("Do you have any quests?").also { stage = 300 }
                4 -> playerl(FaceAnim.HAPPY, "Nice hat!").also { stage = 15 }
            }
            2 -> npcl(FaceAnim.HAPPY, "This place? Why, this is Relleka! Homeland of all Fremennik! I do not recognise your face outerlander; Where do you come from?").also { stage++ }
            3 -> playerl(FaceAnim.HAPPY, "Hmmm... I will not press the issue then outerlander. How may my tribe and I help you?").also { stage = 0 }
            5 -> npcl(FaceAnim.HAPPY, "Do not take it personally, outerlander! We are a simple people, and it is our experience that keeping ourselves to ourselves is best.").also { stage++ }
            6 -> npcl(FaceAnim.HAPPY, "This is why speaking to outlanders is forbidden.").also { stage++ }
            7 -> npcl(FaceAnim.HAPPY, "We do not wish to enter war with the outlanders and their strange magics, so we limit all unauthorised communication.").also { stage++ }
            8 -> playerl(FaceAnim.ASKING, "Then how come you're talking to me?").also { stage++ }
            9 -> npcl(FaceAnim.HAPPY, "Ah, this is because I am the chieftan. I am the one who authorised contact. You will not find many of my tribe so forthcoming with you, as I.").also { stage++ }
            10 -> playerl(FaceAnim.ASKING, "Is there a way for you to authorise your tribe to talk to me then?").also { stage++ }
            11 -> npcl(FaceAnim.HAPPY, "Well, there is one way... but I doubt it is of any interest to you.").also { stage = 0 }
            15 -> npcl(FaceAnim.ANNOYED, "Don't mock me outerlander; this helm has saved my life more times than I care to recall right now.").also { stage = END_DIALOGUE }
            300 -> npc("Quests, you say outlander? Well, I would not call it a", "quest as such, but if you are brave of heart and strong", "of body, perhaps...").also { stage++ }
            301 -> npc("No, you would not be interested. Forget I said", "anything, outerlander.").also { stage++ }
            302 -> options("Yes, I am interested.", "No, I'm not interested.").also { stage++ }
            303 -> when (buttonId) {
                1 -> player("Actually, I would be very interested to hear what you", "have to offer.").also { stage = 310 }
                2 -> player("No, I'm not interested.").also { stage = END_DIALOGUE }
            }
            310 -> npc("You would? These are unusual sentiments to hear from", "an outerlander! My suggestion was going to be that if", "you crave adventure and battle,").also { stage++ }
            311 -> npc("and your heart sings for glory, then perhaps you would", "be interested in joining our clan, and becoming a", "Fremennik yourself?").also { stage++ }
            312 -> player("What would that involve exactly?").also { stage++ }
            313 -> npc("Well, there are two ways to become a member of our", "clan and call yourself a Fremennik: be born a", "Fremennik, or be voted in by our council of elders.").also { stage++ }
            314 -> player("Well, I think I've missed the first way, but how can I", "get the council of elders to vote to let me join your", "clan?").also { stage++ }
            315 -> npc("Well, that I cannot answer myself. You will need to", "speak to each of them and see what they require of you", "as proof of your dedication.").also { stage++ }
            316 -> npc("There are twelve council members around this village;", "you will need to gain a majority vote of at least seven", "councillors in your favor.").also { stage++ }
            317 -> npc("So, what say you? Give me the word and I will tell all", "of my tribe of your intentions, be they yes or nay.").also { stage++ }
            318 -> options("I want to become a Fremennik!", "I don't want to become a Fremennik.").also { stage++ }
            319 -> when (buttonId) {
                1 -> player("I think I would enjoy the challenge of becoming an", "honorary Fremennik. Where and how do I start?").also { stage = 320 }
                2 -> player("That sounds too complicated to me.").also { stage = 322 }
            }
            320 -> npc("As I say outerlander, you must find and speak to the", "twelve members of the council of elders, and see what", "tasks they might set you.").also { stage++ }
            321 -> npc("If you can gain the support of seven of the twelve, then", "you will be accepted as one of us without question.").also { startQuest(player, Quests.THE_FREMENNIK_TRIALS); stage = END_DIALOGUE }
            322 -> npc("Well, that's what I expect from an outerlander.").also { stage = END_DIALOGUE }

            500 -> npcl(FaceAnim.THINKING, "A reduction on sales taxes? Why, I am the only one in the Fremennik who may authorise such a thing. What does an outerlander want with that?").also { stage++ }
            501 -> playerl(FaceAnim.HAPPY, "Actually, it's not for me. I need to get it as part of my trials").also { stage++ }
            502 -> npcl(FaceAnim.THINKING, "Hmmm. Interesting. Your trials seem to be very different to those I took as a young lad.").also { stage++ }
            503 -> npcl(FaceAnim.NEUTRAL, "Well, I am not adverse in principle to giving a slight tax break to our shops.").also { stage++ }
            504 -> npcl(FaceAnim.THINKING, "There will of course be a shortfall in the tribe's income, that will need to be made up for elsewhere, however.").also { stage++ }
            505 -> npcl(FaceAnim.ASKING, "How about this. For many years Sigli has been the only one in the tribe who knows the locations of the best hunting grounds where game is easiest to catch.").also { stage++ }
            506 -> npcl(FaceAnim.ASKING, "If you can persuade him to let the entire tribe know these hunting grounds, then we can increase productivity within the tribe, and any shortfall caused by lowering sales taxes will be covered.").also { stage++ }
            507 -> npcl(FaceAnim.HAPPY, "I think this is a more than fair arrangement to make, dont you?").also { stage++ }
            508 -> playerl(FaceAnim.HAPPY, "Yeah, that sounds very fair.").also { stage++ }
            509 -> npcl(FaceAnim.HAPPY, "Speak to Sigli then, and you may have my promise to reduce our sales taxes. And best of luck with the rest of your trials.").also {
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = END_DIALOGUE
            }
            510 -> npcl(FaceAnim.ANNOYED, "Ah, outerlander... if you wish to become a Fremennik you should try and pay more attention to what people tell you... ").also { stage++ }
            511 -> npcl(FaceAnim.ANNOYED, "Sigli the hunter is the only one who knows of such hunting grounds. Go and request his knowledge.").also { stage = END_DIALOGUE }
            515 -> npcl(FaceAnim.HAPPY, "Excellent work outerlander! And so quickly, too! Here, you may take my financial report promising reduced sales taxes on all goods.").also {
                removeItem(player, Items.TRACKING_MAP_3701)
                addItemOrDrop(player, Items.FISCAL_STATEMENT_3708, 1)
                stage = END_DIALOGUE
            }
            520 -> npcl(FaceAnim.HAPPY, "Not at all outerlander; now that we have Sigli's map we can increase the amount of hunts we run, and make up any shortfall that way.").also { stage = END_DIALOGUE }
            525 -> npcl(FaceAnim.ANNOYED, "Not unless it's a map of the hunting grounds.").also { stage = END_DIALOGUE }
            530 -> playerl(FaceAnim.HAPPY, "I don't have any votes yet.").also { stage++ }
            531 -> npcl(FaceAnim.HAPPY, "Go and speak to the twelve members of the council of elders who live in this village. I am sure at least a few will be prepared to vote for you.").also { stage = 550 }
            535 -> playerl(FaceAnim.HAPPY, "I only have one vote so far.").also { stage++ }
            536 -> npcl(FaceAnim.HAPPY, "Hmmm... well that is certainly a good start I would say. Keep up the good work!").also { stage++ }
            537 -> npcl(FaceAnim.HAPPY, "Remember: You need to get at least seven council votes to be accepted as a member of the Fremennik.").also { stage = 550 }
            540 -> playerl(FaceAnim.HAPPY, "I only have ${getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0)} votes so far.").also { stage++ }
            541 -> npcl(FaceAnim.HAPPY, "Hmmm... you are doing very well so far, outerlander. Keep up the good work!").also { stage = 537 }
            545 -> playerl(FaceAnim.HAPPY, "I have seven members of the council prepared to vote in my favour now!").also { stage++ }
            546 -> npcl(FaceAnim.HAPPY, "I know outerlander, for I have been closely monitoring your progress so far!").also { stage++ }
            547 -> npcl(FaceAnim.HAPPY, "Then let us put the formality aside, and let me personally welcome you into the Fremennik! May you bring us honour!").also {
                setAttribute(player, "/save:fremennikname", generateFremennikName())
                stage = 560
            }
            548 -> sendDialogue("You require 10 free spaces in your backpack to claim your reward.").also { stage = END_DIALOGUE }
            550 -> npcl(FaceAnim.HAPPY, "If you need any help with your trials, I suggest you speak to Askeladden. He is currently doing his own trials of manhood to become a true Fremennik.").also { stage = END_DIALOGUE }
            560 -> npcl(FaceAnim.HAPPY, "From this day onward, you are outerlander no more! In honour of your acceptance into the Fremennik, you gain a new name: ${FremennikTrials.getFremennikName(player)}.",).also {
                cleanupAttributes(player)
                finishQuest(player, Quests.THE_FREMENNIK_TRIALS)
                stage = END_DIALOGUE
            }
            600 -> options("I just came to say hello.", "Do you have any quests?", "Nice hat!", "Can I hear the history of your people?", "Can I have a seal of passage?").also { stage++ }
            601 -> when (buttonId) {
                1 -> playerl(FaceAnim.HAPPY, "I just came by to say hello.").also { stage++ }
                2 -> playerl(FaceAnim.ASKING, "Do you have any quests?").also { stage = 605 }
                3 -> playerl(FaceAnim.HAPPY, "Nice hat!").also { stage = 610 }
                4 -> playerl(FaceAnim.ASKING, "Can I hear the history of your people?").also { stage = 615 }
                5 -> playerl(FaceAnim.HAPPY, "Can I have a seal of passage?").also { stage = 1200 }
            }
            602 -> npcl(FaceAnim.HAPPY, "Well met, ${if (player?.isMale == true) "brother" else "sister"} ${FremennikTrials.getFremennikName(player!!)}. it is always good to see your face in glorious Rellekka once more!",).also { stage = END_DIALOGUE }
            605 -> npcl(FaceAnim.HAPPY, "Not at the moment, ${FremennikTrials.getFremennikName(player!!)}. Rest assured, should your services to the Fremennik be required, I will call upon you").also { stage = END_DIALOGUE }
            610 -> npcl(FaceAnim.HAPPY, "Aye that it is, ${if (player?.isMale == true) "brother" else "sister"} ${FremennikTrials.getFremennikName(player!!)}. Should you desire one of your own, you should go to Skulgrimen's shop and see what they have available!",).also { stage = END_DIALOGUE }
            615 -> npcl(FaceAnim.HAPPY, "Why, of course, ${FremennikTrials.getFremennikName(player!!)}! Do not say 'your people' like that, for you are now a Fremennik yourself! What did you want to hear of?").also { stage++ }
            616 -> options("Tell me of the finding of Koschei", "Tell me of the lands to the North", "Tell me of the outlanders", "Don't tell me anything").also { stage++ }
            617 -> when (buttonId) {
                1 -> playerl(FaceAnim.HAPPY, "I'm interested in finding out about that mysterious warrior I fought as part of Thorvalds' combat trial. His name is Koschei I believe.").also { stage++ }
                2 -> playerl(FaceAnim.HAPPY, "I would like to hear of the lands across the ocean to the North.").also { stage = 635 }
                3 -> playerl(FaceAnim.HAPPY, "I would like to hear a little of the history between the Fremenniks and the outlanders.").also { stage = 645 }
                4 -> playerl(FaceAnim.HAPPY, "Actually, history isn't really my thing.").also { stage = 665 }
            }
            618 -> npcl(FaceAnim.HAPPY, "Ah... the deathless one... We do not even know if Koschei is his name, when we found him he had no memory,").also { stage++ }
            619 -> npcl(FaceAnim.HAPPY, "he was simply repeating that single word. Whatever happened to him must have rattled his soul so hard that his memory ran away from him,").also { stage++ }
            620 -> npcl(FaceAnim.HAPPY, "and he has not yet found its hiding place.").also { stage++ }
            621 -> playerl(FaceAnim.ASKING, "So how exactly did you find him?").also { stage++ }
            622 -> npcl(FaceAnim.HAPPY, "Well, it was a raiding party like any other. We had just struck one of the smaller northern islands to harvest supplies by combat, as our laws allow,").also { stage++ }
            623 -> npcl(FaceAnim.HAPPY, "when we saw a figure in the icey waters. As we were at least a 3 week sail from any port, and saw no other ships nearby, he must have been there some time.").also { stage++ }
            624 -> npcl(FaceAnim.HAPPY, "We could not leave someone to the cold death of the ocean, even an outerlander, so we fished him aboard. He muttered continually, but his words made no sense.").also { stage++ }
            625 -> npcl(FaceAnim.HAPPY, "He had no clothes or possessions with him, and seemed to have been burnt somehow before he had landed in the water. I cannot help but wonder what burnt him,").also { stage++ }
            626 -> npcl(FaceAnim.HAPPY, "for as you may have seen, he is incredibly powerful and resistant to damage by any means I know of! Anyway, we decided to bring him back to Rellekka,").also { stage++ }
            627 -> npcl(FaceAnim.HAPPY, "for he did not seem of good health or sound mind, and simply repeated the word Koschei over and over. Yrsa tended to him for many weeks, until one day it was as though a new soul had entered his burnt and broken body.").also { stage++ }
            628 -> npcl(FaceAnim.HAPPY, "He suddenly left her house one day, fully healed, and able to speak to us as though nothing had happened to him.").also { stage++ }
            629 -> npcl(FaceAnim.HAPPY, "He still had no recollection of how he had come to be in the icey ocean, or of who he was, or where he had come from, but this did not matter to us or him.").also { stage++ }
            630 -> npcl(FaceAnim.HAPPY, "He took his trials of adulthood, as you did, and has been a boon to our clan ever since. Someday I fear he may regain his memory, and leave us for his past...").also { stage++ }
            631 -> npcl(FaceAnim.HAPPY, "But I will always respect any decision he decides to make should that day ever come upon us. Until then, he is more than welcome to stay here.").also { stage++ }
            632 -> npcl(FaceAnim.HAPPY, "So, ${FremennikTrials.getFremennikName(player!!)}, was there anything more you wished to hear?").also { stage = 616 }
            635 -> npcl(FaceAnim.HAPPY, "Well, I think the best thing for you would be to go to the docks and ask to join one of our longboats on a journey. My affairs are concentrated mostly on Rellekka,").also { stage++ }
            636 -> npcl(FaceAnim.HAPPY, "So my knowledge is not great, but I will happily share what little I do know with you, ${FremennikTrials.getFremennikName(player!!)}. North of this town the atmosphere becomes bitterly cold.",).also { stage++ }
            637 -> npcl(FaceAnim.HAPPY, "There are a number of small inhabited islands that we have sea routes with. The largest of these is possibly Miscellania,").also { stage++ }
            638 -> npcl(FaceAnim.HAPPY, "Although I hear that there has been some recent incident with their monarch, but I don't know the details.").also { stage++ }
            639 -> npcl(FaceAnim.HAPPY, "There is also of course the island of the moon clan, who change with the tides. They are our bitterest enemies, and we are currently at war with them.").also { stage++ }
            640 -> npcl(FaceAnim.HAPPY, "They are an evil people, equally cursed and blessed by the magic in their blood. Beyond those islands, there is the large frozen wasteland we call Acheron.").also { stage++ }
            641 -> npcl(FaceAnim.HAPPY, "It is an inhospitable land, and you will need an agile and sturdy body just to keep alive in its perils.").also { stage++ }
            642 -> npcl(FaceAnim.HAPPY, "As I say, my knowledge outside of this town is rather limited, and I know no more than this.").also { stage++ }
            643 -> npcl(FaceAnim.HAPPY, "Was there anything else you wished to hear tell of?").also { stage = 616 }
            645 -> npcl(FaceAnim.HAPPY, "Ah, now that is something I know a great deal about. Believe it or not, all outlanders were once orginally Fremenniks.").also { stage++ }
            646 -> npcl(FaceAnim.HAPPY, "When first man came to these lands all were Fremenniks, and followed our ways. We lived a happy life, and never settled in one place for long.").also { stage++ }
            647 -> npcl(FaceAnim.HAPPY, "We travelled by boat along the coastlines, never taking more from the land than could be regrown by the same time in the following year.").also { stage++ }
            648 -> npcl(FaceAnim.HAPPY, "However, many moons past, some of our tribesmen were weary of constantly travelling the lands, and decided to build themselves permanent homes.").also { stage++ }
            649 -> npcl(FaceAnim.HAPPY, "Unfortunately, the other races that had also migrated here did not like this. They waged war against us at every opportunity.").also { stage++ }
            650 -> npcl(FaceAnim.HAPPY, "We were driven mostly to the coastlines, where we could escape by boat when attacked. Remember, at this time we had no real way to defend against constant attack.").also { stage++ }
            651 -> npcl(FaceAnim.HAPPY, "Then one day one of our Seers, whose full name has been lost in the telling of this tale, discovered a cave full of strange rocks.").also { stage++ }
            652 -> npcl(FaceAnim.HAPPY, "This rock would never be consumed, no matter how much we took, and sparkled at its peak, and broke off in small and regular sized chunks when mined.").also { stage++ }
            653 -> npcl(FaceAnim.HAPPY, "Stranger than this, we found that these rocks would become filled with power when taken to certain places across this land! There was a terrible argument in our tribe!").also { stage++ }
            654 -> npcl(FaceAnim.HAPPY, "Some said that we should take as many of these rocks as we could, and use them in defending against our enemies' attacks!").also { stage++ }
            655 -> npcl(FaceAnim.HAPPY, "Others said that we had found a place that belonged only to the gods, and that should we steal what was not ours we would find only torment and misery.").also { stage++ }
            656 -> npcl(FaceAnim.HAPPY, "We voted on this, and decided that it was in the best interests of the tribe to leave these strange rocks where they lay, for the gods can be spiteful and cruel,").also { stage++ }
            657 -> npcl(FaceAnim.HAPPY, "especially to those who do not treat them with the respect they desire. Some of our number refused to accept this ruling by our council however.").also { stage++ }
            658 -> npcl(FaceAnim.HAPPY, "They began to mine these rocks, and set up transport systems to the various places of power that enchanted them. They even created temples at each of these places!").also { stage++ }
            659 -> npcl(FaceAnim.HAPPY, "This was going too far! We had no choice, but to expel them from our tribe forever!").also { stage++ }
            660 -> npcl(FaceAnim.HAPPY, "We turned our backs upon them, and let them know they would never be welcome back to our tribes until they had released themselves from the rocks.").also { stage++ }
            661 -> npcl(FaceAnim.HAPPY, "This is the tale of how the outlanders came about; through stealing from the gods, and from betraying our ideals. This is why we show them no trust.").also { stage++ }
            662 -> npcl(FaceAnim.HAPPY, "Was there something else you wished to hear?").also { stage = 616 }
            665 -> npcl(FaceAnim.HAPPY, "Well let me know should you wish to hear our lore and histories. We value them highly.").also { stage = END_DIALOGUE }
            1200 -> npcl(FaceAnim.HALF_THINKING, "Well, I ought not to give you such things lightly...").also { stage++ }
            1201 -> npcl(FaceAnim.HALF_THINKING, "I suppose I can grant you one temporarily, provided you meet certain requirements.").also { stage++ }
            1202 -> npcl(FaceAnim.HAPPY, "Very well, ${FremennikTrials.getFremennikName(player!!)}! Let me look you over and see if you're strong enough for this boon.").also { stage++ }
            1203 -> {
                if (player!!.hasItem(Item(Items.SEAL_OF_PASSAGE_9083))) {
                    npcl(FaceAnim.HALF_GUILTY, "I'm sorry, ${FremennikTrials.getFremennikName(player!!)}. You just don't have the experience needed for this gift. Please come back when you've learned more.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.HAPPY, "Yes, yes... I see it. You've got the strength and wisdom for this gift. Please, take this. For now.").also { stage++ }
                }
            }

            1204 -> {
                sendItemDialogue(player, Items.SEAL_OF_PASSAGE_9083, "Chieftan Brundt hands you a Seal of Passage.")
                addItemOrDrop(player, Items.SEAL_OF_PASSAGE_9083, 1)
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BRUNDT_THE_CHIEFTAIN_1294)


    companion object {
        private fun generateFremennikName(): String {
            val namePrefixes = arrayOf("Bal", "Bar", "Dal", "Dar", "Den", "Dok", "Jar", "Jik", "Lar", "Rak", "Ral", "Ril", "Sig", "Tal", "Thor", "Ton")
            val nameSuffixes = arrayOf("dar", "dor", "dur", "kal", "kar", "kir", "kur", "lah", "lak", "lim", "lor", "rak", "rar", "tin", "ton", "tor", "vald")
            val randomPrefix = Random.nextInt(0, 15)
            val randomSuffix = Random.nextInt(0, 16)

            return "${namePrefixes[randomPrefix]}${nameSuffixes[randomSuffix]}"
        }

        private fun cleanupAttributes(player: Player) {
            removeAttribute(player, GameAttributes.QUEST_VIKING_PEER_START)
            removeAttribute(player, GameAttributes.QUEST_VIKING_PEER_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_VOTES)
            removeAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_MANI_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_OLAF_START)
            removeAttribute(player, GameAttributes.QUEST_VIKING_STEW_START)
            removeAttribute(player, GameAttributes.QUEST_VIKING_ASKELADDEN_TALK)
            removeAttribute(player, GameAttributes.QUEST_VIKING_HAS_WOOL)
            removeAttribute(player, GameAttributes.QUEST_VIKING_OLAF_CONCERT)
            removeAttribute(player, GameAttributes.QUEST_VIKING_OLAF_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_THORVALD_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_SIGLI_VOTE)
            removeAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE_SOLVED)
        }
    }
}
