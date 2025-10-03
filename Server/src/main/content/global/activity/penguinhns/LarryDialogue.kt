package content.global.activity.penguinhns

import content.data.GameAttributes
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.Log
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Larry dialogue.
 * @author szu
 */
class LarryDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        setTitle(player, 2)
        sendDialogueOptions(player, "I want to speak to Larry about:", "Cold War", "Penguin Hide and Seek")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val points = player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
        val lostNotebook = hasAnItem(player, Items.SPY_NOTEBOOK_13732).container != null
        when (stage) {
            0 -> when (buttonId) {
                1 -> end()
                2 -> {
                    if (!getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, false)) {
                        npc(FaceAnim.THINKING, "What do you want?")
                        stage = 1
                    } else if (getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, false) && !lostNotebook) {
                        npcl(FaceAnim.HALF_ASKING, "Wait! Where if your spy notebook? Did it fall into enemy hands?")
                        stage = 17
                    } else {
                        npcl(FaceAnim.CRYING, "Do you have news? Have you found more?")
                        stage = 35
                    }
                }
            }
            1 -> player("Uh, I just wanted to as-").also { stage++ }
            2 -> npcl(FaceAnim.THINKING,"SHHHHHH! They're listening. Keep your voice down.").also { stage++ }
            3 -> player(FaceAnim.ASKING, "*whispers* Who's listening?").also { stage++ }
            4 -> npcl(FaceAnim.THINKING, "Never mind. Are you the inquisitive sort? Are you willing to go on an expedition for me?").also { stage++ }
            5 -> playerl(FaceAnim.HALF_ASKING, "What would I need to do on this expedition?").also { stage++ }
            6 -> npcl(FaceAnim.STRUGGLE, "The zoo has granted me permission to study penguins abroad. It's my, er...understanding that there are many penguins located around the world.").also { stage++ }
            7 -> playerl(FaceAnim.HALF_ASKING, "Why do you want to find penguins around the world?").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "I need to see if they're organis-, I mean, if they're migrating or something like that.").also { stage++ }
            9 -> playerl(FaceAnim.FRIENDLY, "You think they're organised? They're just penguins!").also { stage++ }
            10 -> npcl(FaceAnim.FRIENDLY, "Do not underestimate them! They're clever and tricky and LISTENING! I know they're up to something.").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "That's why I'm recruiting brave adventurers to find these penguins and tell me of their locations.").also { stage++ }
            12 -> playerl(FaceAnim.HALF_ASKING, "Well, I don't think they're organised, but I do travel all over the world. I could give you a hand finding them. What do I need to do?").also { stage++ }
            13 -> npcl(FaceAnim.NEUTRAL, "Whenever you spot a penguin, spy on it. They're well trained and will change their positions every week, so keep your eyes peeled.").also { stage++ }
            14 -> npcl(FaceAnim.NEUTRAL, "Report back here and I will reward you for your efforts.").also { stage++ }
            15 -> playerl(FaceAnim.FRIENDLY, "Great, I'll get started right away.").also { stage++ }
            16 -> {
                when {
                    !lostNotebook -> {
                        npc("Hold on, there. Take this notebook.")
                        stage = 34
                    }
                    !getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, false) -> {
                        setAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, true)
                        end()
                    }
                    else -> end()
                }
            }
            17 -> {
                sendItemDialogue(player, Items.SPY_NOTEBOOK_13732, "You receive a penguin spy notebook.")
                addItemOrDrop(player, Items.SPY_NOTEBOOK_13732, 1)
                stage = 0
            }
            18 -> {
                setTitle(player, 5)
                showTopics(
                    Topic("I've found more penguins.", 35),
                    Topic("I'm having trouble finding the penguins, can I have a hint?", 31),
                    Topic("I want to claim my reward.", 26),
                    IfTopic("I need a new notebook.", 34, !lostNotebook),
                    IfTopic("What do I need to do again?", 13, lostNotebook),
                    Topic("Never mind.", 32),
                )
            }
            19 -> playerl(FaceAnim.FRIENDLY, "That's not all! I discovered there are secret agent penguins that have started to appear across Gielinor as well!").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Prawn crackers! Surely not? I heard the whispers but I never believed they would have reached this stage already.").also { stage++ }
            21 -> playerl(FaceAnim.FRIENDLY, "What should we do about them?").also { stage++ }
            22 -> npcl(FaceAnim.FRIENDLY, "Let's just keep tracking them for now. They have to slip up eventually.").also { stage++ }
            23 -> playerl(FaceAnim.FRIENDLY, "Because of the ice, right?").also { stage++ }
            24 -> npcl(FaceAnim.FRIENDLY, "This isn't a laughing matter, Player! Pay attention or who knows what may happen to you!").also { stage++ }
            25 -> playerl(FaceAnim.FRIENDLY, "Sorry. Full focus from now on.").also { stage++ }
            26 -> {
                if(points == 0) {
                    npc(FaceAnim.SAD, "Uh, you don't have any points", "to turn in.").also { stage = END_DIALOGUE }
                } else {
                    npc(FaceAnim.FRIENDLY, "Well, you need rewarding for your hard work. You","have $points Penguin Points.").also { stage = 36 }
                }
            }
            27 -> options("Show me the money!", "Experience, all the way!").also { stage++ }
            28 -> when(buttonId) {
                1 -> player(FaceAnim.HAPPY, "I want the coins reward.").also { stage = 30 }
                2 -> player(FaceAnim.HAPPY, "I want the experience reward.").also { stage++ }
            }
            29 -> {
                setAttribute(player, "caller", this)
                player.interfaceManager.open(Component(134).setUncloseEvent { _: Player?, _: Component? ->
                    player.interfaceManager.openDefaultTabs()
                    removeAttribute(player, "lamp")
                    npc(FaceAnim.HAPPY, "Well done finding those penguins. Keep up the hard", "work, they'll keep moving around.")
                    player.unlock()
                    true
                },).also { end() }
            }
            30 -> {
                end()
                addItem(player, Items.COINS_995, 6500 * player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0))
                removeAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE)
                npc(FaceAnim.HAPPY, "Well done finding those penguins. Keep up the hard", "work, they'll keep moving around.")
            }
            31 -> {
                val hint = PenguinLocation.values()[PenguinManager.penguins.random()].hint
                npcl(FaceAnim.FRIENDLY, "I've heard there's a penguin $hint.")
                stage = 18
            }
            32 -> npc("FINE. Be that way.").also { stage = END_DIALOGUE }

            // Lunar Diplomacy.
            33 -> npcl(FaceAnim.FRIENDLY, "One last thing. If you ever discover a place called Lunar Isle, you might gain access to a spell that will help you get in touch with me from anywhere in the world. I can't say any more that that, there may be spies nearby.").also { stage = END_DIALOGUE }
            34 -> {
                end()
                sendItemDialogue(player, Items.SPY_NOTEBOOK_13732, "Larry hands you a penguin spy notebook.")
                addItemOrDrop(player, Items.SPY_NOTEBOOK_13732, 1)
            }
            35 -> {
                if(points > 10) {
                    npcl(FaceAnim.ASKING, "More? They're spreading so quickly.")
                    stage = 18
                } else {
                    player("I've found $points penguin this week.")
                    stage = 18
                }
            }
            36 -> npc(FaceAnim.HAPPY, "I can either reward you with coins or experience.", "Which would you prefer?").also { stage = 27 }

        }
        return true
    }

    override fun handleSelectionCallback(skill: Int, player: Player) {
        val points = player.getAttribute(GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
        if (points == 0) {
            sendMessage(player, "Sorry, but you have no points to redeem.")
            return
        }
        val level = getStatLevel(player, skill)
        val expGained = points?.toDouble()?.times((level * 25))
        log(this.javaClass, Log.INFO, "exp: $expGained")
        player.skills.addExperience(skill, expGained!!)
        setAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
    }

    override fun newInstance(player: Player?): Dialogue = LarryDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.LARRY_5424)
}
