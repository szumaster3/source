package content.region.desert.alkharid.quest.desertrescue.dialogue

import content.region.desert.alkharid.quest.desertrescue.TouristTrap
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import shared.consts.NPCs
import shared.consts.Quests

class BedabinNomadDialogue(player: Player? = null) : Dialogue(player) {

    private var quest: Quest? = null

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (npc.id) {
            NPCs.BEDABIN_NOMAD_GUARD_834 -> when (quest!!.getStage(player)) {
                54 -> if (player.inventory.containsItem(TouristTrap.TECHNICAL_PLANS)) {
                    player("Al Shabim said I could enter, here are the plans!")
                    stage++
                } else {
                    npc(
                        FaceAnim.NEUTRAL,
                        "Sorry, but you can't use the tent without permission.",
                        "Orders of Al Shabim."
                    )
                }

                100 -> npc(
                    "Sorry, but you can't use the tent without permission. But",
                    "thanks for all your help with the Bedabin people."
                )

                else -> npc(
                    FaceAnim.NEUTRAL, "Sorry, but you can't use the tent without permission.", "Orders of Al Shabim."
                )
            }

            NPCs.BEDABIN_NOMAD_833, NPCs.BEDABIN_NOMAD_FIGHTER_1239 -> npc(
                FaceAnim.FRIENDLY, "Hello Effendi! How can I help you?"
            )
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (npc.id) {
            NPCs.BEDABIN_NOMAD_GUARD_834 -> when (quest!!.getStage(player)) {
                54 -> when (stage) {
                    0 -> end()
                    1 -> {
                        npc("Okay, go ahead.")
                        stage = 2
                    }

                    2 -> {
                        end()
                        val door = getObject(Location(3169, 3046, 0))
                        if (door!!.id != 2701) SceneryBuilder.replace(door, door.transform(2701), 2)
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(3169, 3046)
                        player.packetDispatch.sendMessage("You walk into the tent.")
                    }
                }

                100 -> end()
                else -> end()
            }

            NPCs.BEDABIN_NOMAD_833 -> when (stage) {
                0 -> {
                    options(
                        "What is this place?",
                        "Where is Shantay Pass?",
                        "What do you have to sell?",
                        "Okay, thanks.",
                    )
                    stage++
                }

                1 -> {
                    when (buttonId) {
                        1 -> {
                            player("What is this place?")
                            stage = 10
                        }

                        2 -> {
                            player("Where is Shantay pass?")
                            stage = 20
                        }

                        3 -> {
                            npc.openShop(player)
                            end()
                        }

                        4 -> {
                            player("Okay, thanks.")
                            stage = 40
                        }
                    }
                }

                10 -> {
                    npc(
                        "This is the camp of Bedabin. Talk to our leader, Al",
                        "Shabim, he'll be happy to chat. We can sell you very",
                        "reasonably priced water...",
                    )
                    stage = 0
                }

                20 -> {
                    npc(
                        "It is North East of here effendi, across the trackless",
                        "desert. It will be a thirsty trip, can I interest you",
                        "in a drink?"
                    )
                    stage = 0
                }

                40 -> end()
            }

            NPCs.BEDABIN_NOMAD_FIGHTER_1239 -> when (stage) {
                0 -> {
                    options("What is this place?", "Where is Shantay Pass?", "Okay, thanks.")
                    stage++
                }

                1 -> when (buttonId) {
                    1 -> {
                        player("What is this place?")
                        stage = 10
                    }

                    2 -> {
                        player("Where is Shantay pass?")
                        stage = 20
                    }

                    3 -> {
                        player("Okay, thanks.")
                        stage = 40
                    }
                }

                10 -> {
                    npc(
                        "This is the camp of Bedabin. Talk to our leader, Al",
                        "Shabim, he'll be happy to chat. We can sell you very",
                        "reasonably priced water..."
                    )
                    stage = 40
                }

                20 -> {
                    npc("It is North East of here effendi, across the trackless", "desert. It will be a thirsty trip.")
                    stage = 40
                }

                40 -> end()
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.BEDABIN_NOMAD_GUARD_834,
        NPCs.BEDABIN_NOMAD_833,
        NPCs.BEDABIN_NOMAD_FIGHTER_1239,
    )
}
