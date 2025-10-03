package content.region.kandarin.camelot.quest.arthur.dialogue

import content.data.GameAttributes
import content.region.kandarin.camelot.quest.arthur.npc.ThrantaxTheMightyNPC
import core.api.setQuestStage
import core.api.sendDialogue
import core.api.sendDialogueOptions
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Thrantax dialogue.
 *
 * # Relations
 * - [Merlin Crystal][content.region.kandarin.camelot.quest.arthur.MerlinCrystal]
 */
class ThrantaxDialogue : DialogueFile() {
    val options = arrayOf("Snarthanto Candon Termtrick", "Snarthtrick Candanto Termon", "Snarthon Candtrick Termanto")
    var correct = options[2]
    var shuffled = emptyArray<String>()
    private var incantation = ""

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.THRANTAX_THE_MIGHTY_238)

        when (stage) {
            0 -> {
                initThrantax(player!!, false)
                sendDialogue(player!!, "Suddenly a mighty spirit appears!")
                stage++
            }

            1 -> playerl(FaceAnim.THINKING, "Now what were those magic words again?").also { stage++ }
            2 -> {
                shuffled = options.toMutableList().apply { shuffle() }.toTypedArray()

                sendDialogueOptions(
                    player!!,
                    "Select an Option",
                    shuffled[0],
                    shuffled[1],
                    shuffled[2],
                )
                stage++
            }

            3 -> {
                if (buttonID == 1) {
                    incantation = shuffled[0]
                } else if (buttonID == 2) {
                    incantation = shuffled[1]
                } else if (buttonID == 3) {
                    incantation = shuffled[2]
                }

                playerl(FaceAnim.THINKING, incantation.split(" ")[0] + "...")
                stage++
            }

            4 -> {
                playerl(FaceAnim.THINKING, incantation.split(" ")[1] + "...")
                stage++
            }

            5 -> {
                playerl(FaceAnim.THINKING, incantation.split(" ")[2] + "!")
                stage++
            }

            6 -> {
                initThrantax(player!!, true)
                npcl(FaceAnim.OLD_HAPPY, "GRAAAAAARGH!").also { stage++ }
            }

            7 -> {
                if (incantation != correct) {
                    attackPlayer(player!!)
                    player!!.inventory.remove(Item(Items.LIT_BLACK_CANDLE_32, 1))
                    end()
                    stage = END_DIALOGUE
                } else {
                    npc(FaceAnim.OLD_HAPPY, "Thou hast me in thine control. So that I mayst", "return from whence I came, I must grant thee a boon.").also { stage++ }
                }
            }

            8 -> npc(FaceAnim.OLD_HAPPY, "What dost thou wish of me?").also { stage++ }
            9 -> playerl(FaceAnim.SCARED, "I wish to free Merlin from his giant crystal!").also { stage++ }
            10 -> npcl(FaceAnim.OLD_HAPPY, "GRAAAAAARGH!").also { stage++ }
            11 -> npcl(FaceAnim.OLD_HAPPY, "the deed is done.").also { stage++ }
            12 -> npc(FaceAnim.OLD_HAPPY, "Thou mayst now shatter Merlins' crystal with", "excalibur, ").also { stage++ }
            13 -> npc(FaceAnim.OLD_HAPPY, "and I can once more rest. Begone! And leave me once", "more in peace.").also { stage++ }
            14 -> {
                setQuestStage(player!!, Quests.MERLINS_CRYSTAL, 50)
                disappear(player!!)
                end()
                stage = END_DIALOGUE
            }
        }
    }

    private fun disappear(player: Player) {
        val thrantax = player.getAttribute<ThrantaxTheMightyNPC>(GameAttributes.TEMP_ATTR_THRANTAX, null)

        if (thrantax != null) {
            thrantax.player = null
        }
    }

    private fun attackPlayer(player: Player) {
        val thrantax = player.getAttribute<NPC>(GameAttributes.TEMP_ATTR_THRANTAX, null)

        if (thrantax != null) {
            thrantax.attack(player)
        }
    }

    private fun initThrantax(
        player: Player,
        checkMissing: Boolean,
    ) {
        if (checkMissing) {
            val thrantax = player.getAttribute<NPC>(GameAttributes.TEMP_ATTR_THRANTAX, null)

            if (thrantax == null || thrantax != null && !thrantax.isActive) {
                spawn(player)
            }
        } else {
            spawn(player)
        }
    }

    private fun spawn(player: Player) {
        var thrantax = ThrantaxTheMightyNPC(NPCs.THRANTAX_THE_MIGHTY_238, Location(2780, 3515, 0))
        thrantax.player = player
        thrantax.locks.lockMovement(10000)
        thrantax.graphics(Graphics.create(shared.consts.Graphics.RE_PUFF_86))
        thrantax.init()
        thrantax.isRespawn = false
        setAttribute(player, GameAttributes.TEMP_ATTR_THRANTAX, thrantax)
        setAttribute(thrantax, GameAttributes.TEMP_ATTR_THRANTAX_OWNER, player.username)
    }
}
