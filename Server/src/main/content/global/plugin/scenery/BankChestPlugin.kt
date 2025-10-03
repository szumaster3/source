package content.global.plugin.scenery

import core.api.openBankAccount
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Scenery

class BankChestPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Bank chests scenery interaction.
         */

        defineInteraction(IntType.SCENERY, BANK_CHESTS, "bank", "use", "open") { player, _, _ ->
            openBankAccount(player)
            return@defineInteraction true
        }
    }

    companion object {
        private val BANK_CHESTS = intArrayOf(Scenery.BANK_CHEST_3194, Scenery.BANK_CHEST_4483, Scenery.BANK_CHEST_10562, Scenery.BANK_CHEST_14382, Scenery.BANK_CHEST_16695, Scenery.BANK_CHEST_16696, Scenery.BANK_CHEST_21301, Scenery.BANK_CHEST_27662, Scenery.BANK_CHEST_27663)
    }
}
