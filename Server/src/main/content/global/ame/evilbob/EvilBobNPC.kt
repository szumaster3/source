package content.global.ame.evilbob

import content.data.RandomEvent
import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import shared.consts.Music
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Represents the Evil Bob random event NPC.
 * @author szu, Zerken
 */
class EvilBobNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.EVIL_BOB_2478) {
    override fun init() {
        super.init()
        sendChat("meow")
        queueScript(player, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    lock(player, 6)
                    sendChat(player, "No... what? Nooooooooooooo!")
                    animate(player, EvilBobUtils.teleAnim)
                    player.graphics(EvilBobUtils.telegfx)
                    playAudio(player, Sounds.TP_ALL_200)
                    EvilBobUtils.giveEventFishingSpot(player)
                    return@queueScript delayScript(player, 3)
                }

                1 -> {
                    sendMessage(player, "Welcome to ScapeRune.")
                    setAttribute(player, RandomEvent.save(), player.location)
                    player.properties.teleportLocation = Location.create(3419, 4776, 0)
                    playAudio(player, Music.EVIL_BOBS_ISLAND_411)
                    resetAnimator(player)
                    openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
                    AntiMacro.terminateEventNpc(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, EvilBobDialogue(), this.asNpc())
    }
}
