package content.region.asgarnia.falador.quest.rd.cutscene

import content.region.asgarnia.falador.quest.rd.plugin.SirKuamPlugin
import core.api.*
import core.game.activity.Cutscene
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs

class FailCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2996, 3375))
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                player.lock()
                fadeToBlack()
                setMinimapState(player, 2)
                timedUpdate(6)
            }

            1 -> {
                var clearBoss = getAttribute(player, SirKuamPlugin.spawnSirLeye, NPC(0))
                if (clearBoss.id != 0) {
                    clearBoss.clear()
                }
                clearInventory(player)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            fadeFromBlack()
                            return@queueScript delayScript(player, 2)
                        }

                        1 -> {
                            openDialogue(player, SirTiffyCashienFailedDialogueFile(), NPC(NPCs.SIR_TIFFY_CASHIEN_2290))
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }

                endWithoutFade {
                    face(player, findLocalNPC(player, NPCs.SIR_TIFFY_CASHIEN_2290)!!)
                    fadeFromBlack()
                    player.unlock()
                    player.interfaceManager.restoreTabs()
                    player.interfaceManager.openDefaultTabs()
                }
            }
        }
    }
}
