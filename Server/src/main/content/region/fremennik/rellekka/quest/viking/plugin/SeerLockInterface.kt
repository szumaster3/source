package content.region.fremennik.rellekka.quest.viking.plugin

import content.data.GameAttributes
import core.api.getAttribute
import core.api.removeAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import shared.consts.Components

/**
 * Handles the lock interface for the Seer's riddle door in the Fremennik Trials quest.
 */
class SeerLockInterface : InterfaceListener {

    /**
     * List of available letters A-Z.
     */
    private val letters = ('A'..'Z').toList()

    /**
     * Button ids to cycle the letters backward (A -> Z).
     */
    private val letterBacks = listOf(39, 35, 31, 27)

    /**
     * Button ids to cycle the letters forward (Z -> A).
     */
    private val letterForwards = listOf(40, 36, 32, 28)

    /**
     * Button id to submit the selected combination.
     */
    private val enterButton = 1

    /**
     * Seers interface id.
     */
    private val doorLockInterface = Components.SEER_COMBOLOCK_298

    override fun defineInterfaceListeners() {
        onOpen(doorLockInterface) { player, _ ->
            resetPlayerAttributes(player)
            return@onOpen true
        }

        onClose(doorLockInterface) { player, _ ->
            clearPlayerAttributes(player)
            return@onClose true
        }

        on(doorLockInterface) { player, _, _, buttonID, _, _ ->
            handleButtonClick(player, buttonID)
            return@on true
        }
    }

    private fun resetPlayerAttributes(player: Player) {
        (618..621).forEach { player.packetDispatch.sendVarcUpdate(it.toShort(), 0) }
        player.packetDispatch.sendIfaceSettings(0, 2, 298, 0, 1)
        (1..4).forEach { setAttribute(player, "riddle-letter-$it", 0) }
    }

    private fun clearPlayerAttributes(player: Player) {
        (1..4).forEach { removeAttribute(player, "riddle-letter-$it") }
    }

    private fun handleButtonClick(player: Player, buttonID: Int) {
        when (buttonID) {
            in letterBacks -> updateLetter(player, letterBacks.indexOf(buttonID), -1)
            in letterForwards -> updateLetter(player, letterForwards.indexOf(buttonID), 1)
            enterButton -> checkRiddleSolution(player)
        }
    }

    private fun updateLetter(player: Player, index: Int, change: Int) {
        val letterKey = "riddle-letter-${index + 1}"
        val currentValue = getAttribute(player, letterKey, 0)
        if (currentValue == 0 && change < 0) {
            setAttribute(player, letterKey, 25)
        } else {
            player.incrementAttribute(letterKey, change)
        }
    }

    private fun checkRiddleSolution(player: Player) {
        val lettersSelected = (1..4).map { letters[getAttribute(player, "riddle-letter-$it", 0)] }
        val riddleAnswers = listOf("LIFE", "MIND", "TIME", "WIND")

        val currentRiddle = getAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE, 0)
        if (lettersSelected.joinToString("") == riddleAnswers[currentRiddle]) {
            sendMessage(player, "You have solved the riddle!")
            removeAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE)
            setAttribute(player, GameAttributes.QUEST_VIKING_PEER_RIDDLE_SOLVED, true)
        } else {
            sendMessage(player, "You have failed to solve the riddle.")
        }
        player.interfaceManager.close()
    }
}
