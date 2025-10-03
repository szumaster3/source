package content.region.morytania.phas.quest.hauntedmine.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class ZealotDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        npc("State thy allegiance stranger.")
        end()
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ZealotDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ZEALOT_1528)
}
