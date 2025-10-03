package content.minigame.gnomecook.dialogue

import content.minigame.gnomecook.plugin.*
import content.minigame.gnomecook.plugin.GnomeTipper.getTip
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.colorize

/**
 * Represents the Gnome competition completion dialogue.
 */
class GCCompletionDialogue(val job: GnomeCookingJob) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> {
                val neededItem = player!!.getAttribute<Item>("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", null)
                if (neededItem != null && player!!.inventory.containsItem(neededItem)) {
                    player!!.dialogueInterpreter.sendDialogues(job.npc_id, FaceAnim.OLD_HAPPY, "Thank you!")
                    player!!.inventory.remove(neededItem)
                    player!!.inventory.add(getTip(job.level))
                    player!!.removeAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL")
                    player!!.removeAttribute("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM")
                    var curPoints = player!!.getAttribute("$GC_BASE_ATTRIBUTE:$GC_POINTS", 0)
                    curPoints += 3
                    if (curPoints == 12) {
                        player!!.inventory.add(Item(9474))
                        player!!.sendMessage(colorize("%RYou have been granted a food delivery token. Use it to have food delivered."))
                    } else if (curPoints % 12 == 0) {
                        var curRedeems = player!!.getAttribute("$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD", 0)
                        player!!.setAttribute(
                            "/save:$GC_BASE_ATTRIBUTE:$GC_REDEEMABLE_FOOD",
                            if (curRedeems != 10) ++curRedeems else curRedeems
                        )
                        player!!.sendMessage(colorize("%RYou have been granted a single food delivery charge."))
                    }
                    player!!.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_POINTS", curPoints)
                } else {
                    player!!.dialogueInterpreter.sendDialogues(job.npc_id, FaceAnim.ANGRY, "Where's my food?!")
                }
                stage = END_DIALOGUE
            }
        }
    }
}