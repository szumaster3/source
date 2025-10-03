package content.region.kandarin.ardougne.dialogue

import content.region.kandarin.ardougne.quest.drunkmonk.dialogue.BrotherCedricDialogueFile
import core.api.openDialogue
import core.api.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Brother Cedric dialogue.
 */
@Initializable
class BrotherCedricDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.MONKS_FRIEND) in 10..100) {
            end()
            openDialogue(player, BrotherCedricDialogueFile())
        } else {
            sendDialogue(player, "Brother Cedric is not interested in talking.")
            stage = 0
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BROTHER_CEDRIC_280)
}
