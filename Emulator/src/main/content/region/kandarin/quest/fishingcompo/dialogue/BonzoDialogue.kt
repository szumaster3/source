package content.region.kandarin.quest.fishingcompo.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Bonzo dialogue.
 *
 * Relations:
 * - [Fishing Contest][content.region.kandarin.quest.fishingcompo.FishingContest]
 */
@Initializable
class BonzoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        val init = args.size < 2
        val hasFishingTrophy = hasAnItem(player, Items.FISHING_TROPHY_26).container != null
        val duringContest = getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)

        stage = when {
            /*
             * With fishing rod.
             */
            init && inInventory(player, Items.FISHING_ROD_307) -> {
                npc("Roll up, roll up! Enter the great Hemenster", "Fishing Contest! Only 5gp entrance fee!")
                0
            }

            /*
             * Without fishing rod.
             */
            init -> {
                npc("Sorry, lad, but you need a fishing", "rod to compete.")
                100
            }

            /*
             * Lost trophy.
             */
            getQuestStage(player, Quests.FISHING_CONTEST) == 20 && !hasFishingTrophy -> {
                npc("Hello champ!")
                1600
            }

            /*
             * During contest.
             */

            duringContest -> {
                npc("You've already paid, you don't need to pay me again!")
                100
            }

            else -> {
                /*
                 * After quest.
                 */
                npc("Hello champ! So any hints on how to fish?")
                1500
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options("I'll enter the competition please.", "No thanks, I'll just watch the fun.")
                stage++
            }

            1 -> when (buttonId) {
                1 -> {
                    player("I'll enter the competition please.").also { stage = 10 }
                }

                2 -> {
                    player("No thanks, I'll just watch the fun.")
                    stage = 100
                }
            }

            10 -> {
                npc(FaceAnim.HAPPY, "Marvelous!")
                stage++
            }

            11 -> if (!removeItem(player, Item(Items.COINS_995, 5), Container.INVENTORY)) {
                player("I don't have the 5gp though...")
                stage++
            } else {
                sendItemDialogue(player, Items.COINS_6964, "You pay Bonzo 5 coins.")
                stage = 20
            }

            12 -> {
                npc("No pay, no play.")
                stage = 100
            }

            20 -> {
                npc(
                    FaceAnim.HAPPY,
                    "Ok, we've got all the fishermen! It's time to roll!",
                )
                stage++
            }

            21 -> {
                npc(
                    FaceAnim.NOD_YES,
                    "Ok, nearly everyone is in their place already. You fish",
                    "in the spot by the willow tree, and the Sinister Stranger,",
                    "you fish by the pipes."
                )
                stage++
            }

            22 -> {
                sendDialogue(player, "Your fishing competition spot is by the willow tree.")
                stage++
            }

            /*
             * Fishing contest.
             * Based on 2008 video source.
             * Competition time: 100 ticks (60 seconds).
             */

            23 -> {
                end()
                val bonzoTableLocation = Location.create(2639, 3437, 0)
                val nearPipeFishingSpot = findLocalNPC(player, NPCs.FISHING_SPOT_309)!!
                val tempSpawn = NPC.create(NPCs.FISHING_SPOT_233, Location.create(2637, 3444, 0))
                val playerWon = getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, false)
                Pulser.submit(object : Pulse(1, player) {
                    var counter: Int = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                if(!getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                                    val willowFishingSpot = findNPC(Location.create(2637, 3444, 0), NPCs.FISHING_SPOT_309)
                                } else {
                                    nearPipeFishingSpot.asNpc().transform(NPCs.FISHING_SPOT_233)
                                }
                            }
                            7 -> sendNPCDialogue(player, NPCs.SINISTER_STRANGER_3677, "Arrgh! WHAT is THAT GHASTLY smell??? I think I will move over here instead...")
                            13 -> npc("Hmm. You'd better go and take the area by the pipes", "then.")
                            16 -> {
                                nearPipeFishingSpot.reTransform()
                                // findLocalNPC(player, NPCs.FISHING_SPOT_309)!!.transform(NPCs.FISHING_SPOT_233)
                                tempSpawn.init()
                                sendDialogue(player, "Your fishing competition spot is now beside the pipes.")
                                registerLogoutListener(player, "fishing-contest") { removeAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST) }
                            }
                            119 -> {
                                tempSpawn.clear()
                                player.faceLocation(Location.create(2641, 3437, 0))
                                npc("Ok folks, time's up!", "Let's see who caught the biggest fish!")
                            }
                            123 -> {
                                lock(player, 6)
                                forceWalk(player, bonzoTableLocation, "")
                            }
                            126 -> {
                                val amount = amountInInventory(player, Items.RAW_GIANT_CARP_338)
                                if (!removeItem(player, Item(Items.RAW_GIANT_CARP_338, amount), Container.INVENTORY)) {
                                    npc(FaceAnim.HAPPY, "We have a new winner!")
                                } else {
                                    animate(player, Animations.MULTI_PUT_833)
                                    sendDialogue(player, "You hand over your catch.")
                                    setAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, true)
                                }
                            }
                            129 -> {
                                if (!playerWon) {
                                    npc("And our winner is... the stranger who", "was fishing over by the pipes!")
                                    return false
                                } else {
                                    npc(FaceAnim.HAPPY, "We have a new winner!")
                                    return true
                                }
                            }
                            132 -> {
                                npc(
                                    FaceAnim.HAPPY,
                                    "The heroic-looking person who was fishing by the pipes",
                                    "has caught the biggest carp I've seen since Grandpa",
                                    "Jack used to compete!"
                                )
                            }
                            135 -> {
                                sendItemDialogue(
                                    player,
                                    Items.FISHING_TROPHY_26,
                                    "You are given the Hemenster fishing trophy!"
                                )
                                addItemOrDrop(player, Items.FISHING_TROPHY_26, 1)
                                setQuestStage(player, Quests.FISHING_CONTEST, 20)
                                return true
                            }
                        }
                        return false
                    }
                })
            }

            100 -> end()

            1500 -> {
                player("I think I'll keep them to myself.")
                stage = 100
            }

            1600 -> {
                player(FaceAnim.HALF_GUILTY, "I don't feel like a champ...")
                stage++
            }

            1601 -> {
                npc(FaceAnim.THINKING, "Why not champ?")
                stage++
            }

            1602 -> {
                player(FaceAnim.SAD, "I lost my fishing trophy...")
                stage++
            }

            1603 -> {
                npc(FaceAnim.FRIENDLY, "Is that all chump? Don't worry, I have a spare!")
                addItem(player, Items.FISHING_TROPHY_26)
                stage = 100
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BONZO_225)
}
