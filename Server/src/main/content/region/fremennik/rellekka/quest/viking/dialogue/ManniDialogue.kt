package content.region.fremennik.rellekka.quest.viking.dialogue

import content.data.GameAttributes
import content.region.fremennik.rellekka.quest.viking.FremennikTrials
import core.api.*
import core.api.getQuestStage
import core.api.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Manni dialogue.
 *
 * # Relations
 * - [FremennikTrials]
 */
@Initializable
class ManniDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val questStage = getQuestStage(player, Quests.THE_FREMENNIK_TRIALS)

        if (questStage > 0) {
            when {
                inInventory(player, Items.LEGENDARY_COCKTAIL_3707, 1) -> {
                    playerl(FaceAnim.HAPPY, "Hey. I got your cocktail for you.")
                    stage = 170
                }

                inInventory(player, Items.CHAMPIONS_TOKEN_3706, 1) -> {
                    playerl(FaceAnim.ASKING, "So it doesn't bother you at all that you just gave up your place here for one drink?")
                    stage = 180
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_RETURN, false) -> {
                    playerl(FaceAnim.ASKING, "Is this trade item for you?")
                    stage = 165
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 12 -> {
                    playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find the longhall barkeeps' legendary cocktail, do you?")
                    stage = 162
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 0) == 11 -> {
                    playerl(FaceAnim.ASKING, "I don't suppose you have any idea where I could find a token to allow a seat at the champions table, do you?")
                    stage = 150
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_MANI_START, false) -> {
                    val kegAvailable =
                        inInventory(player, Items.LOW_ALCOHOL_KEG_3712) || inInventory(player, Items.KEG_OF_BEER_3711)
                    if (kegAvailable) {
                        npc("Ah, I see you have your keg of beer. Are ye ready to", "drink against each other?")
                        stage = 101
                    } else {
                        npc("Come back when you're ready to begin", "the contest.")
                        stage = 1000
                    }
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_MANI_VOTE, false) -> {
                    npc("You have my vote!")
                    stage = 1000
                }

                isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                    playerl(FaceAnim.HAPPY, "Howdy!")
                    stage = 190
                }

                else -> {
                    player("Hello there!")
                    stage = 0
                }
            }
            return true
        } else {
            playerl(FaceAnim.HAPPY, "Hello there!")
            stage = 200
            return true
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Hello outerlander. I overheard your conversation with", "Brundt just now. You wish to become a member of the", "Fremennik?").also { stage++ }
            1 -> player("That's right! Why, are you on the counsel?").also { stage++ }
            2 -> npc("Do not let my drink-ssussed appearance fool you, I", "earnt my place on the council many years past.I am", "always glad to see new blood enter our tribe, and will", "happily vote for you.").also { stage++ }
            3 -> player("Great!").also { stage++ }
            4 -> npc("Providing you can pass a little test for me. As a", "Fremennik, you will need to show cunning, stamina,", "fastitude, and an iron constitution. I know of only one", "way to test all of these.").also { stage++ }
            5 -> player("And what's that?").also { stage++ }
            6 -> npc("Why, a drinking contest!").also { stage++ }
            7 -> npc("The task is simple enough! You versus me, a stiff drink", "each, last man standing wins the trial. So what say you?").also { stage++ }
            8 -> options("Yes", "No").also { stage++ }
            9 -> when (buttonId) {
                1 -> player("A drinking contest? Easy. Set them up, and I'll knock", "them back.").also { stage++ }
                2 -> player("I don't like the sound of that.").also { stage = 12 }
            }
            10 -> npc("When you are ready to begin, go and pick up a keg", "from that table over there, and come back here.").also { stage++ }
            11 -> {
                npc("We start when you have your keg of beer with you", "and finish when one of us can drink no more and", "yields.")
                stage = END_DIALOGUE
                setAttribute(player, GameAttributes.QUEST_VIKING_MANI_START, true)
            }
            12 -> npc("That's a shame.").also { stage = END_DIALOGUE }
            101 -> options("Yes", "No").also { stage++ }
            102 -> when (buttonId) {
                1 -> player("Yes, let's start this drinking contest!").also { stage++ }
                2 -> player("No, I don't think I am.").also { stage = END_DIALOGUE }
            }
            103 -> npc("As you wish outerlander; I will drink first, then you will", "drink.").also { stage++ }
            104 -> {
                sendMessage(player, "The Fremennik drinks his tankard first. He staggers a little bit.")
                Pulser.submit(DrinkingPulse(player, findLocalNPC(player, npc.id), getAttribute(player, GameAttributes.QUEST_VIKING_MANI_KEG, false)))
                end()
            }
            150 -> npcl(FaceAnim.HAPPY, "As a matter of fact, I do. I have one right here. I earnt my place here at the longhall for surviving over 5000 battles and raiding parties.").also { stage++ }
            151 -> npcl(FaceAnim.HAPPY, "Due to my contribution to the tribe, I am now permitted to spend my days here in the longhall listening to the epic tales of the bard, and drinking beer.").also { stage++ }
            152 -> playerl(FaceAnim.HAPPY, "Cool. That sounds pretty sweet! So I guess you don't want to give it away?").also { stage++ }
            153 -> npcl(FaceAnim.SAD, "I think it sounds better than it actually is outerlander. I miss my glory days of combat on the battlefield.").also { stage++ }
            154 -> npcl(FaceAnim.SAD, "And to tell you the truth, the beer here isn't great, and the bards' music is lousy. I would happily give up my token if it were not for the one thing that keeps me here.").also { stage++ }
            155 -> npcl(FaceAnim.HAPPY, "Our barkeep is one of the best in the world, and has worked in taverns across the land. When she was younger, she experimented a lot with her drinks").also { stage++ }
            156 -> npcl(FaceAnim.HAPPY, "and invented a cocktail so alcoholic and tasty that it has become something of a legend to all who enjoy a drink.").also { stage++ }
            157 -> npcl(FaceAnim.SAD, "Unfortunately, she decided that cocktails were not a suitable drink for Fremennik warriors, and vowed to never again make it.").also { stage++ }
            158 -> npcl(FaceAnim.SAD, "I have been here every day since she returned, hoping that someday she might change her mind and I might try this legendary cocktail for myself. Alas, it has never come to pass...").also { stage++ }
            159 -> npcl(FaceAnim.SAD, "If you can persuade her to make me her legendary cocktail, I will be happy to never let another drop of alcohol pass my lips, and will give you my champions token.").also { stage++ }
            160 -> playerl(FaceAnim.THINKING, "That's all?").also { stage++ }
            161 -> {
                npcl(FaceAnim.NEUTRAL, "That is all.")
                player.incrementAttribute(GameAttributes.QUEST_VIKING_SIGMUND_PROGRESS, 1)
                stage = END_DIALOGUE
            }
            162 -> npcl(FaceAnim.ANNOYED, "Uh... yes, the longhall barkeep has it. So could you get me my drink now please?").also { stage = END_DIALOGUE }
            165 -> npcl(FaceAnim.ANNOYED, "Not me, no.").also { stage = END_DIALOGUE }
            170 -> {
                npcl(
                    FaceAnim.AMAZED,
                    "...It is true! The legendary cocktail! I have waited for this day ever since I first started drinking!",
                )
                removeItem(player, Items.LEGENDARY_COCKTAIL_3707)
                addItemOrDrop(player, Items.CHAMPIONS_TOKEN_3706, 1)
                stage++
            }
            171 -> npcl(FaceAnim.HAPPY, "Here outerlander, you may take my token. I will happily give up my place at the longhalls table of champions just for a taste of this exquisite beverage!").also { stage++ }
            172 -> playerl(FaceAnim.ASKING, "It's just a drink...").also { stage++ }
            173 -> npcl(FaceAnim.HAPPY, "No, it is an artform. A drink such as this should be appreciated, and admired.").also { stage++ }
            174 -> npcl(FaceAnim.HAPPY, "It is like a fine painting, or a tasteful sculpture. If what I hear is true, then all other drinks become like unpalatable water in comparison to this!").also { stage++ }
            175 -> playerl(FaceAnim.HAPPY, "I guess you're happy with the trade then!").also { stage = END_DIALOGUE }
            180 -> npcl(FaceAnim.HAPPY, "Ah, but it was not just any drink... It was the finest cocktail ever created! Now that I have tasted it, I need never drink again, for my tastebuds will never be so excited!").also { stage++ }
            181 -> playerl(FaceAnim.ASKING, "So it was nice?").also { stage++ }
            182 -> npcl(FaceAnim.HAPPY, "It was... exquisite!").also { stage++ }
            183 -> playerl(FaceAnim.ASKING, "What did it taste of, then?").also { stage++ }
            184 -> npcl(FaceAnim.HAPPY, "Mostly tomato juice.").also { stage = END_DIALOGUE }
            190 -> {
                npcl(FaceAnim.HAPPY, "Hey! It's ${FremennikTrials.getFremennikName(player)}! Let me buy you a drink!")
                addItem(player, Items.BEER_1917)
                stage++
            }

            191 -> npcl(FaceAnim.HAPPY, "There ya go! Anyone who can drink like you earns my respect!").also {
                stage = END_DIALOGUE
            }
            200 -> npcl(FaceAnim.HAPPY, "Do not think me rude outerlander, but our customs forbid me talking to you. All contact with outlanders must be vetted by our chieftain, Brundt.").also { stage++ }
            201 -> playerl(FaceAnim.ASKING, "Where is this Brundt?").also { stage++ }
            202 -> npcl(FaceAnim.HAPPY, "He is standing just over there. He will speak for the tribe.").also { stage = END_DIALOGUE }
        }
        return true
    }

    /**
     * Represents pulse used to drinking the keg in Fremennik trials.
     */
    private class DrinkingPulse(val player: Player?, val npc: NPC?, private val lowAlcohol: Boolean? = false) : Pulse() {
        var counter = 0

        override fun pulse(): Boolean {
            if (!lowAlcohol!!) {
                when (counter++) {
                    0 -> {
                        player?.lock()
                        npc?.lock()
                        npc?.isNeverWalks = true
                    }

                    1 -> {
                        player?.face(npc)
                        npc?.face(player)
                    }

                    3 -> npc?.animator?.animate(Animation(Animations.DRINK_KEG_1330, Animator.Priority.HIGH))
                    5 -> {
                        player?.animator?.animate(Animation(Animations.DRINK_KEG_1330, Animator.Priority.HIGH))
                        player?.inventory?.remove(Item(Items.KEG_OF_BEER_3711))
                        sendMessage(player!!, "You drink from your keg. You feel extremely drunk...")
                    }
                    7 -> player?.dialogueInterpreter?.sendDialogues(player, FaceAnim.DRUNK, "Ish no fair!! I canna drink another drop! I alsho", "feel veddy, veddy ill...")
                    15 -> player?.dialogueInterpreter?.sendDialogues(npc, FaceAnim.DRUNK, "I guessh I win then ouddaladder! (hic) Niche try,", "anyway!")
                    16 -> {
                        player?.unlock()
                        npc?.unlock()
                        player?.face(player)
                        npc?.face(npc)
                        npc?.isNeverWalks = false
                        return true
                    }
                }
            } else {
                when (counter++) {
                    0 -> {
                        player?.lock()
                        npc?.lock()
                        npc?.isNeverWalks = true
                    }

                    1 -> {
                        player?.face(npc)
                        npc?.face(player)
                    }

                    3 -> npc?.animator?.animate(Animation(Animations.DRINK_KEG_1330, Animator.Priority.HIGH))
                    5 -> {
                        player?.animator?.animate(Animation(Animations.DRINK_KEG_1330, Animator.Priority.HIGH))
                        player?.inventory?.remove(Item(Items.KEG_OF_BEER_3711))
                        sendMessage(player!!, "You drink from your keg. You don't feel at all drunk.")
                    }

                    7 -> player?.dialogueInterpreter?.sendDialogues(player, FaceAnim.HAPPY, "Aaaah, lovely stuff. So you want to get the next round", "in, or shall I? You don't look so good there!")
                    15 -> player?.dialogueInterpreter?.sendDialogues(npc, FaceAnim.DRUNK, "Wassha? Guh? You drank that whole keg! But it dinnna", "affect you at all! I conshede! You can probably", "outdrink me!")
                    21 -> player?.dialogueInterpreter?.sendDialogues(npc, FaceAnim.DRUNK, "I jusht can't (hic) believe it! Thatsh shome might fine", "drinking legs you got! Anyone who can drink like", "THAT getsh my vote atta somsh.... coumah... gets my", "vote!")

                    22 -> {
                        player?.unlock()
                        npc?.unlock()
                        player?.face(player)
                        npc?.face(npc)
                        npc?.isNeverWalks = false
                        /*
                         * End of Revellers trial.
                         */
                        removeAttributes(player!!, GameAttributes.QUEST_VIKING_MANI_BOMB, GameAttributes.QUEST_VIKING_MANI_START, GameAttributes.QUEST_VIKING_MANI_KEG)
                        setAttribute(player, GameAttributes.QUEST_VIKING_MANI_VOTE, true)
                        setAttribute(player, GameAttributes.QUEST_VIKING_VOTES, getAttribute(player, GameAttributes.QUEST_VIKING_VOTES, 0) + 1)
                        sendMessage(player, "Congratulations! You have completed the Revellers' trial.")
                        return true
                    }
                }
            }
            return false
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MANNI_THE_REVELLER_1286)
}
