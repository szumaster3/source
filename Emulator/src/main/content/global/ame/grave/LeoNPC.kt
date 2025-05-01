package content.global.ame.grave

import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.ui.setMinimapState
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

class LeoNPC(override var loot: WeightBasedTable? = null, ) : RandomEventNPC(NPCs.LEO_3508) {

    override fun init() {
        super.init()
        sendChat("Can I borrow you for a minute, ${player.username}?")
        setAttribute(player, RandomEvent.save(), player.location)
        teleport(player, Location.create(1928, 5002, 0), TeleportManager.TeleportType.NORMAL, 1)
        queueScript(player, 3, QueueStrength.NORMAL)
        {
            registerLogoutListener(player, RandomEvent.logout())
            {
                p ->
                p.location = getAttribute(p, RandomEvent.save(), player.location)
            }
            setMinimapState(player, 2)
            faceLocation(player, Location(1928, 5003, 0))
            runTask(player,2) {
                player.dialogueInterpreter.open(LeoDialogue(), NPCs.LEO_3508)
            }
            AntiMacro.terminateEventNpc(player)
            return@queueScript stopExecuting(player)
        }
    }

    override fun tick() {
        super.tick()
        if (RandomFunction.random(1, 10) == 5) {
            sendChat("Can I borrow you for a minute, ${player.username}?")
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(LeoDialogue(), npc)
    }
}
