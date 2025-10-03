package content.global.activity.mogre

import content.region.island.tutorial.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.*

/**
 * Represents the Skippy dialogue.
 * @author szu
 */
@Initializable
class SkippyDialogue(player: Player? = null) : Dialogue(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SkippyDialogueFile(), npc)
        return false
    }
    override fun newInstance(player: Player?): Dialogue = SkippyDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.SKIPPY_2795, NPCs.SKIPPY_2796, NPCs.SKIPPY_2797, NPCs.SKIPPY_2798, NPCs.SKIPPY_2799)
}

class SkippyDialogueFile : DialogueFile() {

    companion object {
        const val NETTLE_TEA_BOWL = Items.NETTLE_TEA_4239
        const val NETTLE_TEA_CUP = Items.CUP_OF_TEA_4242
        const val NETTLE_TEA_PORCELAIN_CUP = Items.CUP_OF_TEA_4245
        const val EMPTY_BOWL = Items.BOWL_1923
        const val EMPTY_CUP = Items.EMPTY_CUP_1980
        const val EMPTY_PORCELAIN_CUP = Items.PORCELAIN_CUP_4244
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                /*
                 * Handles tutorial island dialogue.
                 */

                if (player!!.zoneMonitor.isInZone("tutorial")) {
                    npcl(FaceAnim.HALF_ASKING, "Do you wanna skip the Tutorial?")
                    stage = 88
                    return
                }

                /*
                 * Handles activity dialogue.
                 */

                when (getVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344)) {
                    0 -> {
                        stage = END_DIALOGUE
                        val message = if (!inInventory(player!!, Items.BUCKET_OF_WATER_1929)) {
                            "You know, I could shock him out of it if I could find some cold water..."
                        } else {
                            stage = -1
                            "Well, I could dump this bucket of water over him. That would sober him up a little."
                        }
                        playerl(FaceAnim.HALF_GUILTY, message)
                    }
                    1 -> {
                        player("Hey, Skippy.")
                        stage = 12
                    }
                    2 -> {
                        player("Hey, Skippy.")
                        stage = 30
                    }
                    else -> {
                        player("Hey, Skippy.")
                        stage = 77
                    }
                }
            }
            -1 -> options("Throw the water!", "I think I'll leave it for now.").also { stage = 1 }
            1 -> when (buttonID) {
                1 -> player("Hey, Skippy!").also { stage++ }
                2 -> end()
            }
            2 -> npc("What?").also { stage++ }
            3 -> {
                val duration = animationDuration(Animation(Animations.USE_BUCKET_ALT_4255))
                if(!removeItem(player!!, Items.BUCKET_OF_WATER_1929)) return
                submitIndividualPulse(
                    player!!,
                    object : Pulse(duration) {
                        override fun pulse(): Boolean {
                            animate(player!!, Animations.USE_BUCKET_ALT_4255)
                            playAudio(player!!, Sounds.SKIPPY_BUCKET_1399)
                            addItem(player!!, Items.BUCKET_1925)
                            npc("Ahhhhhhhhhhgh! That's cold! Are you trying to kill me?")
                            stage = 5
                            return true
                        }
                    },
                )
            }
            5 -> playerl(FaceAnim.HALF_ASKING, "Nope. Just sober you up. Memory coming back yet?").also { stage++ }
            6 -> npcl(FaceAnim.NEUTRAL, "No. But I could do with a bowl of tea to warm myself up a bit. Go grab me one and we'll talk.").also { stage++ }
            7 -> playerl(FaceAnim.HAPPY, "Any particular type of tea?").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL, "Nettle for preference. Just make sure it's hot.").also { stage++ }
            9 -> npcl(FaceAnim.NEUTRAL, "And don't throw it at me!").also { stage++ }
            10 -> player(FaceAnim.FRIENDLY, "What's your problem? You're all clean now.").also { stage++ }
            11 -> end().also { setVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344, 1, true) }
            12 -> npc("Gaah! Don't drench me again!").also { stage++ }
            13 -> player("Hey! I only did that once! Try not to be such a big", "baby!").also { stage++ }
            14 -> npc("So what are you here for then?").also { stage++ }
            15 -> {
                if (!anyInInventory(player!!, NETTLE_TEA_BOWL, NETTLE_TEA_CUP, NETTLE_TEA_PORCELAIN_CUP)) {
                    playerl(FaceAnim.NEUTRAL, "No real reason. I just thought I would check up on you is all.").also {
                        stage = 29
                    }
                } else {
                    var gaveTea = false

                    if (inInventory(player!!, NETTLE_TEA_BOWL)) {
                        gaveTea = removeItem(player!!, NETTLE_TEA_BOWL) && addItem(player!!, EMPTY_BOWL)
                    } else if (inInventory(player!!, NETTLE_TEA_CUP)) {
                        gaveTea = removeItem(player!!, NETTLE_TEA_CUP) && addItem(player!!, EMPTY_CUP)
                    } else if (inInventory(player!!, NETTLE_TEA_PORCELAIN_CUP)) {
                        gaveTea = removeItem(player!!, NETTLE_TEA_PORCELAIN_CUP) && addItem(player!!, EMPTY_PORCELAIN_CUP)
                    }

                    if (gaveTea) {
                        playerl(FaceAnim.HAPPY, "I've come to give you your tea.")
                        stage++
                    } else {
                        playerl(FaceAnim.NEUTRAL, "No real reason. I just thought I would check up on you is all.")
                        stage = 29
                    }
                }
            }

            16 -> npcl(FaceAnim.HALF_GUILTY, "Excellent! I was thinking I was going to freeze out here!").also { stage++ }
            17 -> sendDialogue(player!!, "Skippy drinks the tea and clutches his forehead in pain.").also { stage++ }
            18 -> npc("Ohhhhh...").also { stage++ }
            19 -> player("What? What's wrong?").also { stage++ }
            20 -> npcl(FaceAnim.NEUTRAL, "Not so loud...I think I have a hangover...").also { stage++ }
            21 -> player("Great...Well, I doubt you can remember anything", "through a hangover. What a waste of nettle tea...").also { stage++ }
            22 -> npc("Hey! A little sympathy here?").also { stage++ }
            23 -> npc("Owwowwoww... must remember not to shout...").also { stage++ }
            24 -> npc("Look, I do know a hangover cure. If you can get me a", "bucket of the stuff I think I'll be ok.").also { stage++ }
            25 -> player("Wait... is this cure a bucket of chocolate milk and snape", "grass?").also { stage++ }
            26 -> npc("Yes!, That's the stuff!").also { stage++ }
            27 -> player("Ahhh. Yes, I've made some of that stuff before. I should", "be able to get you some, no problem.").also { stage++ }
            28 -> end().also { setVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344, 2, true) }
            29 -> npc("Well, I'm still wet, still cold and still", "waiting on that nettle tea.").also { stage = END_DIALOGUE }
            30 -> npcl(FaceAnim.HALF_GUILTY, "Egad! Don't you know not to shout around a guy with a hangover?").also { stage++ }
            31 -> npcl(FaceAnim.HALF_GUILTY, "Ahhhhhg...No more shouting for me...").also { stage++ }
            32 -> npcl(FaceAnim.HALF_GUILTY, "What is it anyway?").also { stage++ }
            33 -> {
                if (!removeItem(player!!, Items.HANGOVER_CURE_1504)) {
                    playerl(FaceAnim.HALF_GUILTY, "I just came back to ask you something.").also { stage++ }
                } else {
                    playerl(FaceAnim.HAPPY, "Well Skippy, you will no doubt be glad to hear that I got you your hangover cure!").also {
                        addItem(player!!, Items.BUCKET_1925)
                        stage = 46
                    }
                }
            }
            34 -> npc("What?").also { stage++ }
            35 -> options("How do I make that hangover cure again?", "Why do they call you 'Skippy'?").also { stage++ }
            36 -> when (buttonID) {
                1 -> player("How do I make that hangover cure again?").also { stage++ }
                2 -> player("Why do they call you 'Skippy'?").also { stage = 15 }
            }
            37 -> npcl(FaceAnim.HALF_GUILTY, "Give me strength...Here's what you do. Pay attention!").also { stage++ }
            38 -> npcl(FaceAnim.HALF_GUILTY, "You take a bucket of milk, a bar of chocolate and some snape grass.").also { stage++ }
            39 -> npcl(FaceAnim.HALF_GUILTY, "Grind the chocolate with a pestle and mortar.").also { stage++ }
            40 -> npcl(FaceAnim.HALF_GUILTY, "Add the chocolate powder to the milk, then add the snape grass.").also { stage++ }
            41 -> npcl(FaceAnim.HALF_GUILTY, "Then bring it here and I will drink it.").also { stage++ }
            42 -> npcl(FaceAnim.HALF_GUILTY, "Are you likely to remember that or should I go get some crayons and draw you a picture?").also { stage++ }
            43 -> playerl(FaceAnim.HALF_GUILTY, "Hey! I remember it now, ok! See you in a bit.").also { stage = END_DIALOGUE }
            44 -> npcl(FaceAnim.HALF_GUILTY, "I think it may have something to do with my near-constant raving about mudskippers.").also { stage++ }
            45 -> npcl(FaceAnim.HALF_GUILTY, "That or it's something to do with that time with the dress and the field full of daisies...").also { stage = END_DIALOGUE }
            46 -> npc("Gimme!").also { stage++ }
            47 -> sendDialogue(player!!, "Skippy chugs the hangover cure... very impressive.").also { stage++ }
            48 -> npc("Ahhhhhhhhhhhhhhh...").also { stage++ }
            49 -> npc("Much better...").also { stage++ }
            50 -> player("Feeling better?").also { stage++ }
            51 -> npc("Considerably.").also { stage++ }
            52 -> player("Then tell me where the mudskippers are!").also { stage++ }
            53 -> npc("Much better...").also { stage++ }
            54 -> npcl(FaceAnim.HALF_GUILTY, "I wish you wouldn't go looking for them. Those vicious killers will tear you apart.").also { stage++ }
            55 -> npc("It's all becoming clear to me now...").also { stage++ }
            56 -> npc("I was fishing using a Fishing Explosive...").also { stage++ }
            57 -> player("A Fishing Explosive?").also { stage++ }
            58 -> npcl(FaceAnim.HALF_GUILTY, "Well, Slayer Masters sell these highly volatile potions for killing underwater creatures.").also { stage++ }
            59 -> npcl(FaceAnim.HALF_GUILTY, "If you don't feel like lobbing a net about all day you can use them to fish with...").also { stage++ }
            60 -> npcl(FaceAnim.HALF_GUILTY, "But this time I was startled by what I thought was a giant mudskipper.").also { stage++ }
            61 -> npcl(FaceAnim.HALF_GUILTY, "What it was, infact, was a...").also { stage++ }
            62 -> npc("Dramatic Pause...").also { stage++ }
            63 -> npc("A Mogre!").also { stage++ }
            64 -> player("What exactly is a Mogre?").also { stage++ }
            65 -> npcl(FaceAnim.HALF_GUILTY, "A Mogre is a type of Ogre that spends most of its time underwater.").also { stage++ }
            66 -> npcl(FaceAnim.HALF_GUILTY, "They hunt giant mudskippers by wearing their skins and swimming close until they can attack them.").also { stage++ }
            67 -> npcl(FaceAnim.HALF_GUILTY, "When I used the Fishing Explosive I scared off all the fish, and so the Mogre got out of the water to express its extreme displeasure.").also { stage++ }
            68 -> npc("My head still hurts.").also { stage++ }
            69 -> playerl(FaceAnim.HALF_ASKING, "I take it the head injury is responsible for the staggering and yelling?").also { stage++ }
            70 -> npc("No, no.").also { stage++ }
            71 -> npcl(FaceAnim.HALF_GUILTY, "My addiction to almost-lethal alcohol did that.").also { stage++ }
            72 -> npcl(FaceAnim.HALF_GUILTY, "But if you are set on finding those Mogres just head south from here until you find Mudskipper Point.").also { stage++ }
            73 -> playerl(FaceAnim.HALF_ASKING, "The mudskipper-eating monsters are to be found at Mudskipper point?").also { stage++ }
            74 -> player("Shock!").also { stage++ }
            75 -> player("Thanks. I'll be careful.").also { stage++ }
            76 -> end().also { setVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE_AND_SKIPPY_1344, 3, true) }
            77 -> npcl(FaceAnim.HALF_GUILTY, "Hey you!").also { stage++ }
            78 -> playerl(FaceAnim.HALF_GUILTY, "How do I annoy the Mogres again?").also { stage++ }
            79 -> npcl(FaceAnim.HALF_GUILTY, "Go south to Mudskipper Point and lob a Fishing Explosive into the sea. You can grab them from the Slayer masters.").also { stage++ }
            80 -> playerl(FaceAnim.HALF_GUILTY, "Thanks! So, what are you going to do now?").also { stage++ }
            81 -> npcl(FaceAnim.HALF_GUILTY, "Well, I was planning on continuing my hobby of wandering up and down this bit of coastline, bellowing random threats and throwing bottles.").also { stage++ }
            82 -> npcl(FaceAnim.HALF_GUILTY, "And you?").also { stage++ }
            83 -> playerl(FaceAnim.HALF_GUILTY, "I was planning on wandering up and down the landscape, bugging people to see if they had mindblowingly dangerous quests for me to undertake.").also { stage++ }
            84 -> npcl(FaceAnim.HALF_GUILTY, "Well, good luck with that!").also { stage++ }
            85 -> playerl(FaceAnim.HALF_GUILTY, "You too!").also { stage++ }
            86 -> npcl(FaceAnim.DISGUSTED, "Weirdo...").also { stage++ }
            87 -> playerl(FaceAnim.DISGUSTED, "Loony...").also { stage = END_DIALOGUE }

            /*
             * Tutorial island dialogue extension.
             */

            88 -> {
                setTitle(player!!, 4)
                sendDialogueOptions(player!!, "What would you like to say?", "Yes, please.", "Who are you?", "Can I decide later?", "I'll stay here for the Tutorial.")
                stage++
            }
            89 -> when (buttonID) {
                1 -> npc(FaceAnim.HAPPY, "Prepare yourself!").also { stage = 97 }
                2 -> player(FaceAnim.HALF_ASKING, "Who are you?").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING, "Can I decide later?").also { stage = 93 }
                4 -> player("I'll stay here for the Tutorial.").also { stage = 94 }
            }
            90 -> npcl(FaceAnim.NEUTRAL, "My name's Skippy. Normally I live down by Rimmington. You should come and see me when you're passing.").also { stage++ }
            91 -> npcl(FaceAnim.LAUGH, "Just lately the Council wants to let people skip the Tutorial, so - ha ha ha - they dump the job on Skippy. Bah!").also { stage++ }
            92 -> npcl(FaceAnim.HALF_ASKING, "So, anyway, do you want to skip the Tutorial?").also { stage = 95 }
            93 -> npcl(FaceAnim.NOD_NO, "Unfortunately, so far there is no such possibility.").also { stage = 92 }
            94 -> npcl(FaceAnim.NOD_YES, "Good choice.").also { stage = END_DIALOGUE }
            95 -> sendDialogueOptions(player!!, "What would you like to say?", "Yes, please.", "Can I decide later?", "I'll stay here for the Tutorial.").also { stage++ }
            96 ->
                when (buttonID) {
                    1 -> npc(FaceAnim.HAPPY, "Prepare yourself!").also { stage = 97 }
                    2 -> player(FaceAnim.HALF_ASKING, "Can I decide later?").also { stage = 93 }
                    3 -> player("I'll stay here for the Tutorial.").also { stage = 94 }
                }

            97 -> {
                end()
                TutorialStage.completeTutorial(player!!)
            }
        }
    }
}
