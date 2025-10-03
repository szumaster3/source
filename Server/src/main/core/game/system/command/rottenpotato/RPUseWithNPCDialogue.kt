package core.game.system.command.rottenpotato

import core.game.dialogue.Dialogue
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.colorize

@Initializable
class RPUseWithNPCDialogue(player: Player? = null) : Dialogue(player) {

    val ID = 38575795

    override fun newInstance(player: Player?): Dialogue = RPUseWithNPCDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        options("Remove NPC", "Enable Respawning", "Disable Respawning", "Kill", "Copy Appearance")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (buttonId) {
            // remove NPC
            1 -> {
                end()
                npc.isRespawn = false
                npc.clear()
                player.sendMessage(colorize("%RNPC Cleared."))
            }
            // Enable Respawn
            2 -> {
                end()
                npc.isRespawn = true
                player.sendMessage(colorize("%RNPC Respawn Enabled."))
            }
            // Disable Respawn
            3 -> {
                end()
                npc.isRespawn = false
                player.sendMessage(colorize("%RNPC Respawn Disabled."))
            }
            // Kill
            4 -> {
                end()
                npc.impactHandler.manualHit(player, npc.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
            }
            // Copy Appearance
            5 -> {
                end()
                player.appearance.transformNPC(npc.id)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(ID)
}
