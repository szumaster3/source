package content.region.fremennik.piratecove.dialogue

import content.region.fremennik.plugin.FremennikShipHelper.sail
import content.region.fremennik.plugin.Travel
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

@Initializable
class CaptainBentleyDialogue(player: Player? = null) : Dialogue(player) {

    private var onIsle = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        onIsle = npc.location.getRegionId() == 8508
        if (!onIsle) {
            player("Can we head to Lunar Isle?")
        } else {
            player("Hi.")
            stage = 2
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Sure matey!").also { stage++ }
            1 -> {
                end()
                sail(player, Travel.PIRATES_COVE_TO_LUNAR)
            }
            2 -> npc("And you're wanting what now?").also { stage++ }
            3 -> options("Can you take me back to Rellekka please?", "So we're here?").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("Can you take me back to Rellekka please?").also { stage++ }
                2 -> player("So we're here?").also { stage = 7 }
            }
            5 -> npc("I'll take you as far as Pirates' Cove. You'll have to find", "the rest of the way back yourself.").also { stage++ }
            6 -> {
                end()
                sail(player, Travel.LUNAR_TO_PIRATES_COVE)
            }
            7 -> npc("Yep. You're free to explore the island. Be careful though, ", "the Moon Clan are very powerful, it wouldn't be wise to", "wrong them.").also { stage++ }
            8 -> player("Thanks, i'll keep that seal of passage close.").also { stage++ }
            9 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CaptainBentleyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CAPTAIN_BENTLEY_4540)
}
