package content.region.kandarin.gnome.quest.grandtree.cutscene

import content.region.kandarin.gnome.quest.grandtree.npc.BlackDemonNPC.Companion.spawnBlackDemon
import core.api.face
import core.api.sendChat
import core.api.sendMessage
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.NPCs

/**
 * Represents the Glough's Pet cutscene in The Grand Tree quest.
 *
 * # Relations
 * [The Grand Tree][content.region.kandarin.quest.grandtree.TheGrandTree]
 */
class GloughsPetCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2491, 9864, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(9882)
        addNPC(NPCs.GLOUGH_671, 48, 8, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                fadeFromBlack()
                teleport(player, 59, 8)
                moveCamera(55, 8)
                rotateCamera(0, 0)
                sendChat(player, "Hello?")
                face(player, getNPC(NPCs.GLOUGH_671)!!)
                timedUpdate(3)
            }

            2 -> {
                moveCamera(48, 8, 300, 5)
                sendChat(player, "Anybody?")
                timedUpdate(3)
            }

            3 -> {
                sendChat(player, "Glough?")
                move(getNPC(NPCs.GLOUGH_671)!!, 52, 8)
                timedUpdate(6)
            }

            4 -> {
                moveCamera(55, 2, 2000)
                rotateCamera(54, 6)
                timedUpdate(4)
            }

            5 -> {
                move(player, 53, 8)
                playerDialogueUpdate(FaceAnim.SCARED, "Glough?")
            }

            6 -> {
                dialogueUpdate(
                    NPCs.GLOUGH_671,
                    FaceAnim.OLD_ANGRY1,
                    "You really are becoming a headache! Well, at least now you can die knowing you were right, it will save me having to hunt you down like all the other human filth of " +
                        settings!!.name +
                        "!",
                )
            }

            7 -> {
                playerDialogueUpdate(FaceAnim.SCARED, "You're crazy, Glough!")
            }

            8 -> {
                dialogueUpdate(
                    NPCs.GLOUGH_671,
                    FaceAnim.OLD_ANGRY1,
                    "Bah! Well, soon you'll see, the gnomes are ready to fight. In three weeks this tree will be dead wood, in ten weeks it will be 30 battleships! Finally we will rid the world of the disease called humanity!",
                )
            }

            9 -> {
                playerDialogueUpdate(FaceAnim.SCARED, "What makes you think I'll let you get away with it?")
            }

            10 -> {
                addNPC(NPCs.BLACK_DEMON_4702, 44, 8, Direction.EAST)
                moveCamera(54, 16, 500)
                rotateCamera(45, 10)
                dialogueUpdate(NPCs.GLOUGH_671, FaceAnim.OLD_ANGRY1, "Fool...meet my little friend!")
            }

            11 -> {
                end()
                spawnBlackDemon(player)
                sendMessage(player, "Glough's run off!")
            }
        }
    }
}
