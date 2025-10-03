package content.region.kandarin.gnome.quest.tree.dialogue

import content.region.kandarin.gnome.quest.tree.TreeGnomeVillage.Companion.mazeEntrance
import content.region.kandarin.gnome.quest.tree.TreeGnomeVillage.Companion.mazeVillage
import core.api.*
import core.api.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.Items
import shared.consts.Quests

class ElkoyDialogue : DialogueFile() {
    fun travelCutscene(
        player: Player,
        location: Location,
    ) {
        sendDialogue(player, "Elkoy escorts you through the maze...")
        Pulser.submit(
            object : Pulse(0) {
                var count = 0

                override fun pulse(): Boolean {
                    when (count) {
                        0 -> {
                            closeOverlay(player)
                            openOverlay(player, Components.FADE_TO_BLACK_120)
                        }

                        2 -> {
                            teleport(player, location)
                            closeOverlay(player)
                            openOverlay(player, Components.FADE_FROM_BLACK_170)
                            return true
                        }
                    }
                    count++
                    return false
                }
            },
        )
    }

    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.TREE_GNOME_VILLAGE)
        val locY = player!!.location.y
        val followLocation = if (locY > 3161) "village" else "exit"
        when {
            inInventory(player!!, Items.ORBS_OF_PROTECTION_588) && followLocation == "exit" -> {
                when (stage) {
                    0 -> playerl("Hello Elkoy. I have the orb.").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "Take it to King Bolren, I'm sure he'll be pleased to see you.").also { stage++ }
                    2 -> options("Alright, I'll do that.", "Can you guide me out of the maze now?").also { stage++ }
                    3 -> when (buttonID) {
                        1 -> playerl("Alright, I'll do that.").also { stage = END_DIALOGUE }
                        2 -> playerl("Can you guide me out of the maze now?").also { stage = 4 }
                    }
                    4 -> npcl(FaceAnim.OLD_NORMAL, "If you like, but please take the orb to King Bolren soon.").also { stage++ }
                    5 -> {
                        travelCutscene(player!!, mazeEntrance)
                        stage++
                    }
                    6 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. Please don't lose the orb!").also { stage = END_DIALOGUE }
                }
            }

            inInventory(player!!, Items.ORB_OF_PROTECTION_587) -> {
                when (stage) {
                    0 -> playerl("Hello Elkoy.").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "You're back! And the orb?").also { stage++ }
                    2 -> playerl("I have it here.").also { stage++ }
                    3 -> {
                        if (locY > 3161) {
                            npcl(FaceAnim.OLD_NORMAL, "You're our saviour. Please return it to the village and we are all saved. Would you like me to show you the way to the village?").also { stage++ }
                        } else {
                            npcl(FaceAnim.OLD_NORMAL, "Take the orb to King Bolren, I'm sure he'll be pleased to see you.").also { stage = END_DIALOGUE }
                        }
                    }

                    4 -> options("Yes please.", "No thanks Elkoy.").also { stage++ }
                    5 -> when (buttonID) {
                        1 -> playerl("Yes please.").also { stage = 7 }
                        2 -> playerl("No thanks Elkoy.").also { stage = 6 }
                    }

                    6 -> npcl(FaceAnim.OLD_NORMAL, "Ok then, take care.").also { stage = END_DIALOGUE }
                    7 -> travelCutscene(player!!, mazeVillage).also { stage++ }
                    8 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. Take the orb to King Bolren, I'm sure he'll be pleased to see you.").also { stage = END_DIALOGUE }
                }
            }

            inInventory(player!!, Items.ORBS_OF_PROTECTION_588) || questStage == 100 -> {
                when (stage) {
                    0 -> playerl("Hello Elkoy.").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "You truly are a hero.").also { stage++ }
                    2 -> playerl("Thanks.").also { stage++ }
                    3 -> npcl(FaceAnim.OLD_NORMAL, "You saved us by defeating the warlord. I'm humbled and wish you well.").also { stage++ }
                    4 -> npcl(FaceAnim.OLD_NORMAL, "Would you like me to show you the way to the $followLocation?").also { stage++ }
                    5 -> options("Yes please.", "No thanks Elkoy.").also { stage++ }
                    6 -> when (buttonID) {
                        1 -> playerl("Yes please.").also { stage = 8 }
                        2 -> playerl("No thanks Elkoy.").also { stage = 7 }
                    }

                    7 -> npcl(FaceAnim.OLD_NORMAL,"Ok then, take care.").also { stage = END_DIALOGUE }
                    8 -> {
                        if (followLocation == "village") {
                            travelCutscene(player!!, mazeVillage)
                            stage = 10
                        } else {
                            travelCutscene(player!!, mazeEntrance)
                            stage++
                        }
                    }

                    9 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. Have a safe journey.").also { stage = END_DIALOGUE }
                    10 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. Feel free to have a look around.").also { stage = END_DIALOGUE }
                }
            }

            questStage == 0 -> {
                when (stage) {
                    0 -> playerl("Hello there.").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "Hello, welcome to our maze. I'm Elkoy the tree gnome.").also { stage++ }
                    2 -> playerl("I haven't heard of your sort.").also { stage++ }
                    3 -> npcl(FaceAnim.OLD_NORMAL, "There's not many of us left. Once you could find tree gnomes anywhere in the world, now we hide in small groups to avoid capture.").also { stage++ }
                    4 -> playerl("Capture from whom?").also { stage++ }
                    5 -> npcl(FaceAnim.OLD_NORMAL, "Tree gnomes have been hunted for so called 'fun' since as long as I can remember.").also { stage++ }
                    6 -> npcl(FaceAnim.OLD_NORMAL, "Our main threat nowadays are General Khazard's troops. They know no mercy, but are also very dense. They'll never find their way through our maze.").also { stage++ }
                    7 -> npcl(FaceAnim.OLD_NORMAL, "Have fun.").also { stage = END_DIALOGUE }
                }
            }

            questStage in 1..39 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.OLD_NORMAL, "Oh my! The orb, they have the orb. We're doomed.").also { stage++ }
                    1 -> playerl("Perhaps I'll be able to get it back for you.").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_NORMAL, "Would you like me to show you the way to the $followLocation?").also { stage++ }
                    3 -> options("Yes please.", "No thanks Elkoy.").also { stage++ }
                    4 -> when (buttonID) {
                        1 -> playerl("Yes please.").also { stage = 6 }
                        2 -> playerl("No thanks Elkoy.").also { stage = 5 }
                    }

                    5 -> npcl(FaceAnim.OLD_NORMAL, "Ok then, take care.").also { stage = END_DIALOGUE }
                    6 -> {
                        if (followLocation == "village") {
                            travelCutscene(player!!, mazeVillage)
                        } else {
                            travelCutscene(player!!, mazeEntrance)
                        }
                        stage++
                    }
                    7 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. I hope you get the orb back soon.").also { stage = END_DIALOGUE }
                }
            }

            questStage == 40 -> {
                when (stage) {
                    0 -> playerl("Hello Elkoy.").also { stage++ }
                    1 -> npcl(FaceAnim.OLD_NORMAL, "Did you hear? Khazard's men have pillaged the village! They slaughtered many, and took the other orbs in an attempt to lead us out of the maze. When will the misery end?").also { stage++ }
                    2 -> npcl(FaceAnim.OLD_NORMAL, "Would you like me to show you the way to the $followLocation?").also { stage++ }
                    3 -> options("Yes please.", "No thanks Elkoy.").also { stage++ }
                    4 -> when (buttonID) {
                        1 -> playerl("Yes please.").also { stage = 6 }
                        2 -> playerl("No thanks Elkoy.").also { stage = 5 }
                    }

                    5 -> npcl(FaceAnim.OLD_NORMAL, "Ok then, take care.").also { stage = END_DIALOGUE }
                    6 -> {
                        if (followLocation == "village") {
                            travelCutscene(player!!, mazeVillage)
                            stage = 8
                        } else {
                            travelCutscene(player!!, mazeEntrance)
                            stage++
                        }
                    }
                    7 -> npcl(FaceAnim.OLD_NORMAL, "Please try to get our orbs back for us, otherwise the village is doomed!").also { stage = END_DIALOGUE }
                    8 -> npcl(FaceAnim.OLD_NORMAL, "Here we are. Despite what has happened here, I hope you feel welcome.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
