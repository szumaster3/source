package content.global.plugin.npc

import core.api.animate
import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.NPCs

/**
 * Handles the TownCrierNPC.
 */
class TownCrierNPC : NPCBehavior(NPCs.TOWN_CRIER_6135, NPCs.TOWN_CRIER_6136, NPCs.TOWN_CRIER_6137, NPCs.TOWN_CRIER_6138, NPCs.TOWN_CRIER_6139) {
    private val forceChatAnimation = mapOf(
        "The Duke of Lumbridge needs a hand." to Animations.TOWN_CRIER_RING_BELL_6865,
        "The squirrels! The squirrels are coming! Noooo, get them out of my head!" to Animations.TOWN_CRIER_SCRATCHES_HEAD_6863
    )
    private val forceChat = forceChatAnimation.entries.toList()

    private var tickDelay = RandomFunction.random(30)
    private val TICK_INTERVAL = 30

    override fun tick(self: NPC): Boolean {
        tickDelay++
        if (tickDelay < TICK_INTERVAL) return super.tick(self)
        tickDelay = 0

        if (RandomFunction.random(100) < 15) {
            self.walkingQueue.reset()
            val (chat, animation) = forceChat.random()
            animate(self, animation)
            sendChat(self, chat)
        }

        return super.tick(self)
    }
}
