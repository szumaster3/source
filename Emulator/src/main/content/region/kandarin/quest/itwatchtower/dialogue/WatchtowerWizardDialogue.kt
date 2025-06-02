package content.region.kandarin.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.item.allInInventory
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Wizard dialogue.
 *
 * Relations
 * - [Watchtower Quest][content.region.kandarin.quest.itwatchtower.Watchtower]
 */
@Initializable
class WatchtowerWizardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.WATCHTOWER)

        if (npc.id == NPCs.WIZARD_5195) {
            player("What's going on here?")
            stage = 100
            return true
        }

        if(questStage == 100 && npc.id == NPCs.WIZARD_5195) {
            npc("All's well that ends well.")
            stage = END_DIALOGUE
            return true
        }

        val items = listOf(
            Items.RELIC_PART_1_2373 to GameAttributes.WATCHTOWER_RELIC_1,
            Items.RELIC_PART_2_2374 to GameAttributes.WATCHTOWER_RELIC_2,
            Items.RELIC_PART_3_2375 to GameAttributes.WATCHTOWER_RELIC_3
        )

        val hasAll = items.all { inInventory(player, it.first) }
        val turnedInAll = items.all { getAttribute(player, it.second, false) }
        val turnedInAny = items.any { getAttribute(player, it.second, false) }

        if (hasAll && !turnedInAny) {
            player("An ogre gave me these...")
            items.forEach { (item, attr) ->
                removeItem(player, item)
                setAttribute(player, attr, true)
            }
            stage = 600
            return true
        }

        val handedIn = items.any { (item, attr) ->
            if (!getAttribute(player, attr, false) && inInventory(player, item)) {
                removeItem(player, item)
                setAttribute(player, attr, true)
                true
            } else false
        }

        if (handedIn || turnedInAll) {
            player("An ogre gave me this...")
            stage = if (handedIn) 700 else 600
            return true
        }

        val alreadyGive = items.any { (item, attr) ->
            getAttribute(player, attr, false) && inInventory(player, item)
        }

        if (alreadyGive) {
            player("An ogre gave me this...")
            stage = 701
            return true
        }

        if(questStage in 10..20) {
            npc("Ah the warrior returns!")
            stage = 702
            return true
        }

        if(getAttribute(player!!, GameAttributes.WATCHTOWER_RIDDLE, false) || inInventory(player!!, Items.SKAVID_MAP_2376)) {
            npc("How is the quest going?")
            stage = 800
            return true
        }

        if(questStage == 100) {
            npcl(FaceAnim.HAPPY, "Greetings, friend. I trust all is well with you? Yanille is safe at last!")
            stage = 1000
            return true
        }

        if (questStage >= 1) {
            npc("Hello again. Did you find anything of interest?")
            stage = 30
        } else {
            npc("Who are you? Are you one of the new guards?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> player("No, I'm an adventurer.").also { stage++ }
            1 -> npc("Well what are you doing here?").also { stage++ }
            2 -> player("Looking for adventures - what else?").also { stage++ }
            3 -> npc("Oh my, oh my! What does it all matter in the end,", "anyway?").also { stage++ }
            4 -> options("What's the matter?", "You wizards are always complaining.").also { stage++ }
            5 -> when (buttonId) {
                1 -> player("What's the matter?").also { stage++ }
                2 -> player("You wizards are always complaining.").also { stage = 200 }
            }

            6 -> npc("Oh dear, oh dear. Darn and drat!").also { stage++ }
            7 -> npc("We try hard to keep this town protected.").also { stage++ }
            8 -> npc("But how can we do that when the Watchtower isn't", "working?").also { stage++ }
            9 -> player("What do you mean it isn't working?").also { stage++ }
            10 -> npc("The Watchtower here works by the power of magic. An", "ancient spell designed to ward off ogres that has been", "in place here for many moons.").also { stage++ }
            11 -> npc("The exact knowledge of the spell is lost to us now, but", "the essence of the spell has been infused into four", "powering crystals that keep the tower protected from", "the hordes in the Feldips.").also { stage++ }
            12 -> options("So how come the spell doesn't work?", "You wizards are always complaining.").also { stage++ }
            13 -> when (buttonId) {
                1 -> player("So how come the spell doesn't work?").also { stage++ }
                2 -> player("You wizards are always complaining.").also { stage = 200 }
            }

            14 -> npc("The crystals! The crystals have been taken!").also { stage++ }
            15 -> player("Taken?").also { stage++ }
            16 -> npc("Stolen!").also { stage++ }
            17 -> player("Stolen?").also { stage++ }
            18 -> npc("Yes, yes! Do I have to repeat myself?").also { stage++ }
            19 -> options("Can I be of help?", "I'm not sure I can help.", "I'm not interested.").also { stage++ }
            20 -> when (buttonId) {
                1 -> player("Can I be of help?").also { stage++ }
                2 -> player("I'm not sure I can help.").also { stage = 300 }
                3 -> player("I'm not interested.").also { stage = 400 }
            }
            21 -> npc("Help? Oh wonderful, dear traveller!").also { stage++ }
            22 -> npc("Yes I could do with an extra pair of eyes here.").also { stage++ }
            23 -> player("???").also { stage++ }
            24 -> npc("There must be some evidence of what has happened", "here. Perhaps you could assist me in searching for", "clues?").also { stage++ }
            25 -> player("I would be happy to.").also { stage++ }
            26 -> npc("Try searching the surrounding area. If you find", "anything unusual, bring it here.").also { stage++ }
            27 -> npc("Try the bushes - I've read enough adventure stories to", "know that clues get caught in bushes all the time.").also { stage++ }
            28 -> npc("I will tell the guards to let you past - that way, you", "can just use the ladder to get in and out.").also { stage++ }
            29 -> {
                end()
                setQuestStage(player, Quests.WATCHTOWER, 1)
            }

            30 -> {
                when {
                    allInInventory(player, *EVIDENCE_IDS) -> {
                        player("Have a look at these...")
                        stage = 34
                    }
                    anyInInventory(player, *EVIDENCE_IDS) -> {
                        EVIDENCE_IDS.forEach {
                            player("I found this ${it.asItem().name}...")
                            stage = 32
                        }
                    }
                    else -> {
                        player("No, sorry, nothing yet.")
                        stage = 31
                    }
                }
            }
            31 -> npc("Oh dear, oh dear, there must be something,", "somewhere...").also { stage = END_DIALOGUE }

            32 -> npc("Let me see...").also { stage++ }
            33 -> npc(FaceAnim.NEUTRAL, "No, sorry, this is not evidence. You need to keep", "searching, I'm afraid.").also { stage = END_DIALOGUE }

            34 -> npc("Interesting, very interesting.").also { stage++ }
            35 -> npc("Long nails...grey in colour...well chewed...").also { stage++ }
            36 -> npc("Of course! They belong to a skavid!").also { stage++ }
            37 -> player("A skavid?").also { stage++ }
            38 -> npc("A servant race to the ogres: grey, depressed-looking", "creatures, always losing nails, teeth and hair!").also { stage++ }
            39 -> npc("They inhabit the caves in the Feldip Hills.").also { stage++ }
            40 -> npc("They normally keep to themselves, though. It's unusual", "for them to venture from their caves.").also { stage++ }
            41 -> options("What do you suggest I do?", "Shall I search the caves?").also { stage++ }
            42 -> when (buttonId) {
                1 -> player("What do you suggest I do?").also { stage++ }
                2 -> player("Shall I search the caves?").also { stage++ }
            }
            43 -> npc("It's no good searching the caves. Well, not yet, anyway.").also { stage++ }
            44 -> player("Why not?").also { stage++ }
            45 -> npc("They are deep and complex. The only way you will", "navigate the caves is to have a map or something.").also { stage++ }
            46 -> npc("It may be that the ogres have one...").also { stage++ }
            47 -> player("And how do you know that?").also { stage++ }
            48 -> npc("Well... I don't.").also { stage++ }
            49 -> options("So what do I do?", "I won't bother then.").also { stage++ }
            50 -> when (buttonId) {
                1 -> player("So what do I do?").also { stage++ }
                2 -> player("I won't bother then.").also { stage = 500 }
            }
            51 -> npc("You need to be fearless and gain entrance to", "Gu'Tanoth, the city of the ogres, then find out how to", "navigate the caves.").also { stage++ }
            52 -> player("That sounds scary...").also { stage++ }
            53 -> npc("Ogres are nasty creatures, yes. Only a strong warrior,", "and a clever one at that, can get the better of them.").also { stage++ }
            54 -> player("What do I need to do to get into the city?").also { stage++ }
            55 -> {
                npc("Well, the guards need to be dealt with. You could start", "by checking out the ogre settlements found around", "here.",)
                removeItem(player, Items.FINGERNAILS_2384)
                stage++
            }
            56 -> npc("Tribal ogres often dislike their neighbours...").also { stage++ }
            57 -> npc("In the meantime, I'll throw those fingernails out for", "you.").also { stage++ }
            58 -> {
                end()
                setQuestStage(player, Quests.WATCHTOWER, 2)
            }

            100 -> npc("You'll have to speak with the Watchtower Wizard", "about that.").also { stage = END_DIALOGUE }
            200 -> npc("Complaining? Complaining!").also { stage++ }
            201 -> npcl(FaceAnim.ANNOYED, "What folks these days don't realise is that if it weren't for us wizards, this entire world would be overrun with every creature you could possibly imagine. And some you couldn't even conceive of!",).also { stage = END_DIALOGUE }
            300 -> npcl(FaceAnim.NEUTRAL, "That's typical, nowadays. It's left to us wizards to do all the work.").also { stage = END_DIALOGUE }
            400 -> npc("Hmph! Suit yourself.").also { stage = END_DIALOGUE }
            500 -> npcl(FaceAnim.NEUTRAL, "Won't bother? Won't bother! Perhaps this quest is too hard for you?").also { stage = END_DIALOGUE }

            600 -> npc(FaceAnim.HAPPY, "Excellent! That seems to be all the pieces. Now I can", "assemble it...").also {
                // Need gfx or wrong anim.
                animate(findLocalNPC(player, npc.id)!!, 4379)
                stage++
            }
            601 -> {
                npc("Hmm, yes, it is as I thought... A statue symbolising an", "ogre warrior of old.").also {
                    stage++
                }
            }
            602 -> npc(FaceAnim.HAPPY, "Well, if you ever want to make friends with an ogre,", "this is the item to have!").also {
                end()
                sendItemDialogue(player, Items.OGRE_RELIC_2372, "The wizard gives you a complete statue.")
                setQuestStage(player, Quests.WATCHTOWER, 10)
                addItemOrDrop(player, Items.OGRE_RELIC_2372, 1)
                removeAttributes(player,
                    GameAttributes.WATCHTOWER_TOBAN_GOLD,
                    GameAttributes.WATCHTOWER_TOBAN_KEY,
                    GameAttributes.WATCHTOWER_GORAD_TOOTH,
                    GameAttributes.WATCHTOWER_DRAGON_BONES,
                    GameAttributes.WATCHTOWER_RELIC_3,
                    GameAttributes.WATCHTOWER_RELIC_2,
                    GameAttributes.WATCHTOWER_RELIC_1,
                )
            }

            700 -> npcl(FaceAnim.HAPPY, "Ah, it's part of an old ogre statue. I'll see if I can fix it up for you. It might come in handy. There may be more parts to find... I'll keep this for later.").also { stage = END_DIALOGUE }
            701 -> npc("I already have that part...").also { stage = END_DIALOGUE }

            702 -> npc("Have you found a way into Gu'Tanoth yet?").also { stage++ }
            703 -> player("I can't get past the guards.").also { stage++ }
            704 -> npc("Well, ogres dislike others apart from their kind", "What you need is some form of proof of friendship.", "Something to trick them into believing you are their friend").also { stage++ }
            705 -> {
                val hasRelic = hasAnItem(player, Items.OGRE_RELIC_2372).container != null
                if(!hasRelic) {
                    options("I have lost the relic you gave me.", "I will find my way in, no problem.").also { stage++ }
                } else {
                    npc("...Which shouldn't be too hard considering their intelligence!").also { stage = END_DIALOGUE }
                }
            }
            706 -> options("I have lost the relic you gave me.", "I will find my way in, no problem.").also { stage++ }
            707 -> when(buttonId) {
                1 -> player("I have lost the relic you gave me.").also { stage++ }
                2 -> player("I will find my way in, no problem.").also { stage = 710 }
            }
            708 -> npc("What! lost the relic ? How careless!", "It's a good job I copied that design then...").also { stage++ }
            709 -> {
                end()
                if (freeSlots(player) == 0) {
                    sendMessage(player, "You don't have enough inventory space.")
                    return true
                }
                npc("You can take this copy instead, its just as good.")
                addItem(player, Items.OGRE_RELIC_2372)
                stage = END_DIALOGUE
            }
            710 -> npc("Yes, I'm sure you will...good luck.").also { stage = END_DIALOGUE }

            800 -> playerl(FaceAnim.NEUTRAL,"I have worked out the guard's puzzle.").also { stage++ }
            801 -> npc("My, my. A wordsmith as well as a hero.").also { stage++ }
            802 -> options("I am still trying to navigate the skavid caves.", "I am trying to get into the shamans' cave.", "It is going well.").also { stage++ }
            803 -> when(buttonId) {
                1 -> playerl(FaceAnim.NEUTRAL, "I am still trying to navigate the skavid caves.").also { stage++ }
                2 -> playerl(FaceAnim.NEUTRAL,"I am trying to get into the shamans' cave.").also { stage = 805 }
                3 -> playerl(FaceAnim.NEUTRAL,"It is going well.").also { stage = 815 }
            }
            804 -> npcl(FaceAnim.NEUTRAL, "Take some illumination with you or else it will be dark.").also { stage = END_DIALOGUE }
            805 -> npcl(FaceAnim.THINKING, "Yes, it will be well guarded. Hmmm, let me see...").also { stage++ }
            806 -> npcl(FaceAnim.FRIENDLY, "Ah yes, I gather some ogres are allergic to certain herbs. Now what was it? It had white berries and blue leaves...").also { stage++ }
            807 -> npcl(FaceAnim.HAPPY, "Cave nightshade, that's it! You should try looking through some of the caves.").also { stage++ }
            808 -> player(FaceAnim.HAPPY, "Alright!").also { stage++ }
            809 -> npcl(FaceAnim.HAPPY, "And don't forget to look out for the dragons!").also { stage++ }
            810 -> player(FaceAnim.PANICKED, "The WHAT?").also { stage++ }
            811 -> npc("Dragons.").also { stage++ }
            812 -> npcl(FaceAnim.FRIENDLY, "Didn't you know that the ogres keep blue dragons as pets and for magical ingredients?").also { stage++ }
            813 -> npcl(FaceAnim.FRIENDLY, "Well, regardless, I'm sure a bold, brave adventurer such as yourself won't have any problems.").also { stage++ }
            814 -> player(FaceAnim.THINKING, "Great...").also { stage = END_DIALOGUE }
            815 -> npcl(FaceAnim.FRIENDLY, "That's good to hear. We are much closer to fixing the tower now.").also { stage = END_DIALOGUE }

            1000 -> showTopics(
                IfTopic("Do you have any more quests for me?", 1001, getAttribute(player, GameAttributes.WATCHTOWER_TELEPORT, false), false),
                IfTopic("I lost the scroll you gave me.", 1002, !getAttribute(player, GameAttributes.WATCHTOWER_TELEPORT, false), false),
                Topic("That's okay.", 1003, false)
            )
            1001 -> npcl(FaceAnim.HALF_THINKING, "More quests? No, indeed, adventurer. You have done us a great service, already.").also { stage = END_DIALOGUE }
            1002 -> if(inInventory(player, Items.SPELL_SCROLL_2396)) {
                npcl(FaceAnim.LAUGH, "Ho, ho, ho! A comedian to the finish. There it is, in your backpack!")
                stage = END_DIALOGUE
            } else {
                npcl(FaceAnim.THINKING, "Never mind, have another...")
                addItem(player, Items.SPELL_SCROLL_2396, 1)
            }
            1003 -> npcl(FaceAnim.FRIENDLY, "We are always in your debt; do come and visit us again.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WATCHTOWER_WIZARD_872, NPCs.WIZARD_5195)

    companion object {
        private val EVIDENCE_IDS = intArrayOf(
            Items.FINGERNAILS_2384,
            Items.DAMAGED_DAGGER_2387,
            Items.TATTERED_EYE_PATCH_2388,
            Items.OLD_ROBE_2385,
            Items.UNUSUAL_ARMOUR_2386,
        )
    }
}
