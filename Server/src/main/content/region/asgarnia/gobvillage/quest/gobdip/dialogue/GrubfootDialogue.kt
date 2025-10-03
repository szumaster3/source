package content.region.asgarnia.gobvillage.quest.gobdip.dialogue

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Grubfoot dialogue.
 *
 * # Relations
 * - [Goblin Diplomacy quest][content.region.asgarnia.gobvillage.quest.gobdip.GoblinDiplomacy]
 */
class GrubfootDialogue(player: Player? = null) : Dialogue(player) {

    private var quest: Quest? = null

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.GOBLIN_DIPLOMACY)
        when (quest!!.getStage(player)) {
            100 -> npc(FaceAnim.OLD_NORMAL, "Me lonely.")
            30 -> npc(FaceAnim.OLD_NORMAL, "Me not like this blue colour.")
            20 -> npc(FaceAnim.OLD_NORMAL, "Me not like this orange armour. Make me look like that", "thing.")
            else -> npc(FaceAnim.OLD_NORMAL, "Grubfoot wear red armour! Grubfoot wear green", "armour!")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (quest!!.getStage(player)) {
            100 -> when (stage) {
                0 -> player("Why?").also { stage++ }
                1 -> npc(FaceAnim.OLD_NORMAL, "Other goblins in village follow either General Wartface", "or General Bentnoze. Me try to follow both but then", "me get left out of both groups.").also { stage++ }
                2 -> sendNPCDialogue(player, NPCs.GENERAL_BENTNOZE_4493, "Shut up Grubfoot!", FaceAnim.OLD_NORMAL).also { stage = END_DIALOGUE }
            }

            30 -> when (stage) {
                0 -> player("Why not?").also { stage++ }
                1 -> npc(FaceAnim.OLD_NORMAL, "Me not know. It just make me feel...").also { stage++ }
                2 -> player("Makes you feel blue?").also { stage++ }
                3 -> npc(FaceAnim.OLD_NORMAL, "Makes me feel kinda of sad.").also { stage++ }
                4 -> sendNPCDialogue(player, NPCs.GENERAL_BENTNOZE_4493, "Shut up Grubfoot!", FaceAnim.OLD_NORMAL).also { stage = END_DIALOGUE }
            }

            20 -> when (stage) {
                0 -> player("Look like what thing?").also { stage++ }
                1 -> npc(FaceAnim.OLD_NORMAL, "That fruit thing. The one that orange. What it called?").also { stage++ }
                2 -> player("An orange?").also { stage++ }
                3 -> npc(FaceAnim.OLD_NORMAL, "That right. This armour make me look same colour as", "orange-fruit.").also { stage++ }
                4 -> sendNPCDialogue(player, NPCs.GENERAL_BENTNOZE_4493, "Shut up Grubfoot!", FaceAnim.OLD_NORMAL).also { stage = END_DIALOGUE }
            }

            else -> when (stage) {
                0 -> npc(FaceAnim.OLD_NORMAL, "Why they not make up their minds?").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.GENERAL_BENTNOZE_4493, "Shut up Grubfoot!", FaceAnim.OLD_NORMAL).also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.GENERAL_WARTFACE_4495,
            NPCs.GRUBFOOT_4497,
            NPCs.GRUBFOOT_4498,
            NPCs.GRUBFOOT_4496
        )
}
