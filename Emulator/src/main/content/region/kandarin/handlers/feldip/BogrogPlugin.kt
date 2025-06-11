package content.region.kandarin.handlers.feldip

import content.region.kandarin.handlers.feldip.BogrogPouch.handle
import core.api.getStatLevel
import core.api.interaction.openNpcShop
import core.api.sendItemSelect
import core.api.sendMessage
import core.cache.def.impl.NPCDefinition
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.NPCs

/**
 * Handles the bogrog npc.
 *
 * @author Vexia
 */
@Initializable
class BogrogPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        NPCDefinition.forId(NPCs.BOGROG_4472).handlers["option:talk-to"] = this
        NPCDefinition.forId(NPCs.BOGROG_4472).handlers["option:swap"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "talk-to" -> sequenceDialogue(player, node.asNpc())
            "swap" -> openSwap(player)
        }
        return true
    }

    /**
     * Opens the swap interface.
     *
     * @param player the player.
     */
    private fun openSwap(player: Player) {
        if (getStatLevel(player, Skills.SUMMONING) < 21) {
            sendMessage(player, "You need a Summoning level of at least 21 in order to do that.")
            return
        } else {
            sendItemSelect(player, "Value", "Swap 1", "Swap 5", "Swap 10", "Swap X") { slot: Int?, index: Int? ->
                handle(player, index!!, slot!!)
                Unit
            }
        }
    }

    private fun sequenceDialogue(player : Player, npc : NPC) {
        dialogue(player) {
            npc(npc, FaceAnim.CHILD_NORMAL, "Hey, yooman, what you wanting?")
            options(null, "Can I buy some summoning supplies?", "Are you interested in buying pouch pouches or scrolls?") { selected ->
                when (selected) {
                    1 -> {
                        player(FaceAnim.HALF_ASKING, "Can I buy some summoning supplies?")
                        npc(npc, FaceAnim.CHILD_NORMAL, "Hur, hur, hur! Yooman's gotta buy lotsa stuff if yooman", "wants ta train good!")
                        end {
                            openNpcShop(player, npc.id)
                        }
                    }
                    2 -> {
                        player(FaceAnim.HALF_ASKING, "Are you interested in buying pouch pouches or scrolls?")
                        npc(npc, FaceAnim.CHILD_NORMAL, "Des other ogre's stealin' Bogrog's stock. Gimmie pouches", "and scrolls and yooman gets da shardies.")
                        player(FaceAnim.NEUTRAL, "Ok.")
                        end {
                            openSwap(player)
                        }
                    }
                }
            }
        }
    }
}
